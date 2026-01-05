package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.util.JsonSchemaUtil;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXJsonSchema;
import net.pricefx.connector.common.util.RequestUtil;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.lang3.StringUtils;

import static net.pricefx.connector.common.util.OperatorId.AND;
import static net.pricefx.connector.common.util.OperatorId.OR;
import static net.pricefx.connector.common.util.PFXConstants.*;

public interface IPFXObjectFilterRequester {


    default void validateCriteria(JsonNode inputNode, boolean mandatory, PFXJsonSchema schema) {

        JsonNode schemaNode = JsonSchemaUtil.loadSchema(schema, true);

        JsonValidationUtil.validatePayload(schemaNode, inputNode);
        if (!JsonUtil.isObjectNode(inputNode) || !JsonUtil.isArrayNode(inputNode.get(FIELD_CRITERIA)) ||
                inputNode.get(FIELD_CRITERIA).size() == 0) {
            if (mandatory) {
                throw new RequestValidationException("Criteria must be specified");
            } else {
                return;
            }
        }

        if (StringUtils.isEmpty(JsonUtil.getValueAsText(inputNode.get(RequestUtil.OPERATOR)))) {
            throw new RequestValidationException("Missing Operator");
        }

        if (!validateCriteria(inputNode) ||
                !ImmutableList.of(AND.getValue(), OR.getValue()).contains(JsonUtil.getValueAsText(inputNode.get(RequestUtil.OPERATOR)))) {
            throw new UnsupportedOperationException("Invalid criteria. Operators not supported or missing fieldName");
        }

        for (JsonNode node : inputNode.get(FIELD_CRITERIA)) {
            if (node.get(FIELD_VALUE) != null && node.get(FIELD_VALUE).isNumber()){
                ((ObjectNode) node).put(FIELD_VALUE, node.get(FIELD_VALUE).numberValue()+"");
            }
        }


    }

    default boolean validateCriteria(JsonNode inputNode) {
        if (inputNode.get(FIELD_CRITERIA) == null || !inputNode.get(FIELD_CRITERIA).isArray()) {
            return false;
        }

        for (JsonNode node : inputNode.get(FIELD_CRITERIA)) {
            if (!JsonUtil.isObjectNode(node) ||
                    StringUtils.isEmpty(JsonUtil.getValueAsText(node.get(FIELD_FIELDNAME))) ||
                    !RequestUtil.isOperatorSupported(JsonUtil.getValueAsText(node.get(RequestUtil.OPERATOR))) ||
                    !RequestUtil.isValidValue(JsonUtil.getValueAsText(node.get(RequestUtil.OPERATOR)), node.get(FIELD_VALUE),
                             node.get("start"), node.get("end"))) {
                return false;
            }
        }
        return true;
    }

}
