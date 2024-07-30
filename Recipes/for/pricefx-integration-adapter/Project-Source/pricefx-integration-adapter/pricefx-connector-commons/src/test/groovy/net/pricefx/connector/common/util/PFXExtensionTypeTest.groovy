package net.pricefx.connector.common.util


import spock.lang.Specification

class PFXExtensionTypeTest extends Specification {

    def "getTypeCodeSuffix"() {

        when:
        def type = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(6)

        then:
        "PX6" == type.getTypeCodeSuffix()

        when:
        type = new PFXExtensionType(PFXTypeCode.DATAMART)

        then:
        PFXTypeCode.DATAMART.getTypeCode() == type.getTypeCodeSuffix()

    }

    def "getMandatoryFields"() {
        when:
        def fields = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).getMandatoryFields()

        then:
        [PFXConstants.FIELD_SKU].toSet() == fields

        when:
        fields = new PFXExtensionType(PFXTypeCode.DATAFEED).getMandatoryFields()

        then:
        fields.isEmpty()

        when:
        fields = new PFXExtensionType(null).getMandatoryFields()

        then:
        fields.isEmpty()

    }

}
