package net.pricefx.connector.common.util;


import com.google.common.collect.ImmutableSet;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashSet;
import java.util.Set;

import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXTypeCode.LOOKUPTABLEITEM;
import static net.pricefx.connector.common.util.PFXTypeCode.MATRIXLOOKUPTABLEITEM;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.INVALID_DATE_FORMAT;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.INVALID_NUMBER_FORMAT;

public class PFXLookupTableType implements IPFXExtensionType {
    public static final String LOWERBOUND = "lowerBound";
    public static final String UPPERBOUND = "upperBound";

    private final LookupTableType lookupTableType;
    private String table; // table ID

    private PFXLookupTableType(LookupTableType lookupTableType, String valueType) {
        this.lookupTableType = lookupTableType;
        if (lookupTableType != null) {
            lookupTableType.setValueType(valueType);
        }
    }

    public static PFXLookupTableType valueOf(String type, String valueType) {
        if (EnumUtils.isValidEnum(LookupTableType.class, type)) {
            PFXLookupTableType lookupTableType = new PFXLookupTableType(LookupTableType.valueOf(type), valueType);
            if (!lookupTableType.getLookupTableType().isMatrix()) {
                return lookupTableType;
            }

            if (EnumUtils.isValidEnum(LookupTableType.class, valueType)) {
                return new PFXLookupTableType(LookupTableType.valueOf(valueType), null);
            }
        }

        throw new ConnectorException("Cannot instantiate PFX lookup table");
    }

    public LookupTableType getLookupTableType() {
        return lookupTableType;
    }

    @Override
    public int getAdditionalAttributes() {
        return MAX_ATTRIBUTES;
    }

    @Override
    public int getAdditionalKeys() {
        return lookupTableType.getAdditionalKeys();
    }

