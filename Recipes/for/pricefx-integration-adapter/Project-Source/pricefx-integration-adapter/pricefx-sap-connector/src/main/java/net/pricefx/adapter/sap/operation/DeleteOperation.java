package net.pricefx.adapter.sap.operation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.adapter.sap.service.ActionService;
import net.pricefx.adapter.sap.service.DeleteService;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXOperation;
import net.pricefx.connector.common.util.PFXTypeCode;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;

public class DeleteOperation {

    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final PFXOperationClient pfxClient;
    private final String uniqueId;
    private final boolean byKey;

    public DeleteOperation(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId, IPFXExtensionType extensionType, boolean byKey) {
        this.typeCode = typeCode;
        this.extensionType = extensionType;
        this.pfxClient = pfxClient;
        this.uniqueId = uniqueId;
        this.byKey = byKey;
    }

    public JsonNode delete(String token, Object input) {

        switch (typeCode) {
            case PRODUCTIMAGE:
                return new ActionService(pfxClient, createPath(PFXOperation.PRODUCT_IMAGE_DELETE.getOperation(), uniqueId)).
                        execute(token, null);
            default:
                return new DeleteService(pfxClient, typeCode, extensionType, byKey).execute(token, input);
        }


    }
}
