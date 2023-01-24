package net.pricefx.connector.common.util

import net.pricefx.connector.common.validation.ConnectorException
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class PFXLookupTableTypeTest extends Specification {
    static matrix = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name())
    static matrix2 = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX2.name())
    static simple = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE.name(), PFXLookupTableType.LookupTableValueType.STRING.name())
    static simpleDate = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE_DATE_KEY.name(), PFXLookupTableType.LookupTableValueType.STRING.name())
    static range = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableValueType.STRING.name())

    def "getUniqueKey"() {
        when:
        PFXLookupTableType.LookupTableType.MATRIX2.getUniqueKey()

        then:
        thrown(UnsupportedOperationException.class)

        when:
        def result = PFXLookupTableType.LookupTableType.MATRIX.getUniqueKey()

        then:
        PFXConstants.FIELD_NAME == result

    }

    def "getLookupTableValueType"() {
        when:
        def result = range.getLookupTableValueType(PFXConstants.FIELD_VALUE)

        then:
        PFXLookupTableType.LookupTableValueType.STRING == result

        when:
        result = matrix.getLookupTableValueType(PFXConstants.FIELD_VALUE)

        then:
        null == result

        when:
        result = simple.getLookupTableValueType(PFXConstants.FIELD_NAME)

        then:
        PFXLookupTableType.LookupTableValueType.STRING == result

        when:
        result = matrix.getLookupTableValueType(PFXConstants.FIELD_NAME)

        then:
        null == result

    }

    def "getLookupValueTypeCode"() {
        when:
        def result = matrix.getLookupTableType().getLookupValueTypeCode()

        then:
        "MLTV" == result

        when:
        result = matrix2.getLookupTableType().getLookupValueTypeCode()

        then:
        "MLTV2" == result

        when:
        result = simple.getLookupTableType().getLookupValueTypeCode()

        then:
        "LTV" == result


    }

    def "getMandatoryFields"(lookupType, result) {
        expect:
        result == lookupType.getLookupTableType().getMandatoryFields()

        where:
        lookupType | result
        matrix2    | ["key1", "key2"].toSet()
        simple     | [PFXConstants.FIELD_NAME, "value"].toSet()
        matrix     | [PFXConstants.FIELD_NAME].toSet()
        range      | ["value", "lowerBound", "upperBound"].toSet()

    }

    def "isSimple"(lookupType, result) {
        expect:
        result == lookupType.getLookupTableType().isSimple()

        where:
        lookupType | result
        matrix2    | false
        simple     | true
        simpleDate | true

    }

    def "isMatrix"(lookupType, result) {
        expect:
        result == lookupType.getLookupTableType().isMatrix()

        where:
        lookupType | result
        matrix2    | true
        simple     | false

    }

    def "valueOf"() {
        when:
        def result = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE_INT_KEY.name(), PFXLookupTableType.LookupTableValueType.STRING.name())

        then:
        PFXLookupTableType.LookupTableType.SIMPLE_INT_KEY == result.getLookupTableType()
        PFXLookupTableType.LookupTableValueType.STRING == result.getLookupTableType().getValueType()
        PFXLookupTableType.LookupTableValueType.INT == result.getLookupTableType().getType()

        when:
        result = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableValueType.DATE.name())

        then:
        PFXLookupTableType.LookupTableType.RANGE == result.getLookupTableType()
        PFXLookupTableType.LookupTableValueType.DATE == result.getLookupTableType().getValueType()
        PFXLookupTableType.LookupTableValueType.STRING == result.getLookupTableType().getType()

        when:
        PFXLookupTableType.valueOf(null, null)

        then:
        thrown(ConnectorException.class)

        when:
        result = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX2.name())

        then:
        PFXLookupTableType.LookupTableType.MATRIX2 == result.getLookupTableType()
        !result.getLookupTableType().getValueType()

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), null)

        then:
        thrown(ConnectorException.class)

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), "X")

        then:
        thrown(ConnectorException.class)

        when:
        result = PFXLookupTableType.valueOf("ABC", "ABC")

        then:
        thrown(ConnectorException.class)
    }

    def "getAdditionalKeys"() {
        when:
        def result = matrix.getAdditionalKeys()

        then:
        !result

        when:
        result = matrix2.getAdditionalKeys()

        then:
        2 == result
    }

    def "getFetchResponseSchema"(lookupType, result) {

        expect:
        result == lookupType.getFetchResponseSchema()

        where:
        lookupType | result
        matrix     | PFXJsonSchema.PP_MATRIX_FETCH_RESPONSE
        matrix2    | PFXJsonSchema.PP_MATRIX_MULTI_FETCH_RESPONSE
        simple     | PFXJsonSchema.PP_SIMPLE_FETCH_RESPONSE

    }

    def "getUpsertRequestSchema"(lookupType, result) {
        expect:
        result == lookupType.getUpsertRequestSchema()

        where:
        lookupType | result
        matrix     | PFXJsonSchema.PP_MATRIX_UPSERT_REQUEST
        matrix2    | PFXJsonSchema.PP_MATRIX_MULTI_UPSERT_REQUEST
        simple     | PFXJsonSchema.PP_SIMPLE_UPSERT_REQUEST

    }

    def "validate"() {
        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableValueType.DATE.name()).getLookupTableType().getValueType().validate("2020/01/01")

        then:
        thrown(RequestValidationException.class)

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableValueType.DATE.name()).getLookupTableType().getValueType().validate("2020-01-01")

        then:
        noExceptionThrown()

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE_INT_KEY.name(), PFXLookupTableType.LookupTableValueType.INT.name()).getLookupTableType().getValueType().validate("x")

        then:
        thrown(RequestValidationException.class)

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE_INT_KEY.name(), PFXLookupTableType.LookupTableValueType.INT.name()).getLookupTableType().getValueType().validate("1")

        then:
        noExceptionThrown()

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE_REAL_KEY.name(), PFXLookupTableType.LookupTableValueType.REAL.name()).getLookupTableType().getValueType().validate("x")

        then:
        thrown(RequestValidationException.class)

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.SIMPLE_REAL_KEY.name(), PFXLookupTableType.LookupTableValueType.REAL.name()).getLookupTableType().getValueType().validate("1")

        then:
        noExceptionThrown()

        when:
        PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableValueType.STRING.name()).getLookupTableType().getValueType().validate("2020/01/01")

        then:
        noExceptionThrown()

    }

    def "getType"(PFXLookupTableType.LookupTableType type, result) {
        expect:
        result == type.getType()

        where:
        type                                               | result
        PFXLookupTableType.LookupTableType.SIMPLE_INT_KEY  | PFXLookupTableType.LookupTableValueType.INT
        PFXLookupTableType.LookupTableType.SIMPLE_DATE_KEY | PFXLookupTableType.LookupTableValueType.DATE
        PFXLookupTableType.LookupTableType.SIMPLE_REAL_KEY | PFXLookupTableType.LookupTableValueType.REAL
        PFXLookupTableType.LookupTableType.SIMPLE          | PFXLookupTableType.LookupTableValueType.STRING
    }
}
