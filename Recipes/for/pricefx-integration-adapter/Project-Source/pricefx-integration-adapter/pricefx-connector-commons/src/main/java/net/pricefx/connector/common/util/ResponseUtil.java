package net.pricefx.connector.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static net.pricefx.connector.common.util.PFXConstants.*;

public class ResponseUtil {
    private static final String RESULT_OBJECT = "resultObject";
    private static final String RESULT = "result";

    private ResponseUtil() {
    }

    private static void formatDataCollectionResponse(ObjectNode node) {
        formatCollectionResponse(node, "fields");
    }

    private static void formatResponse(ObjectNode node) {
        removeNull(node);
        node.remove(SYSTEM_FIELDS);
    }

    private static void formatCollectionResponse(ObjectNode node, String fieldName) {
        formatResponse(node);
        if (JsonUtil.isArrayNode(node.get(fieldName))) {
            node.get(fieldName).forEach((JsonNode field) -> formatResponse((ObjectNode) field));
        }
    }

    /**
     * remove null node from Object Node
     *
     * @param node
     */
    public static void removeNull(ObjectNode node) {
        if (node == null) return;
        List<String> nullFields = new ArrayList<>();
        Iterables.filter(
                JsonUtil.fields(node), item ->
                        !JsonUtil.hasNonNull(node, item.getKey())
        ).forEach((Map.Entry<String, JsonNode> entry) -> nullFields.add(entry.getKey()));

        node.remove(nullFields);
    }

    /**
     * format User (Quote, Contract, etc) response
     *
     * @param node
     */
    private static void formatUserResponse(ObjectNode node) {
        formatCollectionResponse(node, FIELD_GROUPS);
        formatCollectionResponse(node, FIELD_BUSINESSROLES); //businessRoles/roles as well
        formatCollectionResponse(node, "allGroups");
        if (JsonUtil.isArrayNode(node.get(FIELD_BUSINESSROLES))) {
            node.get(FIELD_BUSINESSROLES).forEach((JsonNode field) ->
                    formatCollectionResponse((ObjectNode) field, "roles")
            );
        }
        formatFilterCriteria(node, "productFilterCriteria");
        formatFilterCriteria(node, "customerFilterCriteria");
    }

    private static void formatFilterCriteria(ObjectNode node, String fieldName) {
        //no format is required if it is already an object
        if (node.get(fieldName) != null && node.get(fieldName).isObject()) {
            return;
        }

        String criteria = JsonUtil.getValueAsText(node.get(fieldName));
        try {
            JsonNode criteriaNode = new ObjectMapper().reader().readTree(criteria);
            ((ObjectNode) criteriaNode).remove("_constructor");
            criteriaNode.get(FIELD_CRITERIA).forEach((JsonNode c) -> ((ObjectNode) c).remove("_constructor"));
            node.set(fieldName, criteriaNode);
        } catch (Exception ex) {
            node.remove(fieldName);
        }
    }

    /**
     * format CalculableCollection (Quote, Contract, etc) response
     *
     * @param node
     */
    private static void formatCalculableCollectionResponse(ObjectNode node, boolean convertValueToString) {
        formatCollectionResponse(node, FIELD_INPUTS);
        formatCollectionResponse(node, FIELD_OUTPUTS);

        if (JsonUtil.isArrayNode(node.get(FIELD_LINEITEMS))) {
            node.get(FIELD_LINEITEMS).forEach((JsonNode field) -> {
                formatResponse((ObjectNode) field);
                formatCollectionResponse((ObjectNode) field, FIELD_INPUTS);
                formatCollectionResponse((ObjectNode) field, FIELD_OUTPUTS);
                convertValueField(field.get(FIELD_INPUTS), FIELD_VALUE, convertValueToString);
                convertValueField(field.get(FIELD_OUTPUTS), RESULT, convertValueToString);
            });
        }

        convertValueField(node.get(FIELD_INPUTS), FIELD_VALUE, convertValueToString);
        convertValueField(node.get(FIELD_OUTPUTS), RESULT, convertValueToString);
        formatAttributeExtensions(node);

    }

    private static void convertValueField(JsonNode field, String fieldName, boolean convertValueToString) {
        if (!convertValueToString) return;

        if (JsonUtil.isArrayNode(field)) {
            field.forEach((JsonNode f) -> {
                String value = JsonUtil.getValueAsText(f.get(fieldName));
                if (!StringUtils.isEmpty(value)) {
                    ((ObjectNode) f).put(fieldName, value);
                }
            });
        }
    }

    private static void formatAttributeExtensions(ObjectNode node) {
        Iterable<Map.Entry<String, JsonNode>> fields =
                Iterables.filter(
                        JsonUtil.fields(node), item -> item.getKey().startsWith(ATTRIBUTE_EXT_PREFIX)
                );

        for (Map.Entry<String, JsonNode> field : ImmutableList.copyOf(fields)) {
            node.set(field.getKey().replace(ATTRIBUTE_EXT_PREFIX, ""), field.getValue());
            node.remove(field.getKey());
        }
    }

