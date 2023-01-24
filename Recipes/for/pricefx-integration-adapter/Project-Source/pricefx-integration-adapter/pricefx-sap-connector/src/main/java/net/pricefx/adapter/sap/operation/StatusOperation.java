package net.pricefx.adapter.sap.operation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.adapter.sap.service.ActionService;
import net.pricefx.adapter.sap.service.UploadStatusService;
import net.pricefx.adapter.sap.util.ResponseUtil;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.PFXOperation;
import net.pricefx.connector.common.util.PFXTypeCode;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;

public class StatusOperation {

    private final PFXTypeCode typeCode;
    private final PFXOperationClient pfxClient;
    private final String uniqueId;

    public StatusOperation(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId) {
        this.typeCode = typeCode;
        this.pfxClient = pfxClient;
        this.uniqueId = uniqueId;
    }

    public JsonNode get(String token) {
        JsonNode node;
        switch (typeCode) {
            case DATAFILE:
                node = new UploadStatusService(pfxClient, uniqueId).execute(token, null);
                break;
            case PRODUCTIMAGE:
                node = new ActionService(pfxClient, createPath(PFXOperation.PRODUCT_IMAGE_EXIST.getOperation(), uniqueId)).
                        execute(token, null);
                break;

            default:
                throw new UnsupportedOperationException("operation not supported");
        }

        return ResponseUtil.formatResponse(node);

    }
}
