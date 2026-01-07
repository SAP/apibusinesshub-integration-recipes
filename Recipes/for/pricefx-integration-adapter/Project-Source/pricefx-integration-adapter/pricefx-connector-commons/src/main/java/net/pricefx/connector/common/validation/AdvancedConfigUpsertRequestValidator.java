package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_UNIQUENAME;

public class AdvancedConfigUpsertRequestValidator implements IPFXObjectUpsertRequestValidator {

    @Override
    public void validate(JsonNode input) {
        String uniqueName = JsonUtil.getValueAsText(input.get(FIELD_UNIQUENAME));
        if (StringUtils.isEmpty(uniqueName)) {
            throw new RequestValidationException("Invalid upsert Advanced Config request. Missing uniqueName!");
        }
    }
}
