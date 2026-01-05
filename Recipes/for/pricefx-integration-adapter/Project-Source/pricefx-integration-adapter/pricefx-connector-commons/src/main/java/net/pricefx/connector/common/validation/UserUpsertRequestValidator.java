package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.pricefx.connector.common.util.JsonUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_USER_LOGINNAME;

public class UserUpsertRequestValidator implements IPFXObjectUpsertRequestValidator {

    @Override
    public void validate(JsonNode input) {
        if (JsonUtil.isArrayNode(input) && input.size() > 1) {
            Map<String, Integer> duplicates = JsonUtil.findDuplicates((ArrayNode) input, FIELD_USER_LOGINNAME);
            if (!MapUtils.isEmpty(duplicates)) {
                throw new RequestValidationException("Update request message should not contain records of same login names: " + duplicates);
            }
        }
    }
}
