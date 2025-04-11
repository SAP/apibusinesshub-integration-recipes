package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersionDetector;
import com.networknt.schema.ValidationMessage;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXLookupTableType;
import net.pricefx.connector.common.util.PFXTypeCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static net.pricefx.connector.common.util.JsonSchemaUtil.ITEMS;
import static net.pricefx.connector.common.util.JsonSchemaUtil.SCHEMA_PROPERTIES;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_FIELDNAME;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_ID;
import static net.pricefx.connector.common.util.PFXLookupTableType.LOWERBOUND;
import static net.pricefx.connector.common.util.PFXLookupTableType.UPPERBOUND;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.MISSING_MANDATORY_ATTRIBUTES;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.SCHEMA_VALIDATION_ERROR;

public class JsonValidationUtil {

    private JsonValidationUtil() {
    }

    public static void validateExtraFields(JsonNode schemaNode, JsonNode inputNode) {
        if (schemaNode == null) {
            return;
        }
        JsonNode propertiesNode;

        if (schemaNode.get(ITEMS) != null && schemaNode.get(ITEMS).get(SCHEMA_PROPERTIES) != null) {
            propertiesNode = schemaNode.get(ITEMS).get(SCHEMA_PROPERTIES);
        } else {
            propertiesNode = schemaNode.get(SCHEMA_PROPERTIES);
        }

        if (propertiesNode == null) {
            return;
        }

        Set<String> fieldNames = ImmutableSet.copyOf(propertiesNode.fieldNames());

        if (JsonUtil.isArrayNode(inputNode)) {
            for (int i = 0; i < inputNode.size(); i++) {
                JsonNode node = inputNode.get(i);
                if (!fieldNames.containsAll(ImmutableList.copyOf(node.fieldNames()))) {
                    throw new RequestValidationException(i + 1,
                            SCHEMA_VALIDATION_ERROR, "Contains Extra Fields not stated in schema");
                }
            }
        } else if (JsonUtil.isObjectNode(inputNode) &&
                (!fieldNames.containsAll(ImmutableList.copyOf(inputNode.fieldNames())))) {

            throw new RequestValidationException(
                    SCHEMA_VALIDATION_ERROR, "Contains Extra Fields not stated in schema");
        }

    }

    public static void validateMaxElements(JsonNode payload, int maximumRecords) {
        int count = JsonUtil.countJson(payload);
        if (count > maximumRecords) {
            throw new RequestValidationException("Too many elements. Please make sure no of elements < " + maximumRecords);
        }
    }

    public static void validatePayload(JsonNode schemaNode, JsonNode payload) {
        if (payload == null) {
            payload = new ObjectNode(JsonNodeFactory.instance);
        }

        if (JsonUtil.isObjectNode(schemaNode)) {
            ((ObjectNode) schemaNode).remove(FIELD_ID);
            ((ObjectNode) schemaNode).remove("$id");
        }

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(schemaNode));
        JsonSchema schema = factory.getSchema(schemaNode);


        Set<ValidationMessage> errors = schema.validate(payload);
        if (!CollectionUtils.isEmpty(errors)) {
            Optional<ValidationMessage> msg = errors.stream().findFirst();
            if (msg.isPresent()) {
                throw new RequestValidationException(SCHEMA_VALIDATION_ERROR, msg.get().getMessage());
            }
        }

    }

    public static Set<String> getMandatoryAttributes(Map<String, ObjectNode> metadataMap,
                                                     IPFXExtensionType extensionType, PFXTypeCode typeCode) {
        Set<String> mandatoryFields = (extensionType != null) ? extensionType.getMandatoryFields() : typeCode.getMandatoryFields();

        Set<String> mandatory = new HashSet<>();
        if (mandatoryFields != null) {
            mandatory.addAll(mandatoryFields);
        }

        mandatory.addAll(getMandatoryAttributes(metadataMap));

        return mandatory;
    }

    public static Set<String> getMandatoryAttributes(Map<String, ObjectNode> metadata) {
        if (metadata == null) return new HashSet<>();

        return metadata.entrySet().stream()
                .filter(entry -> Boolean.parseBoolean(JsonUtil.getValueAsText(entry.getValue().get("requiredField"))))
                .map(entry -> JsonUtil.getValueAsText(entry.getValue().get(FIELD_FIELDNAME)))
                .collect(Collectors.toSet());

    }

    public static void validateMissingMandatoryAttributes(ObjectNode inputNode, Set<String> mandatory, int i) {
        if (mandatory.isEmpty()) return;

        Set<String> missing = new HashSet<>(mandatory);

        Set<String> existing = JsonUtil.getExistingFields(inputNode);

        missing.removeAll(existing);

        if (!CollectionUtils.isEmpty(missing)) {
            if (i < 0) {
                throw new RequestValidationException(MISSING_MANDATORY_ATTRIBUTES, missing.toString());
            } else {
                throw new RequestValidationException(i, MISSING_MANDATORY_ATTRIBUTES, missing.toString());
            }
        }

    }

    public static void validateDataType(JsonNode node, JsonNode metadataNode, String fieldName, int line) {
        if (metadataNode == null) {
            return;
        }

        if (!node.isValueNode() && !node.isNull()) {
            throw new RequestValidationException(SCHEMA_VALIDATION_ERROR);
        }

        Number type = JsonUtil.getNumericValue(metadataNode.get("fieldType"));

        if (type != null && ImmutableList.of(1, 3).contains(type.intValue())) {
            validateNumericField(node, line, fieldName);
        }

        if ((type == null || !ImmutableList.of(1, 3).contains(type.intValue())) && !node.isTextual() && !node.isNull()) {
            throw new RequestValidationException(line, SCHEMA_VALIDATION_ERROR, fieldName + " should be string.");
        }

    }

    public static void validateNumericField(JsonNode node, int line, String fieldName) {
        if (node.isNull() || node.isNumber()) {
            return;
        }

        if (!node.isTextual()) {
            throw new RequestValidationException(line, SCHEMA_VALIDATION_ERROR,
                    fieldName + " should be numeric.");
        }

        if (StringUtils.isEmpty(node.textValue())) {
            throw new RequestValidationException(line, SCHEMA_VALIDATION_ERROR,
                    fieldName + " is a numeric field and " +
                            "should not be an empty string. please use null instead");
        }

        if (!NumberUtils.isCreatable(node.textValue())) {
            throw new RequestValidationException(line, SCHEMA_VALIDATION_ERROR,
                    fieldName + " should be numeric.");
        }


    }

    public static void validatePPDataType(PFXLookupTableType extensionType, JsonNode node, int line, String fieldName) {

        PFXLookupTableType.LookupTableValueType valueType = extensionType.getLookupTableValueType(fieldName);

        try {
            if (valueType != null) {
                valueType.validate(node.asText());
            }
        } catch (RequestValidationException ex) {
            throw new RequestValidationException("Error in Line " + line + " - " + ex.getMessage());
        }

        if (extensionType.getLookupTableType() == PFXLookupTableType.LookupTableType.RANGE &&
                (LOWERBOUND.equalsIgnoreCase(fieldName) || UPPERBOUND.equalsIgnoreCase(fieldName))) {
            validateNumericField(node, line, fieldName);
        }

    }

}
