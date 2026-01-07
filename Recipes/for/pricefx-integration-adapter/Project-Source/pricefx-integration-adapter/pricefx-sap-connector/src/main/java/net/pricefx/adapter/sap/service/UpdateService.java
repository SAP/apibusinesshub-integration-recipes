package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.*;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXTypeCode;

public class UpdateService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;
    private final String uniqueId;
    private final IPFXExtensionType extensionType;

    private final String lastUpdatedTimestamp;

    public UpdateService(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType,  String uniqueId, String lastUpdatedTimestamp) {

        super(pfxClient);
        this.uniqueId = uniqueId;
        this.typeCode = typeCode;
        this.extensionType = extensionType;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;

    }


    @Override
    protected JsonNode execute(JsonNode request) {

        JsonNode node;
        switch (typeCode) {
            case CONDITION_RECORD:
                node = new ConditionRecordUpdater(getPfxClient(), extensionType, lastUpdatedTimestamp).bulkLoad(request, true);
                break;
            case ADVANCED_CONFIG:
                node = new AdvancedConfigurationUpsertor(getPfxClient()).upsert(request, true, false, false, false, false);
                break;
            case QUOTE:
                node = new QuoteUpdater(getPfxClient(), null).upsert(request, true, false, false, false, false, false).get(0);
                break;
            case ROLE:
            case BUSINESSROLE:
            case USERGROUP:
                node = new UserAccessUpdater(getPfxClient(), typeCode, uniqueId, null).upsert(request, true, false, false, false, false, false).get(0);
                break;
            default:
                throw new UnsupportedOperationException("Update Operation not supported for " + typeCode);
        }

        return node;

    }
}
