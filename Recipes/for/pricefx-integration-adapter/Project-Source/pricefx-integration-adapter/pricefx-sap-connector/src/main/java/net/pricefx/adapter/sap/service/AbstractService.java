package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;

public abstract class AbstractService {
    private final PFXOperationClient pfxClient;


    protected AbstractService(PFXOperationClient pfxClient) {
        this.pfxClient = pfxClient;

    }


    public abstract JsonNode execute(Object input);

    protected PFXOperationClient getPfxClient() {
        return pfxClient;
    }

}
