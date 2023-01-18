package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;

public class PostService extends AbstractJsonRequestService {
    private final String apiPath;


    public PostService(PFXOperationClient pfxClient, String apiPath) {
        super(pfxClient);
        this.apiPath = apiPath;
    }


    @Override
    protected JsonNode execute(JsonNode request) {
        return getPfxClient().doPost(apiPath, (ObjectNode) request);
    }
}
