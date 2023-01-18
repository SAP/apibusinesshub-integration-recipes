package net.pricefx.connector.common.operation

import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

import static net.pricefx.connector.common.util.Constants.MAX_METADATA_RECORDS

class GenericMetadataFetcherTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def pfxDummyClient = new MockDummyPFXOperationClient()

    def "fetch"() {
        when:
        def result = new GenericMetadataFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null).fetch(0L, MAX_METADATA_RECORDS, null)


        then:
        "attribute1" == result.get(0).get(PFXConstants.FIELD_FIELDNAME).textValue()

        when:
        result = new GenericMetadataFetcher(pfxClient,
                PFXTypeCode.ROLE, null).fetch(0L, MAX_METADATA_RECORDS, null)


        then:
        "ADMIN" == result.get(0).get(PFXConstants.FIELD_FIELDNAME).textValue()

        when:
        result = new GenericMetadataFetcher(pfxDummyClient,
                PFXTypeCode.ROLE, null).fetch(0L, MAX_METADATA_RECORDS, null)


        then:
        result.isEmpty()

    }

}