    /**
     * format lookup table value response
     *
     * @param node
     */
    private static void formatLookupResponse(ObjectNode node) {

        if (node.get(FIELD_RAWVALUE) != null && !node.get(FIELD_RAWVALUE).isMissingNode()) {
            node.replace(FIELD_VALUE, node.get(FIELD_RAWVALUE));
        }

        Set<String> fields = ImmutableMap.copyOf(JsonUtil.fields(node)).keySet();

        if (fields.contains(FIELD_TYPE) && fields.contains(FIELD_VALUETYPE) && fields.contains(FIELD_NAME)) {
            PFXLookupTableType lookupTableType = JsonUtil.getPFXLookupTableType(node);

            if (lookupTableType.getAdditionalKeys() > 0 ||
                    lookupTableType.getLookupTableType() == PFXLookupTableType.LookupTableType.RANGE) {
                node.remove(FIELD_NAME);
            }
        }

        node.remove(LOOKUP_SYSTEM_FIELDS);
    }

    public static List<ObjectNode> formatResponse(PFXTypeCode typeCode, IPFXExtensionType extensionType, Iterable<ObjectNode> results, boolean convertValueToString) {
        if (results == null) {
            return Collections.emptyList();
        }

        List<ObjectNode> list = new ArrayList<>();
        results.forEach((ObjectNode node) -> {
            formatResponse(typeCode, extensionType, node, convertValueToString);
            list.add(node);
        });
        return list;
    }

    private static void moveObjectField(ObjectNode node, String fieldName, String originalField, String objectField) {

        if (JsonUtil.isArrayNode(node.get(fieldName))) {
            node.get(fieldName).forEach(
                    (JsonNode field) -> {
                        if (field.isObject() && field.get(originalField) != null && field.get(originalField).isObject()) {
                            ((ObjectNode) field).set(objectField, field.get(originalField));
                            ((ObjectNode) field).remove(originalField);
                        }
                    });
        }
    }

    public static void preformatResponse(PFXTypeCode typeCode, IPFXExtensionType extensionType, ObjectNode result) {
        if (typeCode == PFXTypeCode.MANUALPRICELIST || typeCode == PFXTypeCode.PAYOUT || typeCode == PFXTypeCode.CONDITION_RECORD) {
            appendId(typeCode, extensionType, result);
        }
    }

    private static void appendId(PFXTypeCode typeCode, IPFXExtensionType extensionType, ObjectNode result) {
        String suffix = typeCode.getTypeCode();
        if (extensionType != null) {
            suffix = extensionType.getTypeCodeSuffix();
        }

        if (!StringUtils.isEmpty(
                JsonUtil.getValueAsText(result.get(FIELD_TYPEDID))) &&
                JsonUtil.getValueAsText(result.get(FIELD_TYPEDID)).endsWith("." + suffix)) {
            result.put(FIELD_ID,
                    Long.parseLong(
                            JsonUtil.getValueAsText(result.get(FIELD_TYPEDID)).replace("." + suffix, "")));
        }

    }

    public static void postformatResponse(PFXTypeCode typeCode, ObjectNode result, boolean convertValueToString) {
        if (typeCode == PFXTypeCode.LOOKUPTABLE) {
            formatLookupResponse(result);
        } else if (PFXTypeCode.isDataCollectionTypeCodes(typeCode)) {
            formatDataCollectionResponse(result);
        } else if (typeCode == PFXTypeCode.QUOTE) {
            formatCalculableCollectionResponse(result, convertValueToString);
            moveObjectField(result, FIELD_INPUTS, FIELD_VALUE, FIELD_VALUEOBJECT);
            moveObjectField(result, FIELD_OUTPUTS, RESULT, RESULT_OBJECT);

            if (JsonUtil.isArrayNode(result.get(FIELD_LINEITEMS))) {
                result.get(FIELD_LINEITEMS).forEach((JsonNode field) -> {
                    if (field.isObject()) {
                        moveObjectField((ObjectNode) field, FIELD_INPUTS, FIELD_VALUE, FIELD_VALUEOBJECT);
                        moveObjectField((ObjectNode) field, FIELD_OUTPUTS, RESULT, RESULT_OBJECT);
                    }
                });
            }
        } else if (typeCode == PFXTypeCode.USER) {
            formatUserResponse(result);
        }
    }

    public static void formatResponse(PFXTypeCode typeCode, IPFXExtensionType extensionType, ObjectNode result, boolean convertValueToString) {
        preformatResponse(typeCode, extensionType, result);

        formatResponse(result);

        postformatResponse(typeCode, result, convertValueToString);

    }

    public static void formatFormulaResponse(ObjectNode result) {

        if (JsonUtil.isObjectNode(result.get(RESULT))) {
            result.set(RESULT_OBJECT, result.get(RESULT));
            result.remove(RESULT);
        } else if (JsonUtil.isArrayNode(result.get(RESULT))) {
            result.set("resultArray", result.get(RESULT));
            result.remove(RESULT);
        }

    }
}
