package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.GenericDeleter;
import net.pricefx.connector.common.operation.GenericKeyDeleter;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXTypeCode;

public class DeleteService extends AbstractJsonRequestService {

    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final boolean byKey;

    public DeleteService(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType,
                         boolean byKey) {
        super(pfxClient);
        this.typeCode = typeCode;
        this.extensionType = extensionType;
        this.byKey = byKey;
    }

    @Override
    protected JsonNode execute(JsonNode request) {
        if (byKey) {
            return new TextNode(new GenericKeyDeleter(getPfxClient(), typeCode, extensionType).delete(request));
        } else {
            return new TextNode(new GenericDeleter(getPfxClient(), typeCode, extensionType).delete(request));
        }
    }
}
