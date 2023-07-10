package net.pricefx.adapter.sap.operation;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.exception.InvalidContextException;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import com.sap.it.api.securestore.exception.SecureStoreException;
import net.pricefx.connector.common.util.ConnectionUtil;
import net.pricefx.connector.common.validation.ConnectorException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;


public class CredentialsOperation {

    private String pricefxHost;

    private ConnectionUtil.Connection connection;

    private String jwtToken;
    private String userId;

    private boolean jwt = false;


    public CredentialsOperation(String securityMaterial, String host) throws InvalidContextException, SecureStoreException, MalformedURLException {
        init(securityMaterial, host);
    }

    protected void init(String securityMaterial, String host) throws InvalidContextException, SecureStoreException, MalformedURLException {
        SecureStoreService secureStoreService = ITApiFactory.getService(SecureStoreService.class, null);
        UserCredential credential = secureStoreService.getUserCredential(securityMaterial);

        if (credential == null) {
            throw new ConnectorException("Security Material - " + securityMaterial + "not found.");
        }

        userId = credential.getCredentialProperties().get("user");

        String kind = credential.getCredentialProperties().get("sec:credential.kind");

        if ("default".equalsIgnoreCase(kind)) {

            if (!ArrayUtils.isEmpty(credential.getPassword())) {
                jwtToken = new String(credential.getPassword());
            }

            jwt = true;
            credential = secureStoreService.getUserCredential(userId);
            if (credential == null) {
                throw new ConnectorException("Security Material - " + userId + "not found.");
            }

        }

        connection = new ConnectionUtil.Connection(new String(credential.getPassword()));

        String tokenUrl = credential.getCredentialProperties().get("sec:server.url");

        if (StringUtils.isEmpty(host)) {
            this.pricefxHost = ConnectionUtil.getHost(tokenUrl);
        } else {
            this.pricefxHost = host;
        }
    }

    public String getPartition() {
        if (connection == null || StringUtils.isEmpty(connection.getPartition())) {
            throw new ConnectorException("Pricefx Partition is missing.");
        }

        return connection.getPartition();

    }

    public String getPricefxHost() {
        return pricefxHost;
    }

    public String getUserId() {
        return userId;
    }

    public ObjectNode buildTokenRequest() {
        if (connection == null) {
            throw new ConnectorException("Connection is not OAuth");
        }

        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("username", connection.getUsername());
        node.put("partition", connection.getPartition());
        node.put("password", connection.getPassword());
        return node;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public boolean isJwt() {
        return jwt;
    }

    public void setPricefxHost(String pricefxHost) {
        this.pricefxHost = pricefxHost;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;

        if (!StringUtils.isEmpty(jwtToken)) {
            SecureStoreService secureStoreService = null;
            try {
                secureStoreService = ITApiFactory.getService(SecureStoreService.class, null);
                UserCredential credential = secureStoreService.getUserCredential("test-jwt");
                credential.getCredentialProperties().put("password", jwtToken);
            } catch (InvalidContextException e) {
                throw new RuntimeException(e);
            } catch (SecureStoreException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
