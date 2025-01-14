package net.pricefx.connector.common.util

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class PFXTypeCodeTest extends Specification {

    def "findByTypeCode"() {
        when:
        def result = PFXTypeCode.findByTypeCode("C")

        then:
        PFXTypeCode.CUSTOMER == result

        when:
        result = PFXTypeCode.findByTypeCode("X")

        then:
        null == result

    }

    def "getPriceListSupportedTypeCodes"() {
        when:
        def result = PFXTypeCode.getPriceListSupportedTypeCodes()

        then:
        result.containsKey(PFXTypeCode.PRICELIST.name())
    }

    def "getPriceListItemSupportedTypeCodes"() {
        when:
        def result = PFXTypeCode.getPriceListItemSupportedTypeCodes()

        then:
        result.containsKey(PFXTypeCode.PRICELISTITEM.name())
    }

    def "isPriceListItemTypeCodes"() {
        when:
        def result = PFXTypeCode.isPriceListItemTypeCodes(PFXTypeCode.PRICELISTITEM)

        then:
        true == result

        when:
        result = PFXTypeCode.isPriceListItemTypeCodes(PFXTypeCode.PRICELIST)

        then:
        false == result
    }

    def "findByLabel"() {

        when:
        def typeCode = PFXTypeCode.findByLabel("QUOTE")

        then:
        typeCode == PFXTypeCode.QUOTE

        when:
        typeCode = PFXTypeCode.findByLabel("x")

        then:
        !typeCode
    }

    def "findByTypeCodeOrName"() {
        when:
        def typeCode = PFXTypeCode.findByTypeCodeOrName("Q", null)

        then:
        typeCode == PFXTypeCode.QUOTE

        when:
        typeCode = PFXTypeCode.findByTypeCodeOrName("QUOTE", null)

        then:
        typeCode == PFXTypeCode.QUOTE

        when:
        typeCode = PFXTypeCode.findByTypeCodeOrName("CX3", null)

        then:
        typeCode == PFXTypeCode.CUSTOMEREXTENSION

        when:
        typeCode = PFXTypeCode.findByTypeCodeOrName("PX3", null)

        then:
        typeCode == PFXTypeCode.PRODUCTEXTENSION

        when:
        typeCode = PFXTypeCode.findByTypeCodeOrName("x", null)

        then:
        null == typeCode

        when:
        typeCode = PFXTypeCode.findByTypeCodeOrName("x", PFXTypeCode.PRICERECORD)

        then:
        PFXTypeCode.PRICERECORD == typeCode

    }

    def "getGenericSupportedTypeCodes"() {
        when:
        def result = PFXTypeCode.getGenericSupportedTypeCodes()

        then:
        2 == result.size()
        result.keySet().contains(PFXTypeCode.PRODUCT.name())

    }

    def "getPATypeCodes"() {
        when:
        def result = PFXTypeCode.getPATypeCodes()

        then:
        4 == result.size()
        result.keySet().contains(PFXTypeCode.DATALOAD.name())

    }

    def "validValueOf"() {
        when:
        def result = PFXTypeCode.validValueOf(PFXTypeCode.PRODUCT.name())

        then:
        PFXTypeCode.PRODUCT == result

    }

    def "getIdentifierFieldNames"() {
        when:
        def result = PFXTypeCode.LOOKUPTABLE.getIdentifierFieldNames(null)

        then:
        PFXConstants.FIELD_NAME == result[0]

        when:
        result = PFXTypeCode.LOOKUPTABLE.getIdentifierFieldNames(PFXLookupTableType.LookupTableType.RANGE.name())

        then:
        PFXConstants.FIELD_VALUE == result[0]

    }

    def "isDataCollectionTypeCodes"(typeCode, result) {

        expect:
        result == PFXTypeCode.isDataCollectionTypeCodes(typeCode)

        where:
        typeCode               | result
        PFXTypeCode.DATAFEED   | true
        PFXTypeCode.DATAMART   | true
        PFXTypeCode.DATASOURCE | true
        PFXTypeCode.PRODUCT    | false
    }

    def "validate"() {
        when:
        PFXTypeCode.QUOTE.validate(new ObjectNode(JsonNodeFactory.instance)
                .put("expiryDate", "2021-01-01")
                .put("targetDate", "2020-01-01"))

        then:
        noExceptionThrown()

        when:
        PFXTypeCode.QUOTE.validate(new ObjectNode(JsonNodeFactory.instance)
                .put("expiryDate", "2020-01-01")
                .put("targetDate", "2021-01-01"))

        then:
        thrown(RequestValidationException.class)

        when:
        PFXTypeCode.QUOTE.validate(new ObjectNode(JsonNodeFactory.instance))

        then:
        thrown(RequestValidationException.class)

        when:
        PFXTypeCode.PRODUCT.validate(new ObjectNode(JsonNodeFactory.instance))

        then:
        noExceptionThrown()

    }

    def "getExtensionBySubtype"(subtypeCode, result) {
        expect:
        result == PFXTypeCode.getExtensionBySubtype(subtypeCode)

        where:
        subtypeCode | result
        null        | null
        ""          | null
        "PX6"       | PFXTypeCode.PRODUCTEXTENSION
        "P"         | null
        "CX6"       | PFXTypeCode.CUSTOMEREXTENSION
    }

    def "getTypeCode"(objectTypeId, result) {
        expect:
        result == PFXTypeCode.getTypeCode(objectTypeId)

        where:
        objectTypeId | result
        "DATAFEED"   | PFXTypeCode.DATAFEED
        ""           | null
        "1.DMF"      | PFXTypeCode.DATAFEED
    }

    def "getMandatoryFields"() {
        when:
        def result = PFXTypeCode.PRICERECORD.getMandatoryFields()

        then:
        3 == result.size()
        result.containsAll(PFXTypeCode.PRICERECORD.identifierFieldNames)
        result.containsAll(PFXTypeCode.PRICERECORD.additionalMandatoryFields)
    }


}
