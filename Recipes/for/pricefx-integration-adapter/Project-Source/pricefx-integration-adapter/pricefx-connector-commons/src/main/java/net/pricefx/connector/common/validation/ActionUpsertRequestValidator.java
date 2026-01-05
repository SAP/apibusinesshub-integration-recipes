package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_UNIQUENAME;

public class ActionUpsertRequestValidator implements IPFXObjectUpsertRequestValidator {

    @Override
    public void validate(JsonNode input) {
        if (JsonUtil.isArrayNode(input)) {
            for (JsonNode node : input) {
                String uniqueName = JsonUtil.getValueAsText(node.get(FIELD_UNIQUENAME));
                if (StringUtils.isEmpty(uniqueName)) {
                    throw new RequestValidationException("Missing uniqueName for Action");
                }
            }
        }else {
            throw new RequestValidationException("Input must be array");
        }
    }
}
