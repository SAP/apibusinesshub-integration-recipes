package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.adapter.sap.util.RequestUtil;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.validation.RequestValidationException;

import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.NON_JSON_INPUT;

public abstract class AbstractJsonRequestService extends AbstractService {

    protected AbstractJsonRequestService(PFXOperationClient pfxClient) {
        super(pfxClient);

    }

    /**
     * accepting JSON payload only
     * if empty request, must supply empty JSON {}
     *
     * @param input
     * @return
     */
    @Override
    public JsonNode execute(Object input) {
        if (input == null) {
            throw new RequestValidationException(NON_JSON_INPUT);
        }

        JsonNode request = RequestUtil.convertRequestToJson(input);

        if (request == null || !(JsonUtil.isObjectNode(request) || request.isArray())) {
            throw new RequestValidationException(NON_JSON_INPUT);
        }

        return execute(request);
    }


    protected abstract JsonNode execute(JsonNode request);


}
