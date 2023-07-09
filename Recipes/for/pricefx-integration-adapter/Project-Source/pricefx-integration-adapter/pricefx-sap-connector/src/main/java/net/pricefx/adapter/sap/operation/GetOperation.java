package net.pricefx.adapter.sap.operation;

import com.fasterxml.jackson.databind.JsonNode;
import net.pricefx.adapter.sap.service.FetchService;
import net.pricefx.adapter.sap.util.ResponseUtil;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXLookupTableType;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.lang3.StringUtils;

public class GetOperation {

    private final PFXTypeCode typeCode;
    private final PFXOperationClient pfxClient;
    private final String uniqueId;
    private final String secondaryId;

    private final IPFXExtensionType extensionType;

    public GetOperation(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId, String secondaryId, IPFXExtensionType extensionType) {
        this.typeCode = typeCode;
        this.pfxClient = pfxClient;
        this.uniqueId = uniqueId;
        this.extensionType = extensionType;
        this.secondaryId = secondaryId;
    }

    public JsonNode get(String token, long pageNumber, int pageSize, boolean formatted) {
        if (StringUtils.isEmpty(uniqueId)) {
            throw new RequestValidationException("Unique ID property is mandatory");
        }

        JsonNode node;
        switch (typeCode) {
            case QUOTE:
                node = new FetchService(pfxClient, typeCode, extensionType, uniqueId).get(token, typeCode.getIdentifierFieldNames()[0], uniqueId, true, formatted, pageNumber, pageSize);
                break;
            case LOOKUPTABLE:
                if (((PFXLookupTableType) extensionType).getLookupTableType().isSimple() ||
                        ((PFXLookupTableType) extensionType).getLookupTableType() == PFXLookupTableType.LookupTableType.RANGE ||
                        ((PFXLookupTableType) extensionType).getLookupTableType() == PFXLookupTableType.LookupTableType.MATRIX) {
                    node = new FetchService(pfxClient, typeCode, extensionType, uniqueId).get(token,
                            ((PFXLookupTableType) extensionType).getLookupTableType().getUniqueKey(), uniqueId, false, formatted, pageNumber, pageSize);
                } else {
                    throw new UnsupportedOperationException("GET operation is not supported for multiple Keys Matrix PP");
                }
                break;
            case PRODUCTIMAGE:
                throw new UnsupportedOperationException("Cannot get Product image from Adapter");
            default:
                node = new FetchService(pfxClient, typeCode, extensionType, secondaryId).get(token, typeCode.getIdentifierFieldNames()[0], uniqueId, false, formatted, pageNumber, pageSize);
        }

        return ResponseUtil.formatResponse(node);
    }
}
