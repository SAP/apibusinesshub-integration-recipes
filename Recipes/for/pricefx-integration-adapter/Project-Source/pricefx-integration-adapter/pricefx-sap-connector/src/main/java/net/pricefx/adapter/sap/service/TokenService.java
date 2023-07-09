package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.PFXOperation;
import net.pricefx.connector.common.validation.ConnectorException;
import org.apache.commons.lang3.StringUtils;


public class TokenService {
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";

    private final PFXOperationClient pfxClient;

    public TokenService(PFXOperationClient pfxClient) {
        this.pfxClient = pfxClient;
    }

    public ObjectNode get(ObjectNode request) {
        JsonNode results;
        try {
            results = pfxClient.getAccessToken(PFXOperation.TOKEN.getOperation(), request);
            return new ObjectNode(JsonNodeFactory.instance)
                    .put(ACCESS_TOKEN, results.get(ACCESS_TOKEN).textValue())
                    .put(REFRESH_TOKEN, results.get(REFRESH_TOKEN).textValue());
        } catch (Exception e) {
            throw new ConnectorException("Cannot get Token:" + e.getMessage());
        }

    }

    public ObjectNode getJwt() {

        try {
            pfxClient.refreshJwtToken();
            String results = pfxClient.getLatestJwtToken();
            return new ObjectNode(JsonNodeFactory.instance)
                    .put(ACCESS_TOKEN, results);

        } catch (Exception e) {
            throw new ConnectorException("Cannot get JWT Token:" + e.getMessage());
        }

    }

    public ObjectNode refresh(String token, String refreshToken) {
        if (!StringUtils.isEmpty(token)) {
            pfxClient.updateOAuthToken(token);
        }

        JsonNode results;
        try {

            if (StringUtils.isEmpty(refreshToken)) {
                throw new ConnectorException("Refresh token is missing");
            }

            ObjectNode request = new ObjectNode(JsonNodeFactory.instance).put(REFRESH_TOKEN, refreshToken);
            results = pfxClient.getAccessToken(PFXOperation.REFRESH_TOKEN.getOperation(), request);
            return new ObjectNode(JsonNodeFactory.instance)
                    .put(ACCESS_TOKEN, results.get(ACCESS_TOKEN).textValue())
                    .put(REFRESH_TOKEN, results.get(REFRESH_TOKEN).textValue());
        } catch (Exception e) {
            throw new ConnectorException("Cannot get Token:" + e.getMessage());
        }

    }

}
