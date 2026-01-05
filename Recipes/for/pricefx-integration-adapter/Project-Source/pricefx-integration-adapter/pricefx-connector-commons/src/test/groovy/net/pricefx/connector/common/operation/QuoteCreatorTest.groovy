package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class QuoteCreatorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/create-quote-request.json"
    def requestFileTooManyItems = "/create-quote-request-toomanyitems.json"
    def requestFileMissingField = "/create-quote-request-missing-fields.json"
    def requestFileInvalidDate = "/create-quote-request-date-invalid.json"

    def "validate"() {
        given:
        def request = new ObjectMapper().readTree(QuoteCreatorTest.class.getResourceAsStream(requestFileMissingField))

        when:
        new QuoteCreator(pfxClient, null).validate(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectMapper().readTree(QuoteCreatorTest.class.getResourceAsStream(requestFileInvalidDate))
        new QuoteCreator(pfxClient, null).validate(request)

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteCreator(pfxClient, null).validate(null)

        then:
        thrown(RequestValidationException.class)

        when:
        new QuoteCreator(pfxClient, null).validate(new ArrayNode(JsonNodeFactory.instance))

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectMapper().readTree(QuoteCreatorTest.class.getResourceAsStream(requestFile))
        new QuoteCreator(pfxClient, null).validate(request)

        then:
        noExceptionThrown()
    }


    def "create"() {
        given:
        def request = new ObjectMapper().readTree(QuoteCreatorTest.class.getResourceAsStream(requestFile))

        when:
        def result = new QuoteCreator(pfxClient, null).upsert(request, true, false, false, false, false, false)

        then:
        result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue() == "P-1000"

        when:
        request = new ObjectMapper().readTree(QuoteCreatorTest.class.getResourceAsStream(requestFileTooManyItems))
        new QuoteCreator(pfxClient, null).upsert(request, true, false, false, false, false, false)

        then:
        thrown(RequestValidationException.class)


    }

}
