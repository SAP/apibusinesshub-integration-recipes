package net.pricefx.connector.common.util;

import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_CUSTOMER_ID;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_SKU;


public class PFXExtensionType implements IPFXExtensionType {

    private final PFXTypeCode typeCode;
    private final Set<String> businessKeys = new HashSet<>();
    private int attributes;
    private String table;

    public PFXExtensionType(PFXTypeCode typeCode) {
        this.typeCode = typeCode;
    }

    public PFXExtensionType withAttributes(Number attributes) {
        this.attributes = (attributes != null) ? attributes.intValue() : 0;

        return this;
    }

    @Override
    public int getAdditionalAttributes() {
        return attributes;
    }

    @Override
    public int getAdditionalKeys() {
        return 0;
    }

    @Override
    public PFXTypeCode getTypeCode() {
        return typeCode;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public Set<String> getMandatoryFields() {

        if (typeCode != null && typeCode.isExtension()) {
            return ImmutableSet.of(typeCode == PFXTypeCode.CUSTOMEREXTENSION ? FIELD_CUSTOMER_ID : FIELD_SKU);
        }

        return Collections.emptySet();
    }

    public Set<String> getBusinessKeys() {
        return businessKeys;
    }

    public IPFXExtensionType withTable(String table) {
        this.table = table;
        return this;
    }

    public PFXExtensionType withBusinessKeys(List<String> businessKeys) {
        this.businessKeys.addAll(businessKeys);
        return this;
    }

    @Override
    public String getTypeCodeSuffix() {
        switch (typeCode) {
            case PRODUCTEXTENSION:
            case CUSTOMEREXTENSION:
                if (attributes == 0){
                    return typeCode.getTypeCode();
                } else {
                    return typeCode.getTypeCode() + attributes;
                }
            default:
                return typeCode.getTypeCode();

        }
    }
}
