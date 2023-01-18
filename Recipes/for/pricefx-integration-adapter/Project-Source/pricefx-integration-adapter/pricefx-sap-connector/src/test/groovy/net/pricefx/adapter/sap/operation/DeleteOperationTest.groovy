package net.pricefx.adapter.sap.operation

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockFailedPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

class DeleteOperationTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    def failedClient = new MockFailedPFXOperationClient(builder)

    def "delete"() {
        given:
        def original = new ObjectMapper().readTree(DeleteOperationTest.class.getResourceAsStream("/delete-product-request.json"))
        def input = original.toString()

        when:
        def result = new DeleteOperation(pfxClient, PFXTypeCode.PRODUCT, null, null, false).delete(null, input)

        then:
        "1" == result.textValue()

        when:
        result = new DeleteOperation(pfxClient, PFXTypeCode.PRODUCTIMAGE, "TEST", null, false).delete(null, input)

        then:
        "1" == result.get(0).get(PFXConstants.FIELD_VALUE).textValue()

    }

}
