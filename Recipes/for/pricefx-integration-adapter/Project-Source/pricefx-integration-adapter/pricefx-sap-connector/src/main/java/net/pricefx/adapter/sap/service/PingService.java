package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_VALUE;

public class PingService extends AbstractService {

    public PingService(PFXOperationClient pfxClient) {
        super(pfxClient);

    }

    @Override
    public JsonNode execute(Object input) {
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        try {
            getPfxClient().testConnection();
            node.put(FIELD_VALUE, Boolean.TRUE.toString());
        } catch (Exception ex) {
            node.put(FIELD_VALUE, ex.getMessage());
        }
        return node;
    }


}
