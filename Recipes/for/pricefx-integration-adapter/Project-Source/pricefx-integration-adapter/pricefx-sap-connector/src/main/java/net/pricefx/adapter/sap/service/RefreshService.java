package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.PADataRefresher;
import net.pricefx.connector.common.util.PFXTypeCode;

import java.util.HashMap;
import java.util.Map;

import static net.pricefx.connector.common.util.Constants.INCREMENTAL_LOAD_DATE;
import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;

public class RefreshService {


    private final Map<String, String> parameters;

    private final PFXTypeCode typeCode;
    private final PFXOperationClient pfxClient;

    public RefreshService(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId, String incLoadDate) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;

        this.parameters = new HashMap<>();
        parameters.put(UNIQUE_KEY, uniqueId);
        parameters.put(INCREMENTAL_LOAD_DATE, incLoadDate);

    }

    public JsonNode refresh() {
        switch (typeCode) {
            case DATAMART:
                return new PADataRefresher(pfxClient, parameters).execute(null);
            case TOKEN:
                return new TokenService(pfxClient).refresh(null, parameters.get(UNIQUE_KEY));
            default:
                throw new UnsupportedOperationException("Refresh Operation not supported for " + typeCode);
        }


    }
}
