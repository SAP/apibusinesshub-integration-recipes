package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.ConditionRecordBulkLoader;
import net.pricefx.connector.common.operation.DatafeedBulkLoader;
import net.pricefx.connector.common.operation.GenericBulkLoader;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXTypeCode;

public class BulkLoadService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final String tableName;
    private final boolean validation;

    private final boolean superseded;

    public BulkLoadService(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, String tableName, boolean validation, boolean superseded) {
        super(pfxClient);
        this.typeCode = typeCode;
        this.extensionType = extensionType;
        this.tableName = tableName;
        this.validation = validation;
        this.superseded = superseded;
    }

    @Override
    protected JsonNode execute(JsonNode request) {

        switch (typeCode) {
            case CONDITION_RECORD:
                return new ConditionRecordBulkLoader(getPfxClient(), extensionType, superseded).bulkLoad(request, validation);
            case DATAFEED:
                return new DatafeedBulkLoader(getPfxClient(), tableName).bulkLoad(request);
            default:
                return new GenericBulkLoader(getPfxClient(), typeCode, extensionType, tableName).bulkLoad(request, validation);
        }

    }
}
