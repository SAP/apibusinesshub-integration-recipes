package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.Constants
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class RebateAgreementFetcherTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/fetch-agreement-request.json"

    def "fetch"() {
        given:
        def request = new ObjectMapper().readTree(RebateAgreementFetcherTest.class.getResourceAsStream(requestFile))

        when:
        def result = new RebateAgreementFetcher(pfxClient, false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)


        then:
        "R-1000" == result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue()

        when:
        result = new RebateAgreementFetcher(pfxClient, false).withFullResult(true).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)


        then:
        "R-1000" == result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue()
        "Qty" == result.get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_NAME).textValue()

        when:
        request.remove("sortBy")
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)

        then:
        thrown(RequestValidationException.class)

    }

}
