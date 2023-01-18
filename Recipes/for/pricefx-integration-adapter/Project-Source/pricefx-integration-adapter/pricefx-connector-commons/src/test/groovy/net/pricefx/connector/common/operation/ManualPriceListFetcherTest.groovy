package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.Constants
import net.pricefx.connector.common.util.PFXConstants
import spock.lang.Specification

class ManualPriceListFetcherTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def "fetch"() {
        given:
        JsonNode request = new ObjectMapper().readTree(ManualPriceListFetcherTest.class.getResourceAsStream("/fetch-mpl-request.json"))

        when:
        def result = new ManualPriceListFetcher(pfxClient, false).
                fetch(request, 0L, Constants.MAX_RECORDS, true, true)


        then:
        "test" == result.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue()
        1 == result.get(0).get(PFXConstants.FIELD_ID).numberValue()
        null == result.get(0).get(PFXConstants.FIELD_TYPEDID)


    }

}
