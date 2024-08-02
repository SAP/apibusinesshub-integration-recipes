package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.ConditionRecordUpdater;
import net.pricefx.connector.common.operation.AdvancedConfigurationUpsertor;
import net.pricefx.connector.common.operation.QuoteUpdater;
import net.pricefx.connector.common.operation.UserAccessUpdater;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXConditionRecordType;
import net.pricefx.connector.common.util.PFXTypeCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class UpdateService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;
    private final String uniqueId;

    private final IPFXExtensionType extensionType;

    public UpdateService(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, String uniqueId) {

        super(pfxClient);
        this.uniqueId = uniqueId;
        this.typeCode = typeCode;
        this.extensionType = extensionType;
    }


    @Override
    protected JsonNode execute(JsonNode request) {

        JsonNode node;
        switch (typeCode) {
            case CONDITION_RECORD:
                List<JsonNode> results = new ConditionRecordUpdater(getPfxClient(), (PFXConditionRecordType) extensionType, null).upsert(request, true, false, false, false, false);

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
                break;
            case ADVANCED_CONFIG:
                node = new AdvancedConfigurationUpsertor(getPfxClient()).upsert(request, true, false, false, false,false).get(0);
                break;
            case QUOTE:
                node = new QuoteUpdater(getPfxClient(), null).upsert(request, true, false, false, false, false).get(0);
                break;
            case ROLE:
            case BUSINESSROLE:
            case USERGROUP:
                node = new UserAccessUpdater(getPfxClient(), typeCode, uniqueId, null).upsert(request, true, false, false, false, false).get(0);
                break;
            default:
                throw new UnsupportedOperationException("Update Operation not supported for " + typeCode);
        }

        return node;

    }
}
