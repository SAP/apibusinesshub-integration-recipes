package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.adapter.sap.component.Producer;
import net.pricefx.adapter.sap.operation.CredentialsOperation;
import net.pricefx.adapter.sap.operation.GetOperation;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.client.okhttp.PfxClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import static net.pricefx.adapter.sap.util.Constants.ACCESS_TOKEN;
import static net.pricefx.connector.common.util.Constants.MAX_RECORDS;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_UNIQUENAME;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_VALUE;


public class TokenService {
    private static final String REFRESH_TOKEN = "refresh-token";
    public static final String SAP_TOKEN_LAST_UPDATE = "SAP_TOKEN_LAST_UPDATE";


    private final PFXOperationClient pfxClient;

    public TokenService(PFXOperationClient pfxClient) {
        this.pfxClient = pfxClient;

    }

    public ObjectNode get(ObjectNode request) {
        JsonNode results;
        try {
            results = pfxClient.getAccessToken(PFXOperation.TOKEN.getOperation(), request);

            updateTokenTimestampConfig(JsonUtil.getValueAsText(results.get(ACCESS_TOKEN)));

            return new ObjectNode(JsonNodeFactory.instance)
                    .put(ACCESS_TOKEN, JsonUtil.getValueAsText(results.get(ACCESS_TOKEN)))
                    .put(REFRESH_TOKEN, JsonUtil.getValueAsText(results.get(REFRESH_TOKEN)));


        } catch (Exception e) {
            throw new ConnectorException("Cannot get Token:" + e.getMessage());
        }

    }

    private void updateTokenTimestampConfig(String token){

        ObjectNode updateAdvRequest = new ObjectNode(JsonNodeFactory.instance)
                .put(FIELD_UNIQUENAME, SAP_TOKEN_LAST_UPDATE).put(FIELD_VALUE, DateUtil.getCurrentTime());

        pfxClient.updateOAuthToken(token);
        new UpdateService(pfxClient, PFXTypeCode.ADVANCED_CONFIG, null, SAP_TOKEN_LAST_UPDATE).execute(updateAdvRequest);


    }



    public ObjectNode getJwt(CredentialsOperation credentialsOperation) {

        try {
            pfxClient.refreshJwtToken();
            String results = pfxClient.getLatestJwtToken();

            ObjectNode updateAdvRequest = new ObjectNode(JsonNodeFactory.instance)
                    .put(FIELD_UNIQUENAME, SAP_TOKEN_LAST_UPDATE).put(FIELD_VALUE, DateUtil.getCurrentTime());


            PfxClientBuilder builder = Producer.getPFXClientBuilder(credentialsOperation.getPartition(),
                    credentialsOperation.getPricefxHost(), results);

            PFXOperationClient updatedPfxClient = (PFXOperationClient) builder.build();

            new UpdateService(updatedPfxClient, PFXTypeCode.ADVANCED_CONFIG, null, SAP_TOKEN_LAST_UPDATE).execute(updateAdvRequest);

            return new ObjectNode(JsonNodeFactory.instance).put(ACCESS_TOKEN, results);
        } catch (Exception e) {
            e.printStackTrace();
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

            updateTokenTimestampConfig(JsonUtil.getValueAsText(results.get(ACCESS_TOKEN)));

            return new ObjectNode(JsonNodeFactory.instance)
                    .put(ACCESS_TOKEN, JsonUtil.getValueAsText(results.get(ACCESS_TOKEN)))
                    .put(REFRESH_TOKEN, JsonUtil.getValueAsText(results.get(REFRESH_TOKEN)));
        } catch (Exception e) {
            throw new ConnectorException("Cannot get Token:" + e.getMessage());
        }

    }

    public static boolean isTokenRetryAllowanceExpired(PFXOperationClient pfxClient){

        try {
            JsonNode node = new GetOperation(pfxClient, PFXTypeCode.ADVANCED_CONFIG, TokenService.SAP_TOKEN_LAST_UPDATE, null, null)
                    .get(0, MAX_RECORDS, false);

            String value = JsonUtil.getValueAsText(node.get(PFXConstants.FIELD_VALUE));

            String fourHourAgo = DateUtil.getFormattedDateTime(DateUtils.addHours(
                    DateUtil.getDateTime(DateUtil.getCurrentTime()), -4));
            return !DateUtil.isAfterTimestamp(value, fourHourAgo);
        }catch(Exception ex){
            return true;
        }
    }

}
