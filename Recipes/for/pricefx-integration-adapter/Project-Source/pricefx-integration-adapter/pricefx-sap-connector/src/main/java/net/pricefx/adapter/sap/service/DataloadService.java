package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.DataloadRunner;
import net.pricefx.connector.common.util.PFXTypeCode;

import java.util.HashMap;
import java.util.Map;

import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;

public class DataloadService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;
    private final String uniqueId;

    private final DataloadRunner.DataloadType dataloadType;

    public DataloadService(PFXOperationClient pfxClient, PFXTypeCode typeCode,
                           DataloadRunner.DataloadType dataloadType, String uniqueId) {
        super(pfxClient);
        this.typeCode = typeCode;
        this.uniqueId = uniqueId;
        this.dataloadType = dataloadType;
    }

    @Override
    protected JsonNode execute(JsonNode request) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put(UNIQUE_KEY, uniqueId);

        return new DataloadRunner(getPfxClient(), typeCode, dataloadType, parameters).execute(request);
    }
}
