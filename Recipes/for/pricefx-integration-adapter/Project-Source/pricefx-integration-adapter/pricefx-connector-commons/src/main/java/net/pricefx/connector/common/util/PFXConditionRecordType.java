package net.pricefx.connector.common.util;


import java.util.HashSet;
import java.util.Set;

import static net.pricefx.connector.common.util.PFXConstants.MAX_ATTRIBUTES;

public class PFXConditionRecordType implements IPFXExtensionType {


    private String table;
    private int tableId;

    private final int keys;

    public PFXConditionRecordType(int keys) {
        this.keys = keys;
    }

    @Override
    public String getTypeCodeSuffix() {
        return getTypeCode().getTypeCode() + keys;
    }

    @Override
    public int getAdditionalAttributes() {
        return MAX_ATTRIBUTES;
    }

    @Override
    public int getAdditionalKeys() {
        return keys;
    }

    @Override
    public PFXTypeCode getTypeCode() {
        return PFXTypeCode.CONDITION_RECORD;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public Set<String> getMandatoryFields() {
        return new HashSet<>();
    }

    @Override
    public Set<String> getBusinessKeys() {
        Set<String> businessKeys = new HashSet<>();
        for (int i = 1; i <= keys; i++) {
            businessKeys.add("key" + i);
        }
        return businessKeys;
    }

    @Override
    public PFXConditionRecordType withTable(String table) {
        this.table = table;
        return this;
    }

    public int getTableId() {
        return tableId;
    }

    public PFXConditionRecordType withTableId(int tableId) {
        this.tableId = tableId;
        return this;
    }
}