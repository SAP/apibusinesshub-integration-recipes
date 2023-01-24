package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.DataloadRunner;
import net.pricefx.connector.common.util.PFXTypeCode;

import java.util.HashMap;
import java.util.Map;

import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;

public class TruncateService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;
    private final String uniqueId;

    public TruncateService(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId) {
        super(pfxClient);
        this.typeCode = typeCode;
        this.uniqueId = uniqueId;
    }

    @Override
    protected JsonNode execute(JsonNode request) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put(UNIQUE_KEY, uniqueId);

        return new DataloadRunner(getPfxClient(), typeCode, DataloadRunner.DataloadType.TRUNCATE, parameters).execute(request);
    }
}
