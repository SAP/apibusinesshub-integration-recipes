package net.pricefx.connector.common.validation

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.util.JsonUtil
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXConstants.FIELD_USER_LOGINNAME

class UserUpsertRequestValidatorTest extends Specification {

    def "validate"() {
        given:
        def request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc2").put("email", "abc2@pricefx.com"),
                new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc1").put("email", "abc1@pricefx.com"))

        when:
        new UserUpsertRequestValidator().validate(request)

        then:
        noExceptionThrown()

        when:
        request.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_USER_LOGINNAME, "abc1").put("email", "abc1@pricefx.com"))
        new UserUpsertRequestValidator().validate(request)

        then:
        thrown(RequestValidationException.class)
    }


}