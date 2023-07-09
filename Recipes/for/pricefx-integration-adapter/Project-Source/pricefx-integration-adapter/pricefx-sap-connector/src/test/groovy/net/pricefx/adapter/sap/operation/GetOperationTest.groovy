package net.pricefx.adapter.sap.operation

import net.pricefx.connector.common.connection.MockFailedPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.*
import spock.lang.Specification

class GetOperationTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    def failedClient = new MockFailedPFXOperationClient(builder)

    def "get"() {
        when:
        def result = new GetOperation(pfxClient, PFXTypeCode.PRODUCT, "TEST", null, (IPFXExtensionType) null).get(null, 0, Constants.MAX_RECORDS, true)

        then:
        "TEST" == result.get(PFXConstants.FIELD_SKU).textValue()
        null == result.get(PFXConstants.FIELD_VERSION)


        when:
        result = new GetOperation(pfxClient, PFXTypeCode.PRODUCT, "TEST", null, (IPFXExtensionType) null).get(null, 0, Constants.MAX_RECORDS, false)

        then:
        "TEST" == result.get(PFXConstants.FIELD_SKU).textValue()
        "0" == result.get(PFXConstants.FIELD_VERSION).textValue()

        when:
        result = new GetOperation(pfxClient, PFXTypeCode.QUOTE, "TEST", null, (IPFXExtensionType) null).get(null, 0, Constants.MAX_RECORDS, true)

        then:
        "TEST" == result.get(PFXConstants.FIELD_UNIQUENAME).textValue()


        when:
        new GetOperation(pfxClient, PFXTypeCode.PRODUCTIMAGE, "TEST", null, (IPFXExtensionType) null).get(null, 0, Constants.MAX_RECORDS, true)

        then:
        thrown(UnsupportedOperationException.class)

        when:
        def matrix = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX2.name())
        new GetOperation(pfxClient, PFXTypeCode.LOOKUPTABLE, "TEST", null, matrix).get(null, 0, Constants.MAX_RECORDS, true)

        then:
        thrown(UnsupportedOperationException.class)

        when:
        matrix = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name())
        matrix.withTable("MatrixTable")
        result = new GetOperation(pfxClient, PFXTypeCode.LOOKUPTABLE, "TEST", null, matrix).get(null, 0, Constants.MAX_RECORDS, true)

        then:
        "TEST" == result.get(PFXConstants.FIELD_NAME).textValue()

    }

}
