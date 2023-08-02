package net.pricefx.adapter.sap.operation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.adapter.sap.service.ExecuteJsonRequestService;
import net.pricefx.adapter.sap.service.ExecuteService;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.CustomOperation;
import net.pricefx.connector.common.util.PFXTypeCode;

import static net.pricefx.connector.common.util.CustomOperation.EXECUTE_FORMULA;

public class ExecuteOperation {

    private final String targetType;
    private final String uniqueId;
    private final String targetName;
    private final PFXOperationClient pfxClient;
    private final PFXTypeCode typeCode;

    public ExecuteOperation(PFXOperationClient pfxClient, String targetType, String uniqueId, String targetName, PFXTypeCode typeCode) {

        this.targetType = targetType;
        this.uniqueId = uniqueId;
        this.targetName = targetName;
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
    }

    public JsonNode execute(Object input) {
        CustomOperation operation = CustomOperation.validValueOf(targetType);

        if (typeCode == null) {
            return new ExecuteService(pfxClient, operation, uniqueId).execute(null);
        }

        switch (typeCode) {
            case FORMULA:
                return new ExecuteJsonRequestService(pfxClient, EXECUTE_FORMULA, targetName).execute(input);
            default:
                return new ExecuteService(pfxClient, operation, uniqueId).execute(null);

        }

    }
}
