package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.validation.ConnectorException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;


public class GenericSingleUpsertor extends GenericUpsertor {

    public GenericSingleUpsertor(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, JsonNode schema) {
        super(pfxClient, typeCode, extensionType, schema);
        withMaximumRecords(1);
    }

    public GenericSingleUpsertor(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, JsonNode schema, boolean showAdditionalKeys, boolean showAdditionalAttributes) {
        super(pfxClient, typeCode, extensionType, schema, showAdditionalKeys, showAdditionalAttributes);
        withMaximumRecords(1);
    }

    public ObjectNode upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean showSystemFields, boolean rawPost) {
        List<JsonNode> results = super.upsert(request, validate, replaceNullKey, convertValueToString, false, showSystemFields, rawPost);
        if (CollectionUtils.isEmpty(results)) {
            return new ObjectNode(JsonNodeFactory.instance);
        } else {
            JsonNode result = results.get(0);
            if (JsonUtil.isObjectNode(result)) {
                return (ObjectNode) result;
            } else {
                throw new ConnectorException("Unexpected output: " + result.toString() + ".Unable to upsert " + getTypeCode());
            }
        }

    }

}