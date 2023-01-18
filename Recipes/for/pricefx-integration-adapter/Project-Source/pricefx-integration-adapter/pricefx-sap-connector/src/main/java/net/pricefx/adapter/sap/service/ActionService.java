package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.GenericActioner;

public class ActionService extends AbstractService {
    private final String path;


    public ActionService(PFXOperationClient pfxClient, String path) {
        super(pfxClient);
        this.path = path;
    }

    @Override
    protected JsonNode execute(Object input) {
        return new GenericActioner(getPfxClient(), path).action();
    }
}
