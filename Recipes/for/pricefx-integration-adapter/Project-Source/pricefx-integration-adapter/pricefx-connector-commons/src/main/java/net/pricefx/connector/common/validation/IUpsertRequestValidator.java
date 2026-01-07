package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.util.*;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXLookupTableType.LOWERBOUND;
import static net.pricefx.connector.common.util.PFXLookupTableType.UPPERBOUND;

public interface IUpsertRequestValidator {
    default void validateAttributes(ArrayNode inputNode, Map<String, ObjectNode> metadataMap,
                                    Set<String> schemaFields, IPFXExtensionType extensionType, PFXTypeCode typeCode,
                                    boolean replaceNullKey) {
        Set<String> mandatory = JsonValidationUtil.getMandatoryAttributes(metadataMap, extensionType, typeCode);
        mandatory = SetUtils.intersection(mandatory, schemaFields).toSet();

        for (int i = 0; i < inputNode.size(); i++) {
            JsonNode node = inputNode.get(i);
            if (JsonUtil.isObjectNode(node)) {
                final int lineNo = i + 1;
                JsonValidationUtil.validateMissingMandatoryAttributes((ObjectNode) node, mandatory, lineNo);

                node.fields().forEachRemaining(field -> {
                    if (field != null) {
                        JsonValidationUtil.validateDataType(field.getValue(), metadataMap.get(field.getKey()), field.getKey(), lineNo);
                    }
                });

                if (extensionType instanceof PFXLookupTableType) {

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(FIELD_VALUE), lineNo, FIELD_VALUE);

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(FIELD_NAME), lineNo, FIELD_NAME);

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(LOWERBOUND), lineNo, LOWERBOUND);

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(UPPERBOUND), lineNo, UPPERBOUND);

                } else if (extensionType instanceof PFXExtensionType && replaceNullKey) {
                    replaceNullKeyWithEmptyString((PFXExtensionType) extensionType, (ObjectNode) inputNode.get(i));
                }
            }
        }
    }

    default void replaceNullKeyWithEmptyString(PFXExtensionType extensionType, ObjectNode node) {
        //replace null key with empty string
        Set<String> existingFields = getExistingFields(node);

        Set<String> missingFields = extensionType.getBusinessKeys();
        missingFields.removeAll(existingFields);

        for (String missingField : missingFields) {
            if (node.get(missingField) == null || node.get(missingField).isMissingNode() ||
                    node.get(missingField).textValue() == null) {
                node.put(missingField, StringUtils.EMPTY);
            }
        }
    }

    default Set<String> getExistingFields(ObjectNode inputNode) {

        Iterable<Map.Entry<String, JsonNode>> fields =
                Iterables.filter(
                        JsonUtil.fields(inputNode), item ->
                                !StringUtils.isEmpty(JsonUtil.getValueAsText(inputNode.get(item.getKey())))
                );
        return ImmutableMap.copyOf(fields).keySet();

    }

    default void validate(JsonNode inputNode, Iterable<ObjectNode> metadata, JsonNode schema, PFXTypeCode typeCode, IPFXExtensionType extensionType,
                          boolean replaceNullKey) {

        JsonValidationUtil.validatePayload(schema, inputNode);
        JsonValidationUtil.validateExtraFields(schema, inputNode);

        Set<String> schemaFields = JsonSchemaUtil.getFields(schema);
        Map<String, ObjectNode> metadataMap = new HashMap<>();

        if (metadata != null) {
            metadataMap = StreamSupport.stream(metadata.spliterator(), false)
                    .collect(Collectors.toMap((ObjectNode obj) -> JsonUtil.getValueAsText(obj.get(FIELD_FIELDNAME)), obj -> obj));
        }

        if (JsonUtil.isArrayNode(inputNode)) {
            validateAttributes((ArrayNode) inputNode, metadataMap, schemaFields, extensionType, typeCode, replaceNullKey);
        } else {
            validateAttributes(new ArrayNode(JsonNodeFactory.instance).add(inputNode), metadataMap, schemaFields, extensionType, typeCode, replaceNullKey);
        }

        typeCode.validate(inputNode);

    }

}
