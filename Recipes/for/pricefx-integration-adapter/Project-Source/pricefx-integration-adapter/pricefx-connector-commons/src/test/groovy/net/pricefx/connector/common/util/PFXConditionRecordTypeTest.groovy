package net.pricefx.connector.common.util


import spock.lang.Specification

class PFXConditionRecordTypeTest extends Specification {

    def "getBusinessKeys"() {
        when:
        def keys = new PFXConditionRecordType(3).getBusinessKeys()

        then:
        3 == keys.size()
        "key1" == keys.getAt(0)
        "key2" == keys.getAt(1)
        "key3" == keys.getAt(2)

    }


    def "getTypeCodeSuffix"() {

        when:
        def name = new PFXConditionRecordType(5).getTypeCodeSuffix()

        then:
        PFXTypeCode.CONDITION_RECORD.getTypeCode() + "5" == name

    }

    def "getTable"() {

        when:
        def type = new PFXConditionRecordType(5).withTable("1")

        then:
        "1" == type.getTable()


    }

}
