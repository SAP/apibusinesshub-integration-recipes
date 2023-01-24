package net.pricefx.adapter.sap.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.util.JsonUtil;

public class ResponseUtil {

    private ResponseUtil() {

    }


    public static JsonNode formatResponse(JsonNode node) {


        if (node == null) {
            return new ObjectNode(JsonNodeFactory.instance);
        } else if (JsonUtil.isArrayNode(node) && node.size() == 1) {
            return node.get(0);
        } else {
            return node;
        }


    }
}
