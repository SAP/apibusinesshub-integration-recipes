package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.validation.ConnectorException
import spock.lang.Specification

class QuoteUpdaterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/update-quote-request.json"
    def fullRequestFile = "/update-fullquote-request.json"
    def lineItemRequestFile = "/update-lineitem-request.json"
    def lineItemOriginalFile = "/update-lineitem-original.json"

    def "update"() {
        given:
        def request = new ObjectMapper().readTree(QuoteUpdaterTest.class.getResourceAsStream(requestFile))

        when:
        def result = new QuoteUpdater(pfxClient, null).upsert(request, true, false, false, false, false, false)

        then:
        result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue() == "P-10000"
        result.get(0).get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_NAME).textValue() == "Qty"
        result.get(0).get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).numberValue() == 1000


    }

    def "upsert"() {
        given:
        def request = new ObjectMapper().readTree(QuoteUpdaterTest.class.getResourceAsStream(fullRequestFile))

        when:
        def result = new QuoteUpdater(pfxClient, null).upsert(request, true, false, false, false, false, false)

        then:
        result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue() == "P-24049"
        result.get(0).get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_NAME).textValue() == "Qty"
        result.get(0).get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).numberValue() == 1000

    }

    def "interpolateQuote"() {
        given:
        def request = new ObjectMapper().readTree(QuoteUpdaterTest.class.getResourceAsStream(lineItemRequestFile))
        def quote = new ObjectMapper().readTree(QuoteUpdaterTest.class.getResourceAsStream(lineItemOriginalFile))


        when:
        QuoteUpdater.interpolateQuote(quote, request)

        then:
        quote.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).numberValue() == 40
        quote.get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).numberValue() == 30


        when:
        QuoteUpdater.interpolateQuote(null, null)

        then:
        thrown(ConnectorException.class)


    }


}
