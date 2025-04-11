package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.OperatorId
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.RequestUtil
import net.pricefx.connector.common.validation.ConnectorException
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXTypeCode.DATAMART

class DataloadRunnerTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def pfxDummyClient = new MockDummyPFXOperationClient()

    def "validateRequest"() {
        given:
        def request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_NAME).put("operator", OperatorId.NOT_NULL.getValue())))


        when:
        new DataloadRunner(pfxClient, DATAMART, DataloadRunner.DataloadType.TRUNCATE, ["UNIQUE_KEY": "Product"]).validateRequest(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put("operator", OperatorId.NOT_NULL.getValue())))


        new DataloadRunner(pfxClient, DATAMART, DataloadRunner.DataloadType.TRUNCATE, ["UNIQUE_KEY": "Product"]).validateRequest(request)

        then:
        thrown(RequestValidationException.class)

        when:
        new DataloadRunner(pfxClient, DATAMART, DataloadRunner.DataloadType.TRUNCATE, ["UNIQUE_KEY": "Product"]).validateRequest(null)

        then:
        noExceptionThrown()

        when:
        new DataloadRunner(pfxClient, DATAMART, DataloadRunner.DataloadType.TRUNCATE, [:]).validateRequest(null)

        then:
        thrown(RequestValidationException.class)


    }


    def "buildRequest"() {
        when:
        def result = new DataloadRunner(pfxClient, DATAMART, DataloadRunner.DataloadType.TRUNCATE, ["UNIQUE_KEY": "Product"]).buildRequest(null)

        then:
        "1.DMDL" == result.get(PFXConstants.FIELD_TYPEDID).textValue()

        when:
        result = new DataloadRunner(pfxClient, DATAMART, DataloadRunner.DataloadType.TRUNCATE, ["UNIQUE_KEY": "Product"]).buildRequest(new ObjectNode(JsonNodeFactory.instance).put("operator", "and"))

        then:
        "and" == result.get("dtoFilter").get("operator").textValue()

        when:
        new DataloadRunner(pfxDummyClient, DATAMART, DataloadRunner.DataloadType.TRUNCATE, ["UNIQUE_KEY": "Product"]).buildRequest(null)

        then:
        thrown(ConnectorException.class)

    }


}
