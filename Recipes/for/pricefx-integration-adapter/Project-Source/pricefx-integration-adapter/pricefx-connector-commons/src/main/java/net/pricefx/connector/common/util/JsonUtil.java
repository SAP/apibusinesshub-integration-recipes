package net.pricefx.connector.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static net.pricefx.connector.common.util.PFXConstants.*;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() {
    }

    public static ObjectNode buildObjectNode(Pair<String, JsonNode>... elements) {
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);

        for (Pair<String, JsonNode> element : elements) {
            node.set(element.getKey(), element.getValue());
        }

        return node;
    }

    /**
     * get first object from data array of the response node returned by PFX API
     *
     * @param dataNode
     * @return
     */
    public static JsonNode getFirstDataNode(JsonNode dataNode) {

        if (dataNode == null) {
            return MissingNode.getInstance();
        }

        if (dataNode.isArray()) {
            return dataNode.get(0);
        }

        return dataNode;
    }

    /**
     * get data node from response
     *
     * @param node
     * @return
     */
    public static JsonNode getData(JsonNode node) {

        if (node != null && !node.isMissingNode() && !node.isNull()) {
            node = node.get(FIELD_RESPONSE);
        }

        if (node != null && !node.isMissingNode() && !node.isNull()) {
            return node.get(FIELD_DATA);
        }

        return MissingNode.getInstance();
    }

    /**
     * get list of string by fieldname from an array of objects
     *
     * @param nodes
     * @param fieldName
     * @return
     */
    public static List<String> getStringArray(Iterable<? extends JsonNode> nodes, String fieldName) {
        List<String> results = new ArrayList<>();

        if (nodes == null || StringUtils.isEmpty(fieldName)) {
            return results;
        }

        Iterator<? extends JsonNode> itr = nodes.iterator();
        if (itr != null && itr.hasNext()) {
            nodes.forEach((JsonNode node) -> {
                if (node.isObject()) {
                    String fieldValue = getValueAsText(node.get(fieldName));
                    if (!StringUtils.isEmpty(fieldValue)) results.add(fieldValue);
                }
            });
        }

        return results;
    }

    public static String getValueAsText(JsonNode node) {
        return getValueAsText(node, StringUtils.EMPTY);
    }

    public static String getValueAsText(JsonNode node, String defaultValue) {
        if (node == null || node.isNull()) {
            return defaultValue;
        } else {
            return node.asText(defaultValue);
        }

    }

    public static List<String> getFieldNames(String jsonString) throws IOException {
        if (StringUtils.isEmpty(jsonString)) return Collections.emptyList();

        JsonNode node = OBJECT_MAPPER.reader().readTree(jsonString);
        if (node != null && node.isObject() && node.size() > 0) {
            return ImmutableList.copyOf(node.fieldNames());
        }

        return Collections.emptyList();
    }

    public static JsonNode getSelectedField(String jsonString, String fieldName) throws IOException {
        if (StringUtils.isEmpty(jsonString) || StringUtils.isEmpty(fieldName)) return null;

        JsonNode node = OBJECT_MAPPER.reader().readTree(jsonString);
        return node.get(fieldName);
    }

    public static PFXLookupTableType getPFXLookupTableType(ObjectNode node) {
        return PFXLookupTableType.valueOf(getValueAsText(node.get(FIELD_TYPE)),
                getValueAsText(node.get(FIELD_VALUETYPE)));
    }

    public static PFXConditionRecordType getConditionRecordType(ObjectNode node) {
        Number number = getNumericValue(node.get("keySize"));
        int keys = 0;
        if (number != null) {
            keys = number.intValue();
        }

        number = getNumericValue(node.get(FIELD_ID));
        int tableId = 0;
        if (number != null) {
            tableId = number.intValue();
        }

        return new PFXConditionRecordType(keys).withTableId(tableId).withTable(
                getValueAsText(node.get(FIELD_UNIQUENAME)));


    }

    /**
     * test if a json node has a value
     *
     * @param node
     * @return
     */
    public static boolean isValidValueNode(JsonNode node) {
        return node != null && node.isValueNode() && !StringUtils.isEmpty(node.asText());
    }

    public static int countJson(JsonNode node) {
        if (node == null || node.isMissingNode()) {
            return 0;
        }

        if (node.isArray()) {
            return node.size();
        }

        return 1;
    }

    public static List<String> getStringArray(JsonNode node) {

        List<String> list = new ArrayList<>();

        if (isArrayNode(node)) {
            node.forEach((JsonNode n) -> list.add(n.textValue()));
        }

        return list;
    }

    /**
     * test if a json node is array node
     *
     * @param node
     * @return
     */
    public static boolean isArrayNode(JsonNode node) {
        return (node != null && node.isArray());
    }

    public static Number getNumericValue(JsonNode node) {
        if (node != null && node.isNumber()) {
            return node.numberValue();
        }

        return null;
    }

    public static boolean getBooleanValue(JsonNode node) {
        if (node != null && node.isBoolean()) {
            return node.booleanValue();
        }

        return false;
    }


    public static String getRandomId(String name) {
        return new String(Base64.getEncoder().encode((new Date().getTime() + name).getBytes())).replace("=", "");
    }

    public static String getLabelTranslations(String s) {

        try {
            JsonNode node = OBJECT_MAPPER.reader().readTree(s);
            return node.fields().next().getValue().textValue().trim()
                    .replace("\n", "").replace("\r", "");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Update list of inputs in request with new values
     *
     * @param inputNode
     * @param newValues
     */
    public static void updateInputs(JsonNode inputNode, JsonNode newValues) {
        if (!JsonUtil.isArrayNode(newValues)) {
            return;
        }

        Map<String, JsonNode> map = convertNameValuePairsToMap((ArrayNode) newValues);
        inputNode.forEach((JsonNode input) -> {
            if (map.containsKey(getValueAsText(input.get(FIELD_NAME)))) {
                if (input.get(FIELD_VALUE) != null &&
                        input.get(FIELD_VALUE).getNodeType() != map.get(getValueAsText(input.get(FIELD_NAME))).getNodeType() &&
                        !map.get(getValueAsText(input.get(FIELD_NAME))).isNull()) {
                    switch (input.get(FIELD_VALUE).getNodeType()) {
                        case STRING:
                            ((ObjectNode) input).put(FIELD_VALUE, map.get(getValueAsText(input.get(FIELD_NAME))).asText());
                            break;
                        case NUMBER:
                            ((ObjectNode) input).put(FIELD_VALUE,
                                    new BigDecimal(map.get(getValueAsText(input.get(FIELD_NAME))).asText()));
                            break;
                        case BOOLEAN:
                            ((ObjectNode) input).put(FIELD_VALUE,
                                    Boolean.parseBoolean(map.get(getValueAsText(input.get(FIELD_NAME))).asText()));
                            break;
                        case NULL:
                        case MISSING:
                            ((ObjectNode) input).set(FIELD_VALUE, map.get(getValueAsText(input.get(FIELD_NAME))));
                            break;
                        default:
                            throw new RequestValidationException("Invalid data type for field - " +
                                    getValueAsText(input.get(FIELD_NAME)));
                    }
                } else {
                    ((ObjectNode) input).set(FIELD_VALUE, map.get(getValueAsText(input.get(FIELD_NAME))));
                }

            }
        });
    }

    public static Map<String, JsonNode> convertNameValuePairsToMap(ArrayNode nameValuePairs) {
        Map<String, JsonNode> map = new HashMap<>();
        for (JsonNode nameValuePair : nameValuePairs) {
            String name = getValueAsText(nameValuePair.get(FIELD_NAME));
            if (!StringUtils.isEmpty(name)) {
                if (JsonUtil.isObjectNode(nameValuePair.get(FIELD_VALUEOBJECT)) &&
                        nameValuePair.get(FIELD_VALUEOBJECT).size() > 0) {
                    map.put(name, nameValuePair.get(FIELD_VALUEOBJECT));
                } else if (getValueAsText(nameValuePair.get(FIELD_VALUE)) == null) {
                    map.put(name, NullNode.getInstance());
                } else {
                    map.put(name, nameValuePair.get(FIELD_VALUE));
                }
            }
        }
        return map;
    }

    /**
     * test if a json node is object node
     *
     * @param node
     * @return
     */
    public static boolean isObjectNode(JsonNode node) {
        return (node != null && node.isObject());
    }

    public static boolean hasNonNull(JsonNode node, String fieldName) {
        return node != null && node.hasNonNull(fieldName);
    }

    public static Iterable<Map.Entry<String, JsonNode>> fields(final JsonNode jsonNode) {
        return () -> jsonNode != null ? jsonNode.fields() : Collections.emptyIterator();
    }

    public static Set<String> getExistingFields(ObjectNode inputNode) {

        Iterator<Map.Entry<String, JsonNode>> fields =
                Iterators.filter(
                        inputNode.fields(), item ->
                                !StringUtils.isEmpty(getValueAsText(inputNode.get(item.getKey())))
                );


        return ImmutableMap.copyOf((Iterable<Map.Entry<String, JsonNode>>) () -> fields).keySet();

    }


    public static Map<String, Integer> findDuplicates(ArrayNode arrayNode, String field) {
        List<String> values = arrayNode.findValuesAsText(field);
        return CollectionUtils.getCardinalityMap(values).entrySet().stream().filter(entry -> entry.getValue() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static ArrayNode createArrayNode(JsonNode... nodes) {
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        if (nodes != null) {
            Arrays.stream(nodes).forEach(arrayNode::add);
        }
        return arrayNode;
    }

    public static JsonNode getResponse(JsonNode node){
        if (node != null && !node.isMissingNode() && !node.isNull()) {
            node = node.get(FIELD_RESPONSE);
        }

        if (node != null && !node.isMissingNode() && !node.isNull()) {
            return node;
        }

        return MissingNode.getInstance();

    }

    public static ArrayNode createArrayNodeFromStrings(List<String> list){
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(arrayNode::add);
        }
        return arrayNode;
    }

    public static ArrayNode createArrayNodeFromStrings(Set<String> list){
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(arrayNode::add);
        }
        return arrayNode;
    }

    public static ArrayNode createArrayNode(List<JsonNode> nodes){
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        if (!CollectionUtils.isEmpty(nodes)) {
            nodes.forEach(arrayNode::add);
        }
        return arrayNode;
    }


}