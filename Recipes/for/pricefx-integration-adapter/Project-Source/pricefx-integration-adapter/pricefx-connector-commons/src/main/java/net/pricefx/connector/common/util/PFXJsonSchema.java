package net.pricefx.connector.common.util;

import org.apache.commons.lang3.StringUtils;

public enum PFXJsonSchema {
    //formula
    EXECUTE_FORMULA_RESPONSE("/schemas/execute-formula-output-schema.json"),

    //generic
    SIMPLE_FETCH_REQUEST("/schemas/fetch-operation-simple-input-schema.json", "/schemas/validation/fetch-operation-simple-input-schema-validation.json"),
    FETCH_REQUEST("/schemas/fetch-operation-input-schema.json", "/schemas/validation/fetch-operation-input-schema-validation.json"),
    FETCH_RESPONSE("/schemas/fetch-operation-output-schema.json"),
    POST_REQUEST("/schemas/post-operation-input-schema.json"),
    POST_RESPONSE("/schemas/post-operation-output-schema.json"),
    UPSERT_REQUEST("/schemas/upsert-operation-input-schema.json"),
    UPDATE_REQUEST("/schemas/update-operation-input-schema.json"),
    BULK_LOAD_REQUEST("/schemas/bulkload-operation-input-schema.json", "/schemas/validation/bulkload-operation-input-schema-validation.json"),
    DELETE_REQUEST("/schemas/generic-filter-input-schema.json", "/schemas/validation/generic-filter-input-schema-validation.json"),
    OPERATION_RESPONSE("/schemas/generic-operation-output-schema.json"),
    FILTER_REQUEST("/schemas/generic-filter-input-schema.json", "/schemas/validation/generic-filter-input-schema-validation.json"),
    ACTION_RESPONSE("/schemas/action-operation-output-schema.json"),


    //price record
    PRICERECORD_UPSERT_REQUEST("/schemas/upsert-pricerecord-input-schema.json"),
    PRICERECORD_FETCH_RESPONSE("/schemas/fetch-pricerecord-output-schema.json"),

    //user
    USER_UPSERT_REQUEST("/schemas/upsert-user-input-schema.json"),
    USER_FETCH_RESPONSE("/schemas/fetch-user-output-schema.json"),
    USER_ACCESS_REQUEST("/schemas/update-user-access-input-schema.json"),

    //product
    PRODUCT_UPSERT_REQUEST("/schemas/upsert-product-input-schema.json"),
    PRODUCT_FETCH_RESPONSE("/schemas/fetch-product-output-schema.json"),

    //customer
    CUSTOMER_UPSERT_REQUEST("/schemas/upsert-customer-input-schema.json"),
    CUSTOMER_FETCH_RESPONSE("/schemas/fetch-customer-output-schema.json"),

    //Price List
    PRICELIST_FETCH_RESPONSE("/schemas/fetch-pricelist-output-schema.json"),
    PRICELISTITEM_FETCH_RESPONSE("/schemas/fetch-pricelistitem-output-schema.json"),

    //Price List
    MANUALPRICELIST_FETCH_RESPONSE("/schemas/fetch-manualpricelist-output-schema.json"),
    MANUALPRICELISTITEM_FETCH_RESPONSE("/schemas/fetch-manualpricelistitem-output-schema.json"),

    //Price Grid
    PRICEGRID_FETCH_RESPONSE("/schemas/fetch-pricegrid-output-schema.json"),
    PRICEGRIDITEM_FETCH_RESPONSE("/schemas/fetch-pricegriditem-output-schema.json"),

    //PX
    PX_UPSERT_REQUEST("/schemas/upsert-PX-input-schema.json"),
    PX_FETCH_RESPONSE("/schemas/fetch-PX-output-schema.json"),

    //CX
    CX_UPSERT_REQUEST("/schemas/upsert-CX-input-schema.json"),
    CX_FETCH_RESPONSE("/schemas/fetch-CX-output-schema.json"),

    //PP - SIMPLE
    PP_SIMPLE_UPSERT_REQUEST("/schemas/upsert-PP-simple-input-schema.json", "/schemas/validation/upsert-PP-simple-input-schema-validation.json"),
    PP_SIMPLE_FETCH_RESPONSE("/schemas/fetch-PP-simple-output-schema.json"),

    //PP - RANGE
    PP_RANGE_UPSERT_REQUEST("/schemas/upsert-PP-range-input-schema.json", "/schemas/validation/upsert-PP-range-input-schema-validation.json"),
    PP_RANGE_FETCH_RESPONSE("/schemas/fetch-PP-range-output-schema.json"),

    //PP - MATRIX
    PP_MATRIX_UPSERT_REQUEST("/schemas/upsert-PP-matrix-input-schema.json", "/schemas/validation/upsert-PP-matrix-input-schema-validation.json"),
    PP_MATRIX_FETCH_RESPONSE("/schemas/fetch-PP-matrix-output-schema.json"),
    PP_MATRIX_MULTI_UPSERT_REQUEST("/schemas/upsert-PP-matrix-multi-input-schema.json"),
    PP_MATRIX_MULTI_FETCH_RESPONSE("/schemas/fetch-PP-matrix-multi-output-schema.json"),

    //QUOTE
    QUOTE_CREATE_REQUEST("/schemas/create-quote-input-schema.json", "/schemas/validation/create-quote-input-schema-validation.json"),
    QUOTE_FETCH_RESPONSE("/schemas/fetch-quote-output-schema.json"),
    QUOTE_UPSERT_REQUEST("/schemas/upsert-quote-input-schema.json", "/schemas/validation/upsert-quote-input-schema-validation.json"),
    QUOTE_UPDATE_REQUEST("/schemas/update-quote-input-schema.json", "/schemas/validation/update-quote-input-schema-validation.json"),

    //CONDITION RECORD
    CONDITION_RECORD_FETCH_RESPONSE("/schemas/fetch-conditionRecord-output-schema.json"),
    CONDITION_RECORD_UPDATE_REQUEST("/schemas/update-conditionRecord-input-schema.json", "/schemas/validation/update-conditionRecord-input-schema-validation.json"),

    //PAYOUT
    PAYOUT_FETCH_RESPONSE("/schemas/fetch-payout-output-schema.json"),

    //REBATE AGREEMENT
    RBA_FETCH_RESPONSE("/schemas/fetch-agreement-output-schema.json"),

    //CONTRACT
    CONTRACT_FETCH_RESPONSE("/schemas/fetch-contract-output-schema.json"),

    //PA
    DATALOAD_FETCH_RESPONSE("/schemas/fetch-dataload-output-schema.json"),
    DATAFEED_FETCH_RESPONSE("/schemas/fetch-datafeed-output-schema.json"),
    DATAMART_FETCH_RESPONSE("/schemas/fetch-datamart-output-schema.json"),
    DATASOURCE_FETCH_RESPONSE("/schemas/fetch-datasource-output-schema.json"),
    DATAUPLOAD_STATUS_RESPONSE("/schemas/upload-data-status-output-schema.json"),

    //Token
    TOKEN_RESPONSE("/schemas/token-output-schema.json"),

    //METADATA
    METADATA_RESPONSE("/schemas/fetch-metadata-output-schema.json");

    private final String path;

    //to allow multiple types for a field, which is not allowed in boomi json schema. only required for complicated upsert or create request
    private final String validationPath;

    PFXJsonSchema(String path) {
        this.path = path;
        this.validationPath = null;
    }

    PFXJsonSchema(String path, String validationPath) {
        this.path = path;
        this.validationPath = validationPath;
    }

