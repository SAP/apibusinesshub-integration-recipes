package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.validation.ConnectorException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_VALUE;


public abstract class AbstractGenericExecuter implements IPFXObjectExecuter {


    private final PFXOperationClient pfxClient;
    private final Map<String, String> parameters;


    protected AbstractGenericExecuter(PFXOperationClient pfxClient, Map<String, String> parameters) {
        this.pfxClient = pfxClient;
        this.parameters = parameters;
    }

    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public JsonNode execute(JsonNode request) {

        validateRequest(request);

        String path = buildPath(request);
        if (StringUtils.isEmpty(path)) {
            throw new ConnectorException("Unable to determine execution path");
        }

        ObjectNode dataNode = buildRequest(request);

        if (dataNode == null) {
            throw new ConnectorException("Unable to build execution request");
        }

        JsonNode result = pfxClient.doPost(path, dataNode);
        JsonNode firstResult = JsonUtil.getFirstDataNode(result);
        if (isSuccess(firstResult)) {
            return new ObjectNode(JsonNodeFactory.instance).put(FIELD_VALUE, getSuccessResponse(firstResult));
        } else {
            return new ObjectNode(JsonNodeFactory.instance).put(FIELD_VALUE, getFailedResponse(firstResult));
        }

    }


    protected abstract void validateRequest(JsonNode request);

    protected abstract String buildPath(JsonNode request);

    protected abstract ObjectNode buildRequest(JsonNode request);

    protected abstract boolean isSuccess(JsonNode firstResult);

    protected abstract String getSuccessResponse(JsonNode firstResult);

    protected abstract String getFailedResponse(JsonNode firstResult);
}