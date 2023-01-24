package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockFailedPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.validation.ConnectorException
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class FormulaExecuterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/execute-formula-request.json"
    def pfxFailedClient = new MockFailedPFXOperationClient()

    def "execute"() {
        given:
        def request = new ObjectMapper().readTree(FormulaExecuterTest.class.getResourceAsStream(requestFile))

        when:
        def result = new FormulaExecuter(pfxClient, "test").execute(request)

        then:
        2 == result.size()
        "Price" == result.get(0).get("resultName").textValue()
        "30" == result.get(0).get("result").asText()

        when:
        new FormulaExecuter(pfxFailedClient, "test").execute(request)

        then:
        thrown(ConnectorException.class)

        when:
        new FormulaExecuter(pfxClient, null).execute(request)

        then:
        thrown(ConnectorException.class)

        when:
        new FormulaExecuter(pfxClient, "TEST").execute(new ArrayNode(JsonNodeFactory.instance))

        then:
        thrown(RequestValidationException.class)

        when:
        new FormulaExecuter(pfxClient, "DUMMY").execute(new ObjectNode(JsonNodeFactory.instance).put("dummy", "dummy"))

        then:
        thrown(NoSuchElementException.class)

    }


}