    public static PFXJsonSchema getUpsertRequestSchema(PFXTypeCode pfxTypeCode, IPFXExtensionType extensionType) {
        if (pfxTypeCode != null) {
            switch (pfxTypeCode) {
                case PRICERECORD:
                    return PRICERECORD_UPSERT_REQUEST;
                case QUOTE:
                    return QUOTE_CREATE_REQUEST;
                case PRODUCT:
                    return PRODUCT_UPSERT_REQUEST;
                case CUSTOMER:
                    return CUSTOMER_UPSERT_REQUEST;
                case PRODUCTEXTENSION:
                    return PX_UPSERT_REQUEST;
                case CUSTOMEREXTENSION:
                    return CX_UPSERT_REQUEST;
                case USER:
                    return USER_UPSERT_REQUEST;
                case LOOKUPTABLE:
                    if (extensionType instanceof PFXLookupTableType) {
                        PFXLookupTableType.LookupTableType lookupTableType = ((PFXLookupTableType) extensionType).getLookupTableType();
                        switch (lookupTableType) {
                            case SIMPLE:
                            case SIMPLE_REAL_KEY:
                            case SIMPLE_INT_KEY:
                            case SIMPLE_DATE_KEY:
                                return PP_SIMPLE_UPSERT_REQUEST;
                            case RANGE:
                                return PP_RANGE_UPSERT_REQUEST;
                            case MATRIX:
                                return PP_MATRIX_UPSERT_REQUEST;
                            case MATRIX2:
                            case MATRIX3:
                            case MATRIX4:
                            case MATRIX5:
                            case MATRIX6:
                                return PP_MATRIX_MULTI_UPSERT_REQUEST;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return POST_REQUEST;
    }

    public String getPath() {
        return path;
    }

    public static PFXJsonSchema getFetchRequestSchema(PFXTypeCode pfxTypeCode) {
        if (pfxTypeCode != null) {
            switch (pfxTypeCode) {
                case DATALOAD:
                case DATAFEED:
                case DATASOURCE:
                case DATAMART:
                    return SIMPLE_FETCH_REQUEST;
                default:
                    break;
            }
        }
        return FETCH_REQUEST;
    }

    public static PFXJsonSchema getFetchResponseSchema(PFXTypeCode pfxTypeCode, IPFXExtensionType extensionType) {
        if (pfxTypeCode != null) {
            switch (pfxTypeCode) {
                case CONTRACT:
                    return CONTRACT_FETCH_RESPONSE;
                case REBATEAGREEMENT:
                    return RBA_FETCH_RESPONSE;
                case CONDITION_RECORD:
                    return CONDITION_RECORD_FETCH_RESPONSE;
                case PAYOUT:
                    return PAYOUT_FETCH_RESPONSE;
                case PRICERECORD:
                    return PRICERECORD_FETCH_RESPONSE;
                case MANUALPRICELIST:
                    return MANUALPRICELIST_FETCH_RESPONSE;
                case MANUALPRICELISTITEM:
                    return MANUALPRICELISTITEM_FETCH_RESPONSE;
                case PRICELIST:
                    return PRICELIST_FETCH_RESPONSE;
                case PRICELISTITEM:
                    return PRICELISTITEM_FETCH_RESPONSE;
                case PRICEGRID:
                    return PRICEGRID_FETCH_RESPONSE;
                case PRICEGRIDITEM:
                    return PRICEGRIDITEM_FETCH_RESPONSE;
                case PRODUCT:
                    return PRODUCT_FETCH_RESPONSE;
                case CUSTOMER:
                    return CUSTOMER_FETCH_RESPONSE;
                case PRODUCTEXTENSION:
                    return PX_FETCH_RESPONSE;
                case CUSTOMEREXTENSION:
                    return CX_FETCH_RESPONSE;
                case DATALOAD:
                    return DATALOAD_FETCH_RESPONSE;
                case QUOTE:
                    return QUOTE_FETCH_RESPONSE;
                case USER:
                    return USER_FETCH_RESPONSE;
                case LOOKUPTABLE:
                    if (extensionType instanceof PFXLookupTableType) {
                        PFXLookupTableType.LookupTableType lookupTableType = ((PFXLookupTableType) extensionType).getLookupTableType();
                        switch (lookupTableType) {
                            case SIMPLE:
                            case SIMPLE_REAL_KEY:
                            case SIMPLE_INT_KEY:
                            case SIMPLE_DATE_KEY:
                                return PP_SIMPLE_FETCH_RESPONSE;
                            case RANGE:
                                return PP_RANGE_FETCH_RESPONSE;
                            case MATRIX:
                                return PP_MATRIX_FETCH_RESPONSE;
                            case MATRIX2:
                            case MATRIX3:
                            case MATRIX4:
                            case MATRIX5:
                            case MATRIX6:
                                return PP_MATRIX_MULTI_FETCH_RESPONSE;
                        }
                    }
                    break;
                default:
                    return FETCH_RESPONSE;
            }
        }
        return null;
    }

    public String getValidationPath() {
        if (StringUtils.isEmpty(validationPath)) {
            return path;
        }

        return validationPath;

    }

}