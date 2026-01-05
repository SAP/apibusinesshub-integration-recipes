package net.pricefx.connector.common.util;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class PFXConstants {

    public static final String HEADER = "header";


    public static final String STATUS = "status";

    public static final String FIELD_ID = "id";
    public static final String FIELD_LINEID = "lineId";
    public static final String FIELD_LABEL = "label";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_LASTUPDATEDATE = "lastUpdateDate";
    public static final String FIELD_VALIDAFTER = "validAfter";
    public static final String FIELD_TYPEDID = "typedId";
    public static final String FIELD_UNIQUENAME = "uniqueName";
    public static final String FIELD_USER_LOGINNAME = "loginName";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_PLI_PRICELISTID = "pricelistId";

    public static final String FIELD_CONDITIONRECRODSETID = "conditionRecordSetId";
    public static final String FIELD_PGI_PRICEGRIDID = "priceGridId";
    public static final String FIELD_SKU = "sku";
    public static final String FIELD_STARTROW = "startRow";
    public static final String FIELD_ENDROW = "endRow";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_FIELDNAME = "fieldName";
    public static final String FIELD_VERSION = "version";
    public static final String FIELD_VALUETYPE = "valueType";
    public static final String FIELD_RAWVALUE = "rawValue";
    public static final String FIELD_LINEITEMS = "lineItems";
    public static final String FIELD_CRITERIA = "criteria";
    public static final String FIELD_FOLDERS = "folders";
    public static final String FIELD_GROUPS = "groups";
    public static final String FIELD_BUSINESSROLES = "businessRoles";
    public static final String FIELD_QUOTE = "quote";
    public static final String FIELD_INPUTS = "inputs";
    public static final String FIELD_OUTPUTS = "outputs";
    public static final String FIELD_VALUEOBJECT = "valueObject";
    public static final String FIELD_DATA = "data";
    public static final String FIELD_RESPONSE = "response";
    public static final String FIELD_WORKFLOWSTATUS = "workflowStatus";
    public static final String FIELD_TARGETNAME = "targetName";
    public static final String FIELD_SOURCENAME = "sourceName";

    public static final int MAX_ATTRIBUTES = 30;
    public static final int MAX_EXT_ATTRIBUTES = 50;
    public static final int MAX_PAYOUT_ATTRIBUTES = 100;
    public static final int MAX_CONDITION_ATTRIBUTES = 100;

    public static final String ATTRIBUTE_EXT_PREFIX = "attributeExtension___";

    public static final List<String> SYSTEM_FIELDS =
            ImmutableList.of(FIELD_TYPEDID, FIELD_VERSION, "actionUUID", "createdBy", "lastUpdateBy", "updatedBy", "updateDate", "nodeId", "tableId", "dbTable", "dbKey", "clicId", "dbColumn",
                    "fetchVerb", "integrateVerb", "bulkloadVerb", "keyFields", "viewState", "renderInfo", "fieldNames", "keyFieldNames","translations");
    public static final List<String> LOOKUP_SYSTEM_FIELDS = ImmutableList.of(FIELD_TYPE, FIELD_RAWVALUE, FIELD_VALUETYPE, "lookupTableTypedId");

    private PFXConstants() {
    }

    public enum WorkflowStatus {
        DRAFT, SUBMITTED, WITHDRAWN
    }

    public enum JobStatus {
        PENDING
    }

    public enum EntityStatus {
        ACTIVE
    }

}
