package net.pricefx.connector.common.util;

import org.apache.commons.lang3.EnumUtils;

public enum CustomOperation {
    GENERIC_POST("Generic Post"), GENERIC_UPSERT("Generic upsert"), GENERIC_DELETE("Generic delete"), GENERIC_QUERY("Generic query"),
    GENERIC_DELETE_BY_KEY("Generic delete by key"),
    GENERIC_GET("Generic get"), GENERIC_FETCH("Fetch"), GENERIC_BULKLOAD("Bulk Data Load"), METADATA("Metadata"),
    PRODUCT_IMAGE_EXIST("Product Image Exists"), PRODUCT_IMAGE_DELETE("Product Image Delete"), PRODUCT_IMAGE_UPLOAD("Product Image Upload"),
    UPDATE_USER_GROUPS("Update User Groups"), UPDATE_ROLES("Update Roles"), UPDATE_BUSINESS_ROLES("Update Business Roles"),
    PA_DATA_COPY("Copy Data"), PA_DATA_FLUSH("Flush Data"), DATAMART_REFRESH("Refresh Datamart"),
    PA_DATA_TRUNCATE("Truncate Data"), PA_DATA_UPLOAD("Upload Data"),
    PA_CALCULATION("Run Dataload Calculation"),
    QUOTE_SUBMIT("Submit Quote"), QUOTE_WITHDRAW("Withdraw quote"), TYPEDID_GET("Get by TypedId"),
    QUOTE_COPY("Create duplicate"), QUOTE_REVISION("Create Revision"), CONVERT_DEAL("Convert to deal"),

    USER_ACCESS_OPERATION("User Access Operation"),
    EXECUTE_FORMULA("Execute Formula"),
    GET_TOKEN("Get Token"), LOGOUT("Log out"),
    UPLOAD_STATUS_CHECK("Upload Status Check"),
    QUOTE_OPERATION("Quote Operation"), PRODUCT_IMAGE_OPERATION("Product Image Operation"), ADMIN_OPERATION("Admin Operation");

    private final String operation;

    CustomOperation(String operation) {
        this.operation = operation;
    }

    public static CustomOperation validValueOf(String operation) {
        if (EnumUtils.isValidEnum(CustomOperation.class, operation)) {
            return CustomOperation.valueOf(operation);
        }
        return null;
    }

    public String getOperation() {
        return operation;
    }

    public boolean isQuoteOperation() {
        return (this == QUOTE_COPY || this == CONVERT_DEAL || this == QUOTE_SUBMIT || this == QUOTE_WITHDRAW || this == QUOTE_REVISION);
    }

}