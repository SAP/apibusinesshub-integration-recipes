package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_VALUE;


public class GenericActioner implements IPFXObjectActioner {


    private final PFXOperationClient pfxClient;

    private final String apiPath;


    public GenericActioner(PFXOperationClient pfxClient, String apiPath) {
        this.pfxClient = pfxClient;
        this.apiPath = apiPath;

    }


    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }

    public String getApiPath() {
        return apiPath;
    }

    @Override
    public JsonNode action() {
        Iterable<ObjectNode> results = pfxClient.doAction(apiPath);
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);

        if (results != null && results.iterator().hasNext()) {
            results.forEach((ObjectNode obj) -> {
                obj = obj.retain(FIELD_VALUE);
                if (obj.size() == 0) {
                    obj.put(FIELD_VALUE, getSuccessResponse());
                } else {
                    obj.put(FIELD_VALUE, JsonUtil.getValueAsText(obj.get(FIELD_VALUE)));
                }
                arrayNode.add(obj);
            });
            return arrayNode;
        } else {
            return new ObjectNode(JsonNodeFactory.instance).put(FIELD_VALUE, getFailedResponse());
        }

    }

    @Override
    public String getSuccessResponse() {
        return Boolean.TRUE.toString();
    }

    @Override
    public String getFailedResponse() {
        return Boolean.FALSE.toString();
    }
}