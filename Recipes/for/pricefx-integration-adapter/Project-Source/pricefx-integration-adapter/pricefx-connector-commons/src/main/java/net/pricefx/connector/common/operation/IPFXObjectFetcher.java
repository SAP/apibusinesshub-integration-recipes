package net.pricefx.connector.common.operation;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.JsonSchemaUtil;
import net.pricefx.connector.common.util.PFXJsonSchema;
import net.pricefx.connector.common.util.PFXTypeCode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IPFXObjectFetcher {

    List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate, boolean formatted);

    List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, boolean validate, boolean formatted);

    List<ObjectNode> fetch(ObjectNode request, Long startRow, int pageSize, boolean validate, boolean formatted);

    default JsonNode loadSchema(PFXJsonSchema schema, PFXTypeCode typeCode,
                                IPFXExtensionType extensionType, List<Pair<String, String>> attributes, boolean showAdditionalKeys,
                                boolean showAdditionalAttributes, boolean withType) {

        JsonNode schemaNode = JsonSchemaUtil.loadSchema(schema, false);

        JsonSchemaUtil.updateSchemaWithMetadata(schemaNode, typeCode, extensionType, attributes,
                showAdditionalKeys, showAdditionalAttributes, withType);

        return schemaNode;

    }
}