    @Override
    public PFXTypeCode getTypeCode() {
        return PFXTypeCode.LOOKUPTABLE;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public Set<String> getMandatoryFields() {
        return lookupTableType.getMandatoryFields();
    }

    @Override
    public Set<String> getBusinessKeys() {
        return getMandatoryFields();
    }

    @Override
    public IPFXExtensionType withTable(String table) {
        this.table = table;
        return this;
    }

    public PFXJsonSchema getFetchResponseSchema() {
        return getLookupTableType().getFetchResponseSchema();
    }

    public PFXJsonSchema getUpsertRequestSchema() {
        return getLookupTableType().getUpsertRequestSchema();
    }

    public String getLookupValueTypeCode() {
        return getLookupTableType().getLookupValueTypeCode();
    }

    public LookupTableValueType getLookupTableValueType(String fieldName) {

        if (FIELD_VALUE.equalsIgnoreCase(fieldName) &&
                (lookupTableType.isSimple() ||
                        lookupTableType == PFXLookupTableType.LookupTableType.RANGE)) {
            return lookupTableType.getValueType();
        } else if (FIELD_NAME.equalsIgnoreCase(fieldName) && lookupTableType.isSimple()) {
            return lookupTableType.getType();
        }

        return null;
    }

    public enum LookupTableValueType {
        DATE, REAL, INT, STRING;

        public static LookupTableValueType validValueOf(String type) {
            if (EnumUtils.isValidEnum(LookupTableValueType.class, type)) {
                return LookupTableValueType.valueOf(type);
            }
            return null;
        }

        public void validate(String value) {
            switch (this) {
                case REAL:
                    if (!NumberUtils.isCreatable(value)) {
                        throw new RequestValidationException(INVALID_NUMBER_FORMAT);
                    }
                    break;
                case INT:
                    if (!NumberUtils.isDigits(value)) {
                        throw new RequestValidationException(INVALID_NUMBER_FORMAT);
                    }
                    break;
                case DATE:
                    if (DateUtil.getDate(value) == null) {
                        throw new RequestValidationException(INVALID_DATE_FORMAT, "should be YYYY-MM-DD");
                    }
                    break;
                default:
                    break;
            }
        }

    }

    public enum LookupTableType {
        SIMPLE, SIMPLE_REAL_KEY, SIMPLE_INT_KEY, SIMPLE_DATE_KEY, RANGE, MATRIX, MATRIX2, MATRIX3, MATRIX4, MATRIX5, MATRIX6;

        LookupTableValueType valueType;

        public LookupTableValueType getValueType() {
            return valueType;
        }

        private void setValueType(String valueType) {
            this.valueType = LookupTableValueType.validValueOf(valueType);
        }

        public boolean isSimple() {
            switch (this) {
                case SIMPLE:
                case SIMPLE_DATE_KEY:
                case SIMPLE_INT_KEY:
                case SIMPLE_REAL_KEY:
                    return true;
                default:
                    return false;
            }
        }

        public Set<String> getMandatoryFields() {
            switch (this) {
                case RANGE:
                    return ImmutableSet.of(FIELD_VALUE, LOWERBOUND, UPPERBOUND);
                case MATRIX:
                    return ImmutableSet.of(FIELD_NAME);
                case MATRIX2:
                case MATRIX3:
                case MATRIX4:
                case MATRIX5:
                case MATRIX6:
                    Set<String> mandatory = new HashSet<>();
                    for (int i = 1; i <= getAdditionalKeys(); i++) {
                        mandatory.add("key" + i);
                    }
                    return mandatory;
                default:
                    return ImmutableSet.of(FIELD_NAME, FIELD_VALUE);
            }
        }

        public int getAdditionalKeys() {
            if (this != LookupTableType.MATRIX && isMatrix()) {
                return Integer.parseInt(name().replace(MATRIX.name(), StringUtils.EMPTY));
            }

            return 0;
        }

        public boolean isMatrix() {
            switch (this) {
                case MATRIX:
                case MATRIX2:
                case MATRIX3:
                case MATRIX4:
                case MATRIX5:
                case MATRIX6:
                    return true;
                default:
                    return false;
            }
        }

        public String getUniqueKey() {
            switch (this) {
                case RANGE:
                    return FIELD_VALUE;
                case MATRIX2:
                case MATRIX3:
                case MATRIX4:
                case MATRIX5:
                case MATRIX6:
                    throw new UnsupportedOperationException("Unique Key is not supported");
                default:
                    return FIELD_NAME;
            }
        }

        public LookupTableValueType getType() {
            switch (this) {
                case SIMPLE_INT_KEY:
                    return LookupTableValueType.INT;
                case SIMPLE_DATE_KEY:
                    return LookupTableValueType.DATE;
                case SIMPLE_REAL_KEY:
                    return LookupTableValueType.REAL;
                default:
                    return LookupTableValueType.STRING;
            }
        }

        public String getLookupValueTypeCode() {
            switch (this) {
                case MATRIX:
                    return MATRIXLOOKUPTABLEITEM.getTypeCode();
                case MATRIX2:
                case MATRIX3:
                case MATRIX4:
                case MATRIX5:
                case MATRIX6:
                    return name().replace(MATRIX.name(), MATRIXLOOKUPTABLEITEM.getTypeCode());
                default:
                    return LOOKUPTABLEITEM.getTypeCode();

            }
        }

        private PFXJsonSchema getUpsertRequestSchema() {
            switch (this) {
                case RANGE:
                    return PFXJsonSchema.PP_RANGE_UPSERT_REQUEST;
                case MATRIX:
                    return PFXJsonSchema.PP_MATRIX_UPSERT_REQUEST;
                case MATRIX2:
                case MATRIX3:
                case MATRIX4:
                case MATRIX5:
                case MATRIX6:
                    return PFXJsonSchema.PP_MATRIX_MULTI_UPSERT_REQUEST;
                default:
                    return PFXJsonSchema.PP_SIMPLE_UPSERT_REQUEST;

            }
        }

        private PFXJsonSchema getFetchResponseSchema() {
            switch (this) {
                case RANGE:
                    return PFXJsonSchema.PP_RANGE_FETCH_RESPONSE;
                case MATRIX:
                    return PFXJsonSchema.PP_MATRIX_FETCH_RESPONSE;
                case MATRIX2:
                case MATRIX3:
                case MATRIX4:
                case MATRIX5:
                case MATRIX6:
                    return PFXJsonSchema.PP_MATRIX_MULTI_FETCH_RESPONSE;
                default:
                    return PFXJsonSchema.PP_SIMPLE_FETCH_RESPONSE;

            }
        }
    }
}