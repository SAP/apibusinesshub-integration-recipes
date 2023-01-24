package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.CustomOperation
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXOperation
import net.pricefx.connector.common.validation.ConnectorException
import spock.lang.Specification

import static net.pricefx.connector.common.util.ConnectionUtil.createPath
import static org.junit.Assert.assertNull

class QuoteExecuterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "submit"() {
        when:
        def result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_SUBMIT).execute(null)

        then:
        "true" == result.get(PFXConstants.FIELD_VALUE).textValue()
    }

    def "withdraw"() {
        when:
        def result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_WITHDRAW).execute(null)

        then:
        "true" == result.get(PFXConstants.FIELD_VALUE).textValue()

    }

    def "copy"() {
        when:
        def result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_COPY).execute(null)

        then:
        "P-1000" == result.get(PFXConstants.FIELD_VALUE).textValue()

    }

    def "revise"() {
        when:
        def result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_REVISION).execute(null)

        then:
        "P-1000" == result.get(PFXConstants.FIELD_VALUE).textValue()

    }

    def "validateRequest"() {
        when:
        new QuoteExecuter(pfxClient, [:], CustomOperation.QUOTE_REVISION).validateRequest(null)

        then:
        thrown(ConnectorException.class)

    }

    def "buildPath"() {
        when:
        def result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_OPERATION).buildPath(null)

        then:
        assertNull(result)

        when:
        result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_REVISION).buildPath(null)

        then:
        createPath(PFXOperation.REVISE_QUOTE.getOperation(), "P-1000") == result

    }

    def "isSuccess"() {
        when:
        def result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_REVISION).isSuccess(new ObjectNode(JsonNodeFactory.instance))

        then:
        !result

        when:
        def workflowStatus = new ObjectNode(JsonNodeFactory.instance).put("workflowStatus", "WITHDRAWN")
        def node = new ObjectNode(JsonNodeFactory.instance).put("workflowStatus", "SUBMITTED").set("workflow", workflowStatus)
        result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_WITHDRAW).isSuccess(node)

        then:
        result

    }

    def "getSuccessResponse"() {
        when:
        def result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_WITHDRAW).getSuccessResponse()

        then:
        "true" == result

        when:
        def node = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME, "P-1")
        result = new QuoteExecuter(pfxClient, ["UNIQUE_KEY": "P-1000"], CustomOperation.QUOTE_REVISION).getSuccessResponse(node)

        then:
        "P-1" == result


    }

}
