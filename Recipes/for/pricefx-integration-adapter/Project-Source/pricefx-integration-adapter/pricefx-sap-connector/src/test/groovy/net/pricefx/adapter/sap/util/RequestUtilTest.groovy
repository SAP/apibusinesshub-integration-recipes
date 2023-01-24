package net.pricefx.adapter.sap.util


import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class RequestUtilTest extends Specification {

    def "convertRequestToJson"() {
        given:
        def objectNode = new ObjectNode(JsonNodeFactory.instance).put("test", "test")

        when:
        RequestUtil.convertRequestToJson(null)

        then:
        thrown(RequestValidationException.class)

        when:
        RequestUtil.convertRequestToJson(objectNode)

        then:
        thrown(RequestValidationException.class)

        when:
        RequestUtil.convertRequestToJson("xxxx")

        then:
        thrown(RequestValidationException.class)

        when:
        def result = RequestUtil.convertRequestToJson(objectNode.toString())

        then:
        objectNode == result

        when:
        result = RequestUtil.convertRequestToJson(objectNode.toString().bytes)

        then:
        objectNode == result

        when:
        result = RequestUtil.convertRequestToJson(RequestUtilTest.class.getResourceAsStream("/upsert-product-request.json"))

        then:
        2 == result.size()
        result.isArray()

    }

}
