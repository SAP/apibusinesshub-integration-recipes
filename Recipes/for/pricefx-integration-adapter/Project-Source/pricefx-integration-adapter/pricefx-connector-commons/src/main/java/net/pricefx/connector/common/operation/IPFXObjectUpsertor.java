package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.util.JsonSchemaUtil;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import org.apache.commons.collections4.SetUtils;

import java.util.List;
import java.util.Set;

import static net.pricefx.connector.common.util.JsonSchemaUtil.ITEMS;
import static net.pricefx.connector.common.util.JsonSchemaUtil.SCHEMA_PROPERTIES;

public interface IPFXObjectUpsertor {
    List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields);

    /**
     * validate if any fields not stated in schema exist in request
     *
     * @param schemaNode
     * @param request
     */
    default void validateExtraFields(JsonNode schemaNode, JsonNode request) {
        if (schemaNode == null) {
            return;
        }

        if (schemaNode.get(ITEMS) != null && schemaNode.get(ITEMS).get(SCHEMA_PROPERTIES) != null) {
            JsonValidationUtil.validateExtraFields(schemaNode.get(ITEMS).get(SCHEMA_PROPERTIES), request);
        } else {
            JsonValidationUtil.validateExtraFields(schemaNode.get(SCHEMA_PROPERTIES), request);
        }
    }

    default Set<String> getSchemaFields(JsonNode schemaNode) {
        if (schemaNode == null) {
            return SetUtils.emptySet();
        }

        if (schemaNode.get(ITEMS) != null && schemaNode.get(ITEMS).get(SCHEMA_PROPERTIES) != null) {
            return JsonSchemaUtil.getFields(schemaNode.get(ITEMS));
        } else {
            return JsonSchemaUtil.getFields(schemaNode);
        }
    }
}
