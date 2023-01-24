package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.QuoteUpdater;
import net.pricefx.connector.common.operation.UserAccessUpdater;
import net.pricefx.connector.common.util.PFXTypeCode;

public class UpdateService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;
    private final String uniqueId;

    public UpdateService(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId) {

        super(pfxClient);
        this.uniqueId = uniqueId;
        this.typeCode = typeCode;
    }


    @Override
    protected JsonNode execute(JsonNode request) {

        JsonNode node;
        switch (typeCode) {
            case QUOTE:
                node = new QuoteUpdater(getPfxClient(), null).upsert(request, true, false, false, false).get(0);
                break;
            case ROLE:
            case BUSINESSROLE:
            case USERGROUP:
                node = new UserAccessUpdater(getPfxClient(), typeCode, uniqueId, null).upsert(request, true, false, false, false).get(0);
                break;
            default:
                throw new UnsupportedOperationException("Update Operation not supported for " + typeCode);
        }

        return node;

    }
}
