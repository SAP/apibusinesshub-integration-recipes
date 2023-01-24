package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.util.JsonUtil;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_FIELDNAME;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_LABEL;

public interface ICalculableObjectUpsertor {


    default void replaceAttributeExtension(JsonNode inputNode, Iterable<ObjectNode> metadata) {

        //replace request attribute with attribute extension
        metadata.forEach((ObjectNode m) -> {
            if (ImmutableList.copyOf(inputNode.fieldNames()).contains(JsonUtil.getValueAsText(m.get(FIELD_LABEL)))
                    && JsonUtil.getValueAsText(m.get(FIELD_FIELDNAME)) != null) {
                if (JsonUtil.getValueAsText(inputNode.get(JsonUtil.getValueAsText(m.get(FIELD_LABEL)))) == null) {
                    ((ObjectNode) inputNode).set(JsonUtil.getValueAsText(m.get(FIELD_FIELDNAME)), NullNode.getInstance());
                } else {
                    ((ObjectNode) inputNode).set(JsonUtil.getValueAsText(m.get(FIELD_FIELDNAME)),
                            inputNode.get(JsonUtil.getValueAsText(m.get(FIELD_LABEL))));
                }

                ((ObjectNode) inputNode).remove(JsonUtil.getValueAsText(m.get(FIELD_LABEL)));
            }
        });

    }

}
