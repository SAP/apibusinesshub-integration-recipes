package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.adapter.sap.util.ResponseUtil;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.FormulaExecuter;
import net.pricefx.connector.common.util.CustomOperation;


public class ExecuteJsonRequestService extends AbstractJsonRequestService {
    private final CustomOperation operation;
    private final String targetName;

    public ExecuteJsonRequestService(PFXOperationClient pfxClient, CustomOperation operation, String targetName) {
        super(pfxClient);
        this.operation = operation;
        this.targetName = targetName;
    }


    @Override
    protected JsonNode execute(JsonNode request) {
        JsonNode node;
        switch (operation) {
            case EXECUTE_FORMULA:
                node = new FormulaExecuter(getPfxClient(), targetName).execute(request);
                break;
            default:
                throw new UnsupportedOperationException("Execute operation not supported");
        }

        return ResponseUtil.formatResponse(node);


    }
}
