package net.pricefx.connector.common.validation


import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.util.PFXConstants
import spock.lang.Specification

class AdvancedConfigUpsertRequestValidatorTest extends Specification {
    def "validate"() {
        given:
        def request = new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test").put(PFXConstants.FIELD_VALUE, "test1")

        when:
        new AdvancedConfigUpsertRequestValidator().validate(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_NAME, "test").put(PFXConstants.FIELD_VALUE, "test1")
        new AdvancedConfigUpsertRequestValidator().validate(request)

        then:
        thrown(RequestValidationException.class)


    }


}