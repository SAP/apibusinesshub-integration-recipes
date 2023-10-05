package net.pricefx.adapter.sap.service

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.CustomOperation
import spock.lang.Specification

class ExecuteJsonRequestServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)


    def "execute"() {
        when:
        def result = new ExecuteJsonRequestService(pfxClient, CustomOperation.EXECUTE_FORMULA, "Test").execute(
                new ObjectNode(JsonNodeFactory.instance).put("name", "test").toString())

        then:
        "test" == result.get("resultObject").get("name").textValue()

        when:
        new ExecuteService(pfxClient, CustomOperation.METADATA, "P-1").execute(null)

        then:
        thrown(UnsupportedOperationException.class)

    }

}
