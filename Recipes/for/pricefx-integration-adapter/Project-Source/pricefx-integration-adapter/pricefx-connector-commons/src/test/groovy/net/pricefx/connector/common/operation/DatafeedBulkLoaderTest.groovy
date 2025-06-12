package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class DatafeedBulkLoaderTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/bulkload-datafeed-request.json"


    def "bulk load"() {
        given:
        def request = new ObjectMapper().readTree(DatafeedBulkLoaderTest.class.getResourceAsStream(requestFile))

        when:
        def result = new DatafeedBulkLoader(pfxClient,  "TESTING").bulkLoad(request, false)

        then:
        "0" == result.textValue()
    }
    def "validateStructure"() {


        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("attribute1").add("attribute2"))

        new DatafeedBulkLoader(pfxClient, "TESTING").validateStructure(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("attribute1").add("attribute2").add("attribute3"))

        new DatafeedBulkLoader(pfxClient,  "TESTING").validateStructure(request)

        then:
        thrown(RequestValidationException.class)



    }
}
