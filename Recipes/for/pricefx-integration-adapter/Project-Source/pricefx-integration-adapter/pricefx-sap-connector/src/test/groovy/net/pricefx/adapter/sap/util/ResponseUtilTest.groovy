package net.pricefx.adapter.sap.util

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.util.JsonUtil
import spock.lang.Specification

class ResponseUtilTest extends Specification {

    def "formatResponse"() {
        given:
        def node = new ObjectNode(JsonNodeFactory.instance).put("test", "test")
        def arr = JsonUtil.createArrayNode(node, node)

        when:
        def result = ResponseUtil.formatResponse(node)

        then:
        node == result

        when:
        result = ResponseUtil.formatResponse(arr)

        then:
        arr == result

        when:
        result = ResponseUtil.formatResponse(JsonUtil.createArrayNode(node))

        then:
        node == result

        when:
        result = ResponseUtil.formatResponse(null)

        then:
        result.isObject()


    }

}
