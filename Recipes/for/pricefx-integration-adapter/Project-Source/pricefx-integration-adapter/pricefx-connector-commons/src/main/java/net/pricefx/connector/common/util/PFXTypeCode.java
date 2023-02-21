package net.pricefx.connector.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.pricefx.connector.common.validation.QuoteRequestValidator;
import net.pricefx.connector.common.validation.RequestValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Stream;

import static net.pricefx.connector.common.util.PFXConstants.*;

public enum PFXTypeCode {

    //not real type in PFX
    TYPEDID("TYPEDID", new String[]{FIELD_ID}),
    TOKEN("TOKEN", new String[]{"TOKEN"}),
    PRODUCTIMAGE("PRODUCTIMAGE", new String[]{"PRODUCTIMAGE"}),
    DATAFILE("DATAFILE", new String[]{"DATAFILE"}),


    //real PFX type
    ADVANCED_CONFIG("AP", new String[]{FIELD_UNIQUENAME} ),
    CUSTOMER("C", new String[]{PFXConstants.FIELD_CUSTOMER_ID}, "CAM"),
    CUSTOMEREXTENSION("CX", "CUSTOMER EXTENSION", new String[]{PFXConstants.FIELD_CUSTOMER_ID}, "CXAM"),

    FORMULA("F", new String[]{FIELD_UNIQUENAME, FIELD_VALIDAFTER}),

    //default key of lookup tables is name, override it in net.pricefx.connector.common.operation if keys are different.
    LOOKUPTABLE("LT", "PRICE PARAMETER", new String[]{PFXConstants.FIELD_NAME}, "MLTVM"),
    LOOKUPTABLEITEM("LTV", new String[]{PFXConstants.FIELD_NAME}),
    MATRIXLOOKUPTABLEITEM("MLTV", "PRICE PARAMETER ITEM", new String[]{PFXConstants.FIELD_NAME}, "MLTVM"),

    PRICERECORD("PR", "PRICE RECORD", new String[]{"sourceId", FIELD_LINEID}, "PRAM", null, new String[]{FIELD_VALIDAFTER}),

    PRODUCT("P", new String[]{PFXConstants.FIELD_SKU}, "PAM"),
    PRODUCTEXTENSION("PX", "PRODUCT EXTENSION", new String[]{PFXConstants.FIELD_SKU}, "PXAM"),

    QUOTE("Q", "QUOTE", new String[]{FIELD_UNIQUENAME}, "QAM", new QuoteRequestValidator(), null),

    DATALOAD("DMDL", new String[]{PFXConstants.FIELD_TYPEDID}),

    MANUALPRICELIST("MPL", new String[]{FIELD_ID}),
    MANUALPRICELISTITEM("MPLI", "MANUAL PRICELIST ITEM", new String[]{FIELD_PLI_PRICELISTID}, "MPLAM"),

    PRICEGRID("PG", new String[]{FIELD_ID}),
    PRICEGRIDITEM("PGI", "PRICEGRID ITEM", new String[]{FIELD_PGI_PRICEGRIDID}, "PGIM"),
    MATRIXPRICEGRIDITEM("XPGI", new String[]{FIELD_PGI_PRICEGRIDID}),

    PRICELIST("PL", new String[]{FIELD_ID}),
    PRICELISTITEM("PLI", "PRICELIST ITEM", new String[]{FIELD_PLI_PRICELISTID}, "PLIM"),
    MATRIXPRICELISTITEM("XPLI", new String[]{FIELD_PLI_PRICELISTID}),

    REBATEAGREEMENT("RBA", new String[]{FIELD_UNIQUENAME}),
    REBATETYPE("RBT", new String[]{FIELD_UNIQUENAME}),
    REBATERECORD("RR", new String[]{FIELD_UNIQUENAME}),

    USER("U", "USER", new String[]{PFXConstants.FIELD_USER_LOGINNAME}, null, null, new String[]{"email"}),

    USERGROUP("UG", "USER GROUP", new String[]{FIELD_UNIQUENAME}, "UG"),
    ROLE("R", "ROLE", new String[]{FIELD_UNIQUENAME}, "R"),
    BUSINESSROLE("BR", "BUSINESS ROLE", new String[]{FIELD_UNIQUENAME}, "BR"),

