package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.util.DateUtil;
import net.pricefx.connector.common.util.JsonUtil;

import java.text.ParseException;

import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.*;

public class QuoteUpsertRequestValidator implements IPFXObjectUpsertRequestValidator {

    @Override
    public void validate(JsonNode input) {
        if (input == null || !input.isObject()) {
            throw new RequestValidationException(EMPTY_INPUT_ERROR.getMessage());
        }

        String expiryDate = JsonUtil.getValueAsText(input.get("expiryDate"));
        String targetDate = JsonUtil.getValueAsText(input.get("targetDate"));

        try {
            if (!DateUtil.isAfter(expiryDate, targetDate)) {
                throw new RequestValidationException(EXPIRY_DATE_EARLIER);
            }
        } catch (ParseException ex) {
            throw new RequestValidationException(INVALID_DATE_FORMAT);
        }
    }
}
