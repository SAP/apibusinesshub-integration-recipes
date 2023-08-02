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

        String kind = credential.getCredentialProperties().get("sec:credential.kind");
        String base64;
        if ("secure_param".equalsIgnoreCase(kind) && !ArrayUtils.isEmpty(credential.getPassword())) {
            //JWT
            jwt = true;
            pricefxHost = host;
            base64 = new String(credential.getPassword());
        } else if (kind.startsWith("oauth2")) {
            //OAUTH
            userId = credential.getCredentialProperties().get("user");

            String tokenUrl = credential.getCredentialProperties().get("sec:server.url");
            pricefxHost = ConnectionUtil.getHost(tokenUrl);

            base64 = new String(credential.getPassword());
        } else {
            //not JWT and not OAuth
            throw new ConnectorException("OAuth security material or security parameter is required");
        }

        connection = new ConnectionUtil.Connection(base64);
    }

    public String getPartition() {
        if (connection == null || StringUtils.isEmpty(connection.getPartition())) {
            throw new ConnectorException("Incorrect Connection configuration!");
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
            throw new ConnectorException("Incorrect Connection configuration!");
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
    }

    public ConnectionUtil.Connection getConnection() {
        return connection;
    }

    public void setConnection(ConnectionUtil.Connection connection) {
        this.connection = connection;
    }

    public void setJwt(boolean jwt) {
        this.jwt = jwt;
    }
}
