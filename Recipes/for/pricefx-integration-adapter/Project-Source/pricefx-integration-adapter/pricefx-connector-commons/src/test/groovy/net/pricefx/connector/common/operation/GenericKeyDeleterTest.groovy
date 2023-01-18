package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockFailedPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class GenericKeyDeleterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def pfxFailedClient = new MockFailedPFXOperationClient()
    def requestFile = "/delete-key-request.json"

    def "delete"() {
        given:
        def request = new ObjectMapper().readTree(GenericKeyDeleterTest.class.getResourceAsStream(requestFile))

        when:
        def result = new GenericKeyDeleter(pfxClient, PFXTypeCode.PRODUCT, null).delete(request)

        then:
        "1" == result

        when:
        result = new GenericKeyDeleter(pfxFailedClient, PFXTypeCode.PRODUCT, null).delete(request)

        then:
        "0" == result

    }

    def "validate - missing key"() {
        given:
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance)
        node.put(PFXConstants.FIELD_SKU, "")

        when:
        new GenericKeyDeleter(pfxClient, PFXTypeCode.PRODUCT, null).delete(node)

        then:
        thrown(RequestValidationException.class)

    }

    def "validate - extra fields"() {
        given:
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance)
        node.put(PFXConstants.FIELD_SKU, "abc")
        node.put("x", "abc")

        when:
        new GenericKeyDeleter(pfxClient, PFXTypeCode.PRODUCT, null).delete(node)

        then:
        thrown(RequestValidationException.class)

    }

}
