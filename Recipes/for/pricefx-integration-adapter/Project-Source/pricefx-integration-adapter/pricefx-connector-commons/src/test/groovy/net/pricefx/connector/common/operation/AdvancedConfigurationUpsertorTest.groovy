package net.pricefx.connector.common.operation


import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class AdvancedConfigurationUpsertorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()


    def "validateRequest"() {
        given:
        def request = new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test-config").put(PFXConstants.FIELD_VALUE, "test1")


        when:
        new AdvancedConfigurationUpsertor(pfxClient).validateRequest(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_VALUE, "test1")
        new AdvancedConfigurationUpsertor(pfxClient).validateRequest(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ArrayNode(JsonNodeFactory.instance).add(request)
        new AdvancedConfigurationUpsertor(pfxClient).validateRequest(request)

        then:
        thrown(RequestValidationException.class)
    }
}