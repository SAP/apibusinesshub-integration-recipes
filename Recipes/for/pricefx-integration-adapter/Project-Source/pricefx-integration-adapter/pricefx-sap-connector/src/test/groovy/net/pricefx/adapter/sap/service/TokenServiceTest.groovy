package net.pricefx.adapter.sap.service

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.JsonUtil
import spock.lang.Specification

import static net.pricefx.adapter.sap.util.Constants.ACCESS_TOKEN

class TokenServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)

    def "get"() {
        when:
        def result = new TokenService(pfxClient).get(new ObjectNode(JsonNodeFactory.instance))

        then:
        "dummy-access-token" == JsonUtil.getValueAsText(result.get(ACCESS_TOKEN))

    }

    def "refresh"() {
        when:
        def result = new TokenService(pfxClient).refresh("dummy-token", "refresh-dummy-token")

        then:
        "dummy-access-token" == JsonUtil.getValueAsText(result.get(ACCESS_TOKEN))

    }

    def "isTokenRetryAllowanceExpired"() {
        when:
        def result = TokenService.isTokenRetryAllowanceExpired(pfxClient)

        then:
        false == result

        when:
        result = TokenService.isTokenRetryAllowanceExpired(null)

        then:
        true == result

    }


}
