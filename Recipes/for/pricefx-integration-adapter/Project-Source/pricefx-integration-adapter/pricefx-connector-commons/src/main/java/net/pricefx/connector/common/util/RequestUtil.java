package net.pricefx.connector.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.smartgwt.client.types.OperatorId;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.smartgwt.client.types.OperatorId.*;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXTypeCode.LOOKUPTABLE;
import static net.pricefx.connector.common.validation.ConnectorException.ErrorType.TABLE_NOT_FOUND;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.INVALID_NUMBER_FORMAT;

public class RequestUtil {

    public static final String EMPTY_INPUT_ERROR = "Input is null!";
    public static final String OPERATOR = "operator";
    private static final List<OperatorId> SUPPORTED_OPERATORS;


    static {
        SUPPORTED_OPERATORS = ImmutableList.of(OperatorId.EQUALS,
                NOT_EQUAL, IEQUALS, INOT_EQUAL,
                GREATER_THAN, LESS_THAN, GREATER_OR_EQUAL, LESS_OR_EQUAL,
                CONTAINS, STARTS_WITH, ENDS_WITH, ICONTAINS, ISTARTS_WITH, IENDS_WITH,
                INOT_STARTS_WITH, INOT_ENDS_WITH,
                NOT_CONTAINS, NOT_STARTS_WITH, NOT_ENDS_WITH, INOT_CONTAINS, IS_NULL, NOT_NULL,
                IN_SET, NOT_IN_SET);
    }

    private RequestUtil() {
    }

    public static ObjectNode buildSimpleCriterion(String fieldName, String operator) {
        return new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, fieldName).put(OPERATOR, operator);
    }

    public static int getPageSize(String pageSize, int maxAllowedRecords) {
        int maxRecords = maxAllowedRecords;
        try {
            if (!StringUtils.isEmpty(pageSize)) {
                maxRecords = Integer.parseInt(pageSize);
            }
        } catch (NumberFormatException ex) {
            throw new ConnectorException("Invalid Page Size.");
        }

        if (maxRecords > maxAllowedRecords) {
            throw new ConnectorException("Page Size must not be > " + maxAllowedRecords);
        }

        if (maxRecords <= 0) {
            throw new ConnectorException("Page Size must be > 0");
        }

        return maxRecords;
    }

    public static long getStartRow(String page, int pageSize) {
        if (StringUtils.isEmpty(page)) return 0;

        long pageNumber;
        try {
            pageNumber = Long.parseLong(page);
        } catch (NumberFormatException ex) {
            throw new ConnectorException("Invalid Page Number.");
        }

        if (pageNumber > 0) {
            return (pageNumber - 1) * pageSize;
        }

        throw new ConnectorException("Page should be a positive number > 0");
    }

    public static ObjectNode createSimpleFetchRequest(ObjectNode criterion) {
        return createSimpleFetchRequest(AND.getValue(), Arrays.asList(criterion));
    }

    public static ObjectNode createSimpleFetchRequest(String mainOperator, List<ObjectNode> criterions) {

        ObjectNode dataNode = new ObjectNode(JsonNodeFactory.instance).put(OPERATOR, mainOperator)
                .put("_constructor", "AdvancedCriteria");
        dataNode.putArray(FIELD_CRITERIA).addAll(criterions);
        return dataNode;
    }

    public static void addAdvancedCriteria(ObjectNode node) {
        node.put("_constructor", "AdvancedCriteria");
    }

    /**
     * support only string, number, boolean
     *
     * @param map
     * @return
     */
    public static ObjectNode createSimpleDataRequest(Map<String, Object> map) {
        ObjectNode dataNode = new ObjectNode(JsonNodeFactory.instance);

        map.forEach((String k, Object v) -> {
            if (v instanceof Boolean) dataNode.put(k, (Boolean) v);
            else if (v instanceof Number) dataNode.put(k, new BigDecimal(v.toString()));
            else dataNode.put(k, (String) v);
        });

        return JsonUtil.buildObjectNode(Pair.of(FIELD_DATA, dataNode));
    }

    /**
     * convert list of query criterions to a simple data request
     * support only -
     * string (default, no need to specify in the map),
     * number (Number.class),
     * boolean (Boolean.class)
     *
     * @param criterions
     * @return
     */
    public static ObjectNode createSimpleDataRequest(List<ObjectNode> criterions, Map<String, Class<?>> valueClass) {
        ObjectNode dataNode = new ObjectNode(JsonNodeFactory.instance);

        criterions.forEach((ObjectNode criterion) -> {
            String fieldName = JsonUtil.getValueAsText(criterion.get(FIELD_FIELDNAME));
            String value = JsonUtil.getValueAsText(criterion.get(FIELD_VALUE));
            if (valueClass.get(fieldName) != null) {
                if (valueClass.get(fieldName).equals(Boolean.class)) {
                    dataNode.put(fieldName, Boolean.parseBoolean(value));
                } else if (valueClass.get(fieldName).equals(Number.class)) {
                    try {
                        dataNode.put(fieldName, new BigDecimal(value));
                    } catch (Exception ex) {
                        throw new RequestValidationException(INVALID_NUMBER_FORMAT);
                    }
                } else {
                    dataNode.put(fieldName, value);
                }
            } else {
                dataNode.put(fieldName, value);
            }
        });
        return JsonUtil.buildObjectNode(Pair.of(FIELD_DATA, dataNode));
    }

    public static boolean isOperatorSupported(String operatorIdValue) {
        if (StringUtils.isEmpty(operatorIdValue)) return false;
        if ("isEmpty".equals(operatorIdValue) || "notEmpty".equals(operatorIdValue)) return true;
        return SUPPORTED_OPERATORS.stream().filter((OperatorId op) -> operatorIdValue.equals(op.getValue())).count() > 0;
    }

    public static boolean isValidValue(String operatorIdValue, JsonNode value) {
        OperatorId operatorId = getOperatorId(operatorIdValue);
        if ("isEmpty".equals(operatorIdValue) || "notEmpty".equals(operatorIdValue)) return true;
        if (operatorId == null) return false;

        switch (operatorId) {
            case IN_SET:
            case NOT_IN_SET:
                return (value != null && value.isArray() && value.size() > 0);
            case IS_NULL:
            case NOT_NULL:
                return true;
            default:
                return (value != null && !value.isMissingNode());

        }
    }

    public static OperatorId getOperatorId(String operatorIdValue) {
        if (StringUtils.isEmpty(operatorIdValue)) return null;
        return SUPPORTED_OPERATORS.stream().filter((OperatorId op) -> operatorIdValue.equals(op.getValue())).findFirst().orElse(null);
    }

    public static boolean isValidUrl(String value) {
        return (HttpUrl.parse(value) != null);
    }

    public static void validateExtensionType(PFXTypeCode typeCode, IPFXExtensionType extensionType) {
        if ((typeCode.isExtension() || typeCode == LOOKUPTABLE || typeCode.isConditionRecord()) &&
                (extensionType == null || StringUtils.isEmpty(extensionType.getTable()))) {
            throw new ConnectorException(TABLE_NOT_FOUND);
        }
    }
}
