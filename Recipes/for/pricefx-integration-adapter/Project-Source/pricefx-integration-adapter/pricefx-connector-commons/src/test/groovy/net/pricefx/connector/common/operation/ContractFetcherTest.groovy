package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.Constants
import net.pricefx.connector.common.util.PFXConstants
import spock.lang.Specification

class ContractFetcherTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/fetch-contract-request.json"

    def "fetch"() {
        given:
        def request = new ObjectMapper().readTree(ContractFetcherTest.class.getResourceAsStream(requestFile))

        when:
        def result = new ContractFetcher(pfxClient, false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)


        then:
        "C-1000" == result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue()

        when:
        result = new ContractFetcher(pfxClient, false).withFullResult(true).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)


        then:
        "C-1000" == result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue()
        "Qty" == result.get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_NAME).textValue()


    }

}
