package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.JsonSchemaUtil;
import net.pricefx.connector.common.util.PFXJsonSchema;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static net.pricefx.connector.common.util.JsonSchemaUtil.ITEMS;
import static net.pricefx.connector.common.util.JsonSchemaUtil.SCHEMA_PROPERTIES;

public interface IPFXObjectUpsertor {
    List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields);

    /**
     * get schema for validation. additional attributes are added from metadata
     *
     * @param schema
     * @param typeCode
     * @param extensionType
     * @param attributes
     * @param showAdditionalKeys
     * @param showAdditionalAttributes
     * @param withType
     * @return
     */
    default JsonNode loadSchema(PFXJsonSchema schema, PFXTypeCode typeCode,
                                IPFXExtensionType extensionType, List<Pair<String, String>> attributes, boolean showAdditionalKeys,
                                boolean showAdditionalAttributes, boolean withType) {

        JsonNode schemaNode = JsonSchemaUtil.loadSchema(schema, true);

        JsonSchemaUtil.updateSchemaWithMetadata(schemaNode, typeCode, extensionType, attributes,
                showAdditionalKeys, showAdditionalAttributes, withType);

        return schemaNode;

    }

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
}
