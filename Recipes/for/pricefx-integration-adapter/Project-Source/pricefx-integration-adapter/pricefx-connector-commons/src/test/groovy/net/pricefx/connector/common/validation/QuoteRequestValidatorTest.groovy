package net.pricefx.connector.common.validation


import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import spock.lang.Specification

class QuoteRequestValidatorTest extends Specification {

    def "validate"() {
        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance).put("expiryDate", "2020-01-01").put("targetDate", "2019-12-31"))

        then:
        noExceptionThrown()

        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance).put("expiryDate", "2020-01-01").put("targetDate", "2020-12-31"))

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance).put("expiryDate", "2020-01-01"))

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance).put("expiryDate", "2020-01-01").put("targetDate", "xxxx"))

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteRequestValidator().validate(new ObjectNode(JsonNodeFactory.instance).put("expiryDate", "2020-01-01").put("targetDate", 1))

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteRequestValidator().validate(null)

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteRequestValidator().validate(new TextNode("x"))

        then:
        thrown(RequestValidationException.class)

    }

}
