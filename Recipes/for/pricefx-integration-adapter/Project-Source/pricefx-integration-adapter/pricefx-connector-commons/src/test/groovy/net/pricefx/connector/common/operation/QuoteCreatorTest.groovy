package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class QuoteCreatorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/create-quote-request.json"
    def requestFileTooManyItems = "/create-quote-request-toomanyitems.json"

    def "create - too many items"() {
        given:
        def request = new ObjectMapper().readTree(QuoteCreatorTest.class.getResourceAsStream(requestFileTooManyItems))

        when:
        new QuoteCreator(pfxClient, null).upsert(request, true, false, false, false, false)

        then:
        thrown(RequestValidationException.class)
    }


    def "create"() {
        given:
        def request = new ObjectMapper().readTree(QuoteCreatorTest.class.getResourceAsStream(requestFile))

        when:
        def result = new QuoteCreator(pfxClient, null).upsert(request, true, false, false, false, false)

        then:
        "P-1000" == result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue()

    }

}