    DATAMART("DM", new String[]{FIELD_UNIQUENAME}),
    DATASOURCE("DMDS", new String[]{FIELD_UNIQUENAME}),
    DATAFEED("DMF", new String[]{FIELD_UNIQUENAME});

    private final String typeCode;
    private final String label;
    private final String[] identifierFieldNames;
    private final String metadataTypeCode;
    private final String[] additionalMandatoryFields;
    private RequestValidator validator;

    PFXTypeCode(String value, String[] identifierFieldNames) {
        this.typeCode = value;
        this.identifierFieldNames = identifierFieldNames;
        this.label = this.name();
        this.metadataTypeCode = null;
        this.additionalMandatoryFields = null;
    }

    PFXTypeCode(String value, String[] identifierFieldNames, String metadataTypeCode) {
        this.typeCode = value;
        this.label = this.name();
        this.identifierFieldNames = identifierFieldNames;
        this.metadataTypeCode = metadataTypeCode;
        this.additionalMandatoryFields = null;
    }


    PFXTypeCode(String value, String label, String[] identifierFieldNames, String metadataTypeCode) {
        this.typeCode = value;
        this.label = label;
        this.identifierFieldNames = identifierFieldNames;
        this.metadataTypeCode = metadataTypeCode;
        this.additionalMandatoryFields = null;
    }

    PFXTypeCode(String value, String label, String[] identifierFieldNames, String metadataTypeCode, RequestValidator validator, String[] additionalMandatoryFields) {
        this.typeCode = value;
        this.label = label;
        this.identifierFieldNames = identifierFieldNames;
        this.metadataTypeCode = metadataTypeCode;
        this.validator = validator;
        this.additionalMandatoryFields = additionalMandatoryFields;
    }

    public static PFXTypeCode findByLabel(String label) {
        return Stream.of(PFXTypeCode.values()).filter((PFXTypeCode pfxTypeCode) -> pfxTypeCode.getLabel().equalsIgnoreCase(label)).findFirst().orElse(null);
    }

    public String getLabel() {
        return label;
    }

    public static SortedMap<String, Pair<String, String>> getGenericSupportedTypeCodes() {
        SortedMap<String, Pair<String, String>> map = new TreeMap<>();
        map.put(PRODUCT.name(), Pair.of(PRODUCT.getLabel(), PRODUCT.getLabel()));
        map.put(CUSTOMER.name(), Pair.of(CUSTOMER.getLabel(), CUSTOMER.getLabel()));
        return map;
    }

    public static SortedMap<String, Pair<String, String>> getPriceListSupportedTypeCodes() {
        SortedMap<String, Pair<String, String>> map = new TreeMap<>();
        map.put(PRICELIST.name(), Pair.of(PRICELIST.getLabel(), PRICELIST.getLabel()));
        map.put(PRICEGRID.name(), Pair.of(PRICEGRID.getLabel(), PRICEGRID.getLabel()));
        map.put(MANUALPRICELIST.name(), Pair.of(MANUALPRICELIST.getLabel(), MANUALPRICELIST.getLabel()));
        return map;
    }

    public static SortedMap<String, Pair<String, String>> getPriceListItemSupportedTypeCodes() {
        SortedMap<String, Pair<String, String>> map = new TreeMap<>();
        map.put(PRICEGRIDITEM.name(), Pair.of(PRICEGRIDITEM.getLabel(), PRICEGRIDITEM.getLabel()));
        map.put(PRICELISTITEM.name(), Pair.of(PRICELISTITEM.getLabel(), PRICELISTITEM.getLabel()));
        map.put(MANUALPRICELISTITEM.name(), Pair.of(MANUALPRICELISTITEM.getLabel(), MANUALPRICELISTITEM.getLabel()));
        return map;
    }

    public static boolean isPriceListItemTypeCodes(PFXTypeCode typeCode) {
        return (typeCode == PRICELISTITEM || typeCode == PRICEGRIDITEM || typeCode == MANUALPRICELISTITEM);
    }


