package net.pricefx.adapter.sap.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

class UpsertServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    def dummyClient = new MockDummyPFXOperationClient(builder)

    def "upsert"() {
        given:
        def request = new ObjectMapper().readTree(UpsertServiceTest.class.getResourceAsStream("/upsert-product-request.json"))
        def original = request.deepCopy()

        when:
        def result = new UpsertService(pfxClient, PFXTypeCode.PRODUCT, null, false, false, false).execute(null, request.toString())

        then:
        original.size() == result.size()

        when:
        result = new UpsertService(pfxClient, PFXTypeCode.PRODUCT, null, true, false, false).execute(null, request.toString())

        then:
        original.size() + "" == result.textValue()

        when:
        ((ArrayNode) request).remove(1)
        result = new UpsertService(pfxClient, PFXTypeCode.PRODUCT, null, false, false, false).execute(null, request.toString())

        then:
        result.isObject()

        when:
        result = new UpsertService(dummyClient, PFXTypeCode.PRODUCT, null, false, false, false).execute(null, request.toString())

        then:
        result.isObject()
        result.isEmpty()


    }

}
