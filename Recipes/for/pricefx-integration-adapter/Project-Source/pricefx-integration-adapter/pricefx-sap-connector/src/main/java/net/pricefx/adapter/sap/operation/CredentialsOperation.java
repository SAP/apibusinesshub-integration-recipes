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
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;


public class CredentialsOperation {

    private String pricefxHost;

    private ConnectionUtil.Connection connection;

    private String jwtToken;
    private String clientId;

    public CredentialsOperation(String securityMaterial, String host, String partition) throws InvalidContextException, SecureStoreException, MalformedURLException {
        init(securityMaterial, host, partition);
    }

    protected void init(String securityMaterial, String host, String partition) throws InvalidContextException, SecureStoreException, MalformedURLException {
        SecureStoreService secureStoreService = ITApiFactory.getService(SecureStoreService.class, null);
        UserCredential credential = secureStoreService.getUserCredential(securityMaterial);

        if (credential == null) {
            throw new ConnectorException("Security Material - " + securityMaterial + "not found.");
        }

        clientId = credential.getCredentialProperties().get("user");
        if (StringUtils.isEmpty(clientId) || "none".equalsIgnoreCase(clientId)) {
            jwtToken = new String(credential.getPassword());
            clientId = partition;
        } else {
            connection = new ConnectionUtil.Connection(new String(credential.getPassword()));
        }


        String tokenUrl = credential.getCredentialProperties().get("sec:server.url");

        if (StringUtils.isEmpty(host)){
            this.pricefxHost = ConnectionUtil.getHost(tokenUrl);
        } else {
            this.pricefxHost = host;
        }
    }

    public String getUsername() {
        return connection.getUsername();
    }

    public String getPartition() {
        if (connection == null || StringUtils.isEmpty(connection.getPartition())) {
            return clientId;
        }else{
            return connection.getPartition();
        }

    }

    public String getPricefxHost() {
        return pricefxHost;
    }

    public String getClientId() {
        return clientId;
    }

    public ObjectNode buildTokenRequest() {
        if (connection == null){
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
        return (connection == null);
    }

    public void setPricefxHost(String pricefxHost) {
        this.pricefxHost = pricefxHost;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
