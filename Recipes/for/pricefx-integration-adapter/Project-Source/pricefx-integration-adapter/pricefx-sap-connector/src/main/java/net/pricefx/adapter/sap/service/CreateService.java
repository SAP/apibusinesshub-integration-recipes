package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.QuoteCreator;
import net.pricefx.connector.common.util.PFXTypeCode;

public class CreateService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;

    public CreateService(PFXOperationClient pfxClient, PFXTypeCode typeCode) {
        super(pfxClient);
        this.typeCode = typeCode;
    }

    @Override
    protected JsonNode execute(JsonNode request) {

        ObjectNode node;
        switch (typeCode) {
            case QUOTE:
                node = (ObjectNode) new QuoteCreator(getPfxClient(), null).upsert(request, true, false, false, false, false).get(0);
                break;
            default:
                throw new UnsupportedOperationException("operation not supported");
        }

        if (node == null) {
            return new ObjectNode(JsonNodeFactory.instance);
        } else {
            return node;
        }
    }
}
