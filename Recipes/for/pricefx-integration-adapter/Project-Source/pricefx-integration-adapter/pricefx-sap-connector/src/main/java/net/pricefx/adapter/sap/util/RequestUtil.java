package net.pricefx.adapter.sap.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pricefx.connector.common.validation.RequestValidationException;

import java.io.InputStream;

import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.NON_JSON_INPUT;

public class RequestUtil {

    private RequestUtil() {

    }

    public static JsonNode convertRequestToJson(Object input) {

        try {
            if (input instanceof String) {
                return new ObjectMapper().readTree((String) input);
            } else if (input instanceof InputStream) {
                return new ObjectMapper().readTree((InputStream) input);
            } else if (input instanceof byte[]) {
                return new ObjectMapper().readTree((byte[]) input);
            }
        } catch (Exception ex) {
            throw new RequestValidationException(NON_JSON_INPUT);
        }
        throw new RequestValidationException(NON_JSON_INPUT);

    }
}
