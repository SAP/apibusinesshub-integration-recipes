package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.util.DateUtil;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.RequestUtil;

import java.text.ParseException;

import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.EXPIRY_DATE_EARLIER;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.INVALID_DATE_FORMAT;

public class QuoteRequestValidator implements RequestValidator {

    @Override
    public void validate(JsonNode input) {
        if (input == null || !input.isObject()) {
            throw new RequestValidationException(RequestUtil.EMPTY_INPUT_ERROR);
        }

        String expiryDate = JsonUtil.getValueAsText(input.get("expiryDate"));
        String targetDate = JsonUtil.getValueAsText(input.get("targetDate"));

        try {
            if (!DateUtil.isAfter(expiryDate, targetDate)) {
                throw new RequestValidationException(EXPIRY_DATE_EARLIER);
            }
        } catch (ParseException ex) {
            throw new RequestValidationException(INVALID_DATE_FORMAT, "Should be YYYY-MM-DD");
        }
    }
}
