package net.pricefx.connector.common.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.lang3.StringUtils;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_INPUTS;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_LINEITEMS;
import static net.pricefx.connector.common.util.PFXOperation.*;

public class RequestPathFactory {
    private RequestPathFactory() {
    }

    private static String buildFetchSubtypePath(String extensionSubtype) {
        if (!StringUtils.isEmpty(extensionSubtype)) {
            PFXTypeCode typeCode = PFXTypeCode.findByTypeCodeOrName(extensionSubtype, null);

            if (typeCode != PFXTypeCode.MATRIXPRICEGRIDITEM && typeCode != PFXTypeCode.MATRIXPRICELISTITEM) {
                return createPath(FETCH.getOperation(), extensionSubtype);
            }
        }
        return null;
    }

    public static String buildFetchPath(IPFXExtensionType extensionType, PFXTypeCode typeCode, String uniqueName, String extensionSubtype) {
        String subtypePath = buildFetchSubtypePath(extensionSubtype);

        if (!StringUtils.isEmpty(subtypePath)) {
            return subtypePath;
        }

        if (typeCode == null) {
            return null;
        }

        switch (typeCode) {
            case CONDITION_RECORD:
                if (extensionType instanceof PFXConditionRecordType) {
                    return createPath(PFXOperation.getFetchOperation(typeCode).getOperation(),
                            extensionType.getTypeCodeSuffix());
                } else {
                    throw new RequestValidationException("Condition record set is not identified");
                }
            case PRICELISTITEM:
            case PRICEGRIDITEM:
            case MANUALPRICELISTITEM:
            case MATRIXPRICEGRIDITEM:
            case MATRIXPRICELISTITEM:
                if (StringUtils.isEmpty(uniqueName)) {
                    throw new RequestValidationException("Price List or Price Grid ID is mandatory");
                }
                return createPath(PFXOperation.getFetchOperation(typeCode).getOperation(), uniqueName);
            case QUOTE:
            case DATALOAD:
                return PFXOperation.getFetchOperation(typeCode).getOperation();
            case DATAFEED:
            case DATAMART:
            case DATASOURCE:
                if (StringUtils.isEmpty(uniqueName)) {
                    return createPath(PFXOperation.getFetchOperation(typeCode).getOperation(), typeCode.getTypeCode());
                } else {
                    return createPath(PA_FETCH.getOperation(), typeCode.getFullTargetName(uniqueName));
                }
            default:
                if (extensionType != null && !StringUtils.isEmpty(extensionType.getTable())) {
                    return createPath(PFXOperation.getFetchOperation(typeCode).getOperation(), extensionType.getTable());
                }

                return createPath(PFXOperation.getFetchOperation(typeCode).getOperation(), typeCode.getTypeCode());
        }

    }

    public static String buildBulkLoadPath(IPFXExtensionType extensionType, PFXTypeCode typeCode, String tableName) {
        if (typeCode == null) {
            throw new UnsupportedOperationException("Data load operation not supported for unknown typeCode");
        }

        if (typeCode == PFXTypeCode.CONDITION_RECORD && extensionType != null) {
            return createPath(BULK_LOAD.getOperation(), typeCode.getTypeCode() + extensionType.getAdditionalKeys());
        } else if (typeCode == PFXTypeCode.LOOKUPTABLE && extensionType != null) {
            return createPath(LOOKUPTABLE_VALUES_BULK_LOAD.getOperation(), ((PFXLookupTableType) extensionType).getLookupValueTypeCode());
        } else if (typeCode == PFXTypeCode.DATASOURCE || typeCode == PFXTypeCode.DATAFEED) {
            return createPath(PA_BULK_LOAD.getOperation(), typeCode.getFullTargetName(tableName));
        } else if (typeCode.isExtension() && extensionType != null) {
            return createPath(BULK_LOAD.getOperation(), extensionType.getTypeCodeSuffix());
        } else {
            return createPath(BULK_LOAD.getOperation(), typeCode.getTypeCode());
        }
    }

    public static String buildDeletePath(IPFXExtensionType extensionType, PFXTypeCode typeCode) {
        if (typeCode == null) {
            throw new UnsupportedOperationException("Delete operation not supported for unknown typeCode");
        }

        if (typeCode.isExtension()) {
            return createPath(DELETE.getOperation(), extensionType.getTypeCodeSuffix(), BATCH.getOperation(), FORCEFILTER.getOperation());
        } else if (typeCode == PFXTypeCode.LOOKUPTABLE) {
            return createPath(LOOKUPTABLE_DELETE.getOperation(), extensionType.getTable(), BATCH.getOperation());
        } else {
            return createPath(DELETE.getOperation(), typeCode.getTypeCode(), BATCH.getOperation(), FORCEFILTER.getOperation());
        }
    }

    public static String buildUpdatePath(PFXTypeCode typeCode, ObjectNode inputNode) {
        if (typeCode == PFXTypeCode.QUOTE &&
                ((inputNode.get(FIELD_INPUTS) != null &&
                        JsonUtil.isArrayNode(inputNode.get(FIELD_INPUTS)) && inputNode.get(FIELD_INPUTS).size() > 0) ||
                        inputNode.get("headerText") != null ||
                        (inputNode.get(FIELD_LINEITEMS) != null &&
                                JsonUtil.isArrayNode(inputNode.get(FIELD_LINEITEMS)) && inputNode.get(FIELD_LINEITEMS).size() > 0))) {
            return SAVE_QUOTE.getOperation();
        }

        if (typeCode == PFXTypeCode.ROLE || typeCode == PFXTypeCode.BUSINESSROLE || typeCode == PFXTypeCode.USERGROUP || typeCode == PFXTypeCode.QUOTE) {
            return createPath(UPDATE.getOperation(), typeCode.getTypeCode());
        }

        throw new UnsupportedOperationException("Update operation not supported for " + typeCode);

    }

    public static String buildUpsertPath(IPFXExtensionType extensionType, PFXTypeCode typeCode) {
        if (typeCode == null) {
            throw new UnsupportedOperationException("Upsert operation not supported for unknown typeCode");
        }

        if (typeCode.isExtension()) {
            return createPath(INTEGRATE.getOperation(), extensionType.getTypeCodeSuffix());
        } else if (typeCode == PFXTypeCode.LOOKUPTABLE) {
            return createPath(LOOKUPTABLE_VALUES_INTEGRATE.getOperation(), extensionType.getTable());
        } else {
            return createPath(INTEGRATE.getOperation(), typeCode.getTypeCode());
        }
    }

    public static String buildCreatePath(PFXTypeCode typeCode) {
        if (typeCode == PFXTypeCode.QUOTE) {
            return SAVE_QUOTE.getOperation();
        }

        throw new UnsupportedOperationException("Create operation not supported for " + typeCode);
    }

}
