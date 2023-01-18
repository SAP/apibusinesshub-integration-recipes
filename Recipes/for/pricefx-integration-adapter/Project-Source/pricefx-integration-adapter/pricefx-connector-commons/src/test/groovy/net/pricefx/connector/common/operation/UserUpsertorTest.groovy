package net.pricefx.connector.common.operation


import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXConstants.FIELD_USER_LOGINNAME

class UserUpsertorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "validateRequest"() {
        given:
        def request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc2").put("email", "abc2@pricefx.com"),
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc1").put("email", "abc1@pricefx.com"))

        when:
        new UserUpsertor(pfxClient).validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc1").put("email", "abc1@pricefx.com"))

        new UserUpsertor(pfxClient).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)
    }


    def "upsert"() {
        given:
        def request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc2").put("email", "abc2@pricefx.com"),
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc1").put("email", "abc1@pricefx.com"))

        when:
        def results = new UserUpsertor(pfxClient).upsert(request, true, false, false, false)

        then:
        2 == results.size()


    }

}
