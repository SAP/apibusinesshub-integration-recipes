package net.pricefx.adapter.sap.service

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class FetchServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    def dummyClient = new MockDummyPFXOperationClient(builder)

    def "get"() {
        when:
        def result = new FetchService(pfxClient, PFXTypeCode.PRODUCT, null, null).
                get(PFXConstants.FIELD_SKU, "PRODUCT", true, true, 0, 500)

        then:
        "PRODUCT" == result.get(PFXConstants.FIELD_SKU).textValue()
        null == result.get(PFXConstants.FIELD_VERSION)

        when:
        result = new FetchService(pfxClient, PFXTypeCode.PRODUCT, null, null).
                get(PFXConstants.FIELD_SKU, "PRODUCT", true, false, 0, 500)

        then:
        "PRODUCT" == result.get(PFXConstants.FIELD_SKU).textValue()
        "0" == result.get(PFXConstants.FIELD_VERSION).textValue()

        when:
        result = new FetchService(pfxClient, PFXTypeCode.QUOTE, null, null).
                get(PFXConstants.FIELD_UNIQUENAME, "P-1", true, true, 0, 500)

        then:
        "P-1" == result.get(PFXConstants.FIELD_UNIQUENAME).textValue()

        when:
        result = new FetchService(pfxClient, PFXTypeCode.TYPEDID, null, null).
                get(PFXConstants.FIELD_SKU, "1.P", true, true, 0, 500)

        then:
        "1" == result.get(PFXConstants.FIELD_SKU).textValue()

        when:
        result = new FetchService(pfxClient, PFXTypeCode.TYPEDID, null, null).
                get(PFXConstants.FIELD_UNIQUENAME, "1.DMF", true, true, 0, 500)

        then:
        "1" == result.get(PFXConstants.FIELD_UNIQUENAME).textValue()


    }

    def "fetchMetadata"() {
        when:
        def result = new FetchService(pfxClient, PFXTypeCode.PRODUCT, null, null).fetchMetadata(0L, 100)

        then:
        "attribute1" == result.getAt(0).get(PFXConstants.FIELD_FIELDNAME).textValue()

    }

    def "fetch"() {
        given:
        def request = new ObjectMapper().readTree(FetchServiceTest.class.getResourceAsStream("/fetch-product-request.json"))

        when:
        def result = new FetchService(pfxClient, PFXTypeCode.PRODUCT, null, null).fetch(0L, 100, true, true, request.toString())

        then:
        "PRODUCT" == result.getAt(0).get(PFXConstants.FIELD_SKU).textValue()

        when:
        result = new FetchService(dummyClient, PFXTypeCode.PRODUCT, null, null).fetch(0L, 100, true, true, request.toString())

        then:
        result.isObject()
        result.isEmpty()

        when:
        new FetchService(pfxClient, PFXTypeCode.PRODUCT, null, null).fetch(0L, 100, true, false, "xxx")

        then:
        thrown(RequestValidationException.class)

        when:
        new FetchService(pfxClient, PFXTypeCode.PRODUCT, null, null).fetch(0L, 100, true, false, "[]")

        then:
        thrown(RequestValidationException.class)


        when:
        new FetchService(pfxClient, PFXTypeCode.PRODUCT, null, null).fetch(0L, 100, true, false, null)

        then:
        thrown(RequestValidationException.class)


    }

}