    public static Map<String, Pair<String, String>> getPATypeCodes() {
        Map<String, Pair<String, String>> map = new HashMap<>();
        map.put(DATALOAD.name(), Pair.of(DATALOAD.getLabel(), DATALOAD.getLabel()));
        map.put(DATAMART.name(), Pair.of(DATAMART.getLabel(), DATAMART.getLabel()));
        map.put(DATASOURCE.name(), Pair.of(DATASOURCE.getLabel(), DATASOURCE.getLabel()));
        map.put(DATAFEED.name(), Pair.of(DATAFEED.getLabel(), DATAFEED.getLabel()));
        return map;
    }

    public static PFXTypeCode getTypeCode(String objectTypeId) {

        try {
            PFXTypeCode typeCode = validValueOf(objectTypeId);
            if (typeCode == null) {
                typeCode = findByTypeCodeOrName(objectTypeId.split("\\.")[1], null);
            }

            return typeCode;
        } catch (Exception ex) {
            return null;
        }

    }

    public static PFXTypeCode validValueOf(String typeCode) {
        if (EnumUtils.isValidEnum(PFXTypeCode.class, typeCode)) {
            return PFXTypeCode.valueOf(typeCode);
        }
        return null;
    }

    public static PFXTypeCode findByTypeCodeOrName(String typeCode, PFXTypeCode defaultTypeCode) {
        return Stream.of(values()).filter(
                        (PFXTypeCode pfxTypeCode) ->
                                pfxTypeCode.getTypeCode().equalsIgnoreCase(typeCode) || pfxTypeCode.name().equalsIgnoreCase(typeCode) ||
                                        getExtensionBySubtype(typeCode) == pfxTypeCode)
                .findFirst().orElse(defaultTypeCode);
    }

    public String getTypeCode() {
        return typeCode;
    }

    public static PFXTypeCode getExtensionBySubtype(String subtypeCode) {

        if (StringUtils.isEmpty(subtypeCode)) {
            return null;
        }

        if (subtypeCode.startsWith(PRODUCTEXTENSION.getTypeCode())) {
            return PRODUCTEXTENSION;
        }

        if (subtypeCode.startsWith(CUSTOMEREXTENSION.getTypeCode())) {
            return CUSTOMEREXTENSION;
        }

        return null;

    }

    public static PFXTypeCode findByTypeCode(String typeCode) {
        return Stream.of(values()).filter((PFXTypeCode pfxTypeCode) ->
                pfxTypeCode.getTypeCode().equalsIgnoreCase(typeCode)
        ).findFirst().orElse(null);
    }

    public static boolean isDataCollectionTypeCodes(PFXTypeCode typeCode) {
        return (typeCode == DATAFEED || typeCode == DATAMART || typeCode == DATASOURCE);
    }

    public boolean isExtension() {
        return (this == PRODUCTEXTENSION || this == CUSTOMEREXTENSION);
    }

    public String[] getIdentifierFieldNames() {
        return getIdentifierFieldNames(null);
    }

    public String[] getIdentifierFieldNames(String subType) {
        if (this == LOOKUPTABLE && PFXLookupTableType.LookupTableType.RANGE.name().equalsIgnoreCase(subType)) {
            return new String[]{PFXConstants.FIELD_VALUE};
        }
        return identifierFieldNames;
    }

    public String[] getAdditionalMandatoryFields() {
        return additionalMandatoryFields;
    }

    public String getMetadataTypeCode() {
        return metadataTypeCode;
    }

    public boolean shouldGetTableNames() {
        return (ImmutableList.of(CUSTOMEREXTENSION, PRODUCTEXTENSION, LOOKUPTABLE).contains(this));
    }

    public void validate(JsonNode inputNode) {
        if (validator != null) {
            validator.validate(inputNode);
        }
    }

    public boolean isDataCollectionTypeCodes() {
        return this == DATAFEED || this == DATAMART || this == DATASOURCE;
    }


    public Set<String> getMandatoryFields() {

        String[] arr = new String[0];
        arr = ArrayUtils.addAll(arr, this.identifierFieldNames);
        arr = ArrayUtils.addAll(arr, this.additionalMandatoryFields);

        return ImmutableSet.copyOf(arr);
    }

    public String getFullTargetName(String targetName) {
        return getTypeCode() + "." + targetName;

    }

}
