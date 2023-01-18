package net.pricefx.connector.common.util;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Stream;

public enum PFXOperation {
    //test connection
    PING,

    TOKEN,
    REFRESH_TOKEN("token/refresh"),

    //formula
    EXECUTE_FORMULA("formulamanager.executeformula"),
    GET_FORMULA_NAMES("formulamanager.getnames"),
    FETCH_FORMULA_ELEMENTS("formulamanager.fetch"),

    //user roles
    ASSIGN_ROLE("accountmanager.assignrole"),
    ASSIGN_BUSINESS_ROLE("accountmanager.assignbusinessrole"),
    ASSIGN_GROUP("accountmanager.assigngroup"),

    //price list
    PRICE_LIST_ITEM_FETCH("pricelistmanager.fetch"),

    //price grid
    PRICE_GRID_ITEM_FETCH("pricegridmanager.fetch"),

    //manual price list
    MANUAL_PRICELIST_ITEM_FETCH("manualpricelistmanager.fetch"),

    //file import
    DATA_FILE_IMPORT("datamart.importfile"),
    FILE_UPLOAD_SLOT("uploadmanager.newuploadslot"),
    FILE_UPLOAD_STATUS("uploadmanager.progress"),
    FILE_UPLOAD_SLOT_DELETE("uploadmanager.deleteslot"),

    //Product Image
    PRODUCT_IMAGE_UPLOAD("productimages.upload"),
    PRODUCT_IMAGE_EXIST("productimages.containsImage"),
    PRODUCT_IMAGE_DELETE("productimages.deleteImage"),

    //Price Analyzer
    GET_FCS("datamart.getfcs"),
    REFRESH_DATAMART("datamart.refresh"),
    RUN_DATALOAD("datamart.rundataload"),
    PA_BULK_LOAD("datamart.loaddata"),
    PA_FETCH("datamart.fetch"),
    GET_DATALOADS("datamart.getdataloads"),

    //Quote
    SUBMIT_QUOTE("quotemanager.submit"),
    FETCH_QUOTE("quotemanager.fetch"),
    FETCH_QUOTES("quotemanager.fetchlist"),
    FETCH_WORKFLOW_DETAILS("workflowsmanager.fetchdetailsviaapprovable"),
    WITHDRAW_WORKFLOW("workflowsmanager.withdraw"),
    SAVE_QUOTE("quotemanager.save"),
    ADD_LINEITEM_QUOTE("quotemanager.addproducts"),
    REVISE_QUOTE("quotemanager.createnewrevision"),
    COPY_QUOTE("quotemanager.copy"),

    //PP table
    LOOKUPTABLE_VALUES_INTEGRATE("lookuptablemanager.integrate"), //Upsert
    LOOKUPTABLE_FETCH("lookuptablemanager.fetch"),
    LOOKUPTABLE_DELETE("lookuptablemanager.delete"),
    LOOKUPTABLE_VALUES_BULK_LOAD("lookuptablemanager.loaddata"),

    //Generic Master Data
    FETCH,
    DELETE,
    ADD,
    UPDATE,
    INTEGRATE,  //Upsert
    BULK_LOAD("loaddata"),

    //Options
    FORCEFILTER,
    BATCH,

    //Extensions
    PRODUCTEXTENSION_LIST("configurationmanager.get/productextension/versioned"),
    PRODUCTEXTENSION_FETCH("productmanager.fetch/*/PX"),
    CUSTOMEREXTENSION_LIST("configurationmanager.get/customerextension/versioned"),
    CUSTOMEREXTENSION_FETCH("customermanager.fetch/*/CX");

    private static final List<PFXOperation> FETCH_DATA_OPERATIONS = ImmutableList.of(
            FETCH_QUOTES, LOOKUPTABLE_FETCH, FETCH, PRODUCTEXTENSION_FETCH, CUSTOMEREXTENSION_FETCH
    );

    private final String operation;

    PFXOperation() {
        this.operation = this.name().toLowerCase();
    }

    PFXOperation(String operation) {
        this.operation = operation;
    }

    public static PFXOperation findPFXOperation(String path) {
        return Stream.of(PFXOperation.values()).filter((PFXOperation v) -> path.startsWith(v.getOperation())).findFirst().orElse(null);
    }

    public String getOperation() {
        return operation;
    }

    public static PFXOperation getFetchOperation(PFXTypeCode typeCode) {
        if (typeCode == null) return FETCH;

        switch (typeCode) {
            case PRICEGRIDITEM:
            case MATRIXPRICEGRIDITEM:
                return PRICE_GRID_ITEM_FETCH;
            case PRICELISTITEM:
            case MATRIXPRICELISTITEM:
                return PRICE_LIST_ITEM_FETCH;
            case DATAFEED:
            case DATAMART:
            case DATASOURCE:
                return GET_FCS;
            case DATALOAD:
                return GET_DATALOADS;
            case MANUALPRICELISTITEM:
                return MANUAL_PRICELIST_ITEM_FETCH;
            case PRODUCTEXTENSION:
                return PRODUCTEXTENSION_FETCH;
            case CUSTOMEREXTENSION:
                return CUSTOMEREXTENSION_FETCH;
            case LOOKUPTABLE:
                return LOOKUPTABLE_FETCH;
            case QUOTE:
                return FETCH_QUOTES;
            default:
                return FETCH;
        }
    }

    public boolean isFetchOperation() {
        return FETCH_DATA_OPERATIONS.contains(this);
    }

}