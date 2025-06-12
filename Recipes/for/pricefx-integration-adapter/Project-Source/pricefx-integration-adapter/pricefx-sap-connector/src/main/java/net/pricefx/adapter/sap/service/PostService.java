package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;

public class PostService extends AbstractJsonRequestService {
    private final String apiPath;
    private final boolean postRaw;


    public PostService(PFXOperationClient pfxClient, String apiPath, boolean postRaw) {
        super(pfxClient);
        this.apiPath = apiPath;
        this.postRaw = postRaw;
    }


    @Override
    protected JsonNode execute(JsonNode request) {
        if (postRaw){
            return getPfxClient().doPostRaw(apiPath, request);
        }
        return getPfxClient().doPost(apiPath, (ObjectNode) request);
    }
}
