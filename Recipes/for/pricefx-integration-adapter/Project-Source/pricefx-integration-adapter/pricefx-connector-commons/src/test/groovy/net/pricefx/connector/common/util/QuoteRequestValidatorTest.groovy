package net.pricefx.connector.common.util

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.validation.QuoteRequestValidator
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class QuoteRequestValidatorTest extends Specification {

    def "validate"() {
        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance)
                .put("expiryDate", "2021-02-01")
                .put("targetDate", "2021-01-01"))

        then:
        noExceptionThrown()

        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance)
                .put("expiryDate", "2021-02-01")
                .put("targetDate", "2021-07-01"))

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance)
                .put("expiryDate", "x")
                .put("targetDate", "x"))

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteRequestValidator().validate(new ObjectNode(null))

        then:
        thrown(RequestValidationException.class)


    }


}
