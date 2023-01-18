package net.pricefx.connector.common.util

import spock.lang.Specification

class PFXOperationTest extends Specification {

    def "findPFXOperation"() {

        when:
        def operation = PFXOperation.findPFXOperation("lookuptablemanager.fetch/abc")

        then:
        operation == PFXOperation.LOOKUPTABLE_FETCH

        when:
        operation = PFXOperation.findPFXOperation("abc")

        then:
        !operation
    }

    def "getFetchOperation"(typeCode, result) {
        expect:
        result == PFXOperation.getFetchOperation(typeCode)

        where:
        typeCode                     | result
        null                         | PFXOperation.FETCH
        PFXTypeCode.PRODUCTEXTENSION | PFXOperation.PRODUCTEXTENSION_FETCH
        PFXTypeCode.PRODUCT          | PFXOperation.FETCH
        PFXTypeCode.DATAFEED         | PFXOperation.GET_FCS
    }

    def "isFetchOperation"() {
        when:
        def result = PFXOperation.FETCH.isFetchOperation()

        then:
        result

        when:
        result = PFXOperation.GET_FCS.isFetchOperation()

        then:
        !result


    }
}
