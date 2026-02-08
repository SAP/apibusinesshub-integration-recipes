package net.pricefx.connector.common.util


import spock.lang.Specification

class PFXConditionRecordTypeTest extends Specification {

    def "getBusinessKeys"() {
        when:
        def keys = new PFXConditionRecordType(3, false, true).getBusinessKeys()

        then:
        5 == keys.size()
        "key1" == keys.getAt(0)
        "key2" == keys.getAt(1)
        "key3" == keys.getAt(2)

    }


    def "getTypeCodeSuffix"() {

        when:
        def name = new PFXConditionRecordType(5, false, true).getTypeCodeSuffix()

        then:
        PFXTypeCode.CONDITION_RECORD.getTypeCode() + "5" == name

    }

    def "getTable"() {

        when:
        def type = new PFXConditionRecordType(5, false, true).withTable("1")

        then:
        "1" == type.getTable()


    }

    def "getTypeCode"() {

        when:
        def type = new PFXConditionRecordType(5, false, true).withTable("1").getTypeCode()

        then:
        PFXTypeCode.CONDITION_RECORD == type

        when:
        type = new PFXConditionRecordType(5, true, true).withTable("1").getTypeCode()

        then:
        PFXTypeCode.CONDITION_RECORD_ALL == type

        when:
        type = new PFXConditionRecordType(5, true, false).withTable("1").getTypeCode()

        then:
        PFXTypeCode.CONDITION_RECORD_HISTORY == type

        when:
        type = new PFXConditionRecordType(5, false, false).withTable("1").getTypeCode()

        then:
        PFXTypeCode.CONDITION_RECORD == type


    }

}
