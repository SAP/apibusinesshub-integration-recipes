package net.pricefx.connector.common.validation

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.util.PFXConstants
import spock.lang.Specification

class ActionUpsertRequestValidatorTest extends Specification {
    def "validate"() {
        given:
        def request = new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test").put("attribute1", "test1"))

        when:
        new ActionUpsertRequestValidator().validate(request)

        then:
        noExceptionThrown()

        when:
        request =new ArrayNode(JsonNodeFactory.instance).add( new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_NAME, "test").put("attribute1", "test1"))
        new ActionUpsertRequestValidator().validate(request)

        then:
        thrown(RequestValidationException.class)


    }


}