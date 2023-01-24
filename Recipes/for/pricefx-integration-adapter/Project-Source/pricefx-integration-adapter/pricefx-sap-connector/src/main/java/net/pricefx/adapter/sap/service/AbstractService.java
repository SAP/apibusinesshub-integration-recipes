package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractService {
    private final PFXOperationClient pfxClient;


    protected AbstractService(PFXOperationClient pfxClient) {
        this.pfxClient = pfxClient;

    }

    public JsonNode execute(String token, Object input) {
        setToken(token);
        return execute(input);
    }

    private void setToken(String token) {
        if (!StringUtils.isEmpty(token)) {
            pfxClient.updateOAuthToken(token);
        }
    }

    protected abstract JsonNode execute(Object input);

    protected PFXOperationClient getPfxClient() {
        return pfxClient;
    }

}
