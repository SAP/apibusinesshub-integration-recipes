package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.UploadStatusChecker;

import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;

public class UploadStatusService extends AbstractService {


    private final String uniqueId;

    public UploadStatusService(PFXOperationClient pfxClient, String uniqueId) {

        super(pfxClient);

        this.uniqueId = uniqueId;

    }

    @Override
    public JsonNode execute(Object input) {
        return new UploadStatusChecker(getPfxClient(), ImmutableMap.of(UNIQUE_KEY, uniqueId)).execute(null);
    }
}
