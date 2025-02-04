package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.ConditionRecordUpdater;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXConditionRecordType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class ConditionRecordUpdateService extends AbstractJsonRequestService {

    private final IPFXExtensionType extensionType;

    private final String lastUpdateTimestamp;

    public ConditionRecordUpdateService(PFXOperationClient pfxClient, IPFXExtensionType extensionType, String lastUpdateTimestamp) {

        super(pfxClient);
        this.extensionType = extensionType;
        this.lastUpdateTimestamp = lastUpdateTimestamp;

    }


    @Override
    protected JsonNode execute(JsonNode request) {

        JsonNode node;
        List<JsonNode> results = new ConditionRecordUpdater(getPfxClient(), (PFXConditionRecordType) extensionType, null, lastUpdateTimestamp)
                .upsert(request, true, false, false, false, false);

        if (CollectionUtils.isEmpty(results)) {
            return new ObjectNode(JsonNodeFactory.instance);
        } else {
            if (results.size() == 1) {
                node = results.get(0);
            } else {
                ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
                results.forEach(arrayNode::add);
                node = arrayNode;
            }
        }

        return node;

    }
}
