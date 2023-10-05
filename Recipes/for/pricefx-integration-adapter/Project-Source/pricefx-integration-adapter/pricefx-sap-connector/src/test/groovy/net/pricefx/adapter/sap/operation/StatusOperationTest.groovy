package net.pricefx.adapter.sap.operation

import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

class StatusOperationTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    def dummyClient = new MockDummyPFXOperationClient(builder)

    def "get"() {
        when:
        def result = new StatusOperation(pfxClient, PFXTypeCode.PRODUCTIMAGE, "TEST").get()

        then:
        "true" == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        result = new StatusOperation(dummyClient, PFXTypeCode.PRODUCTIMAGE, "TEST").get()

        then:
        "false" == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        new StatusOperation(pfxClient, PFXTypeCode.PRODUCT, "TEST").get()

        then:
        thrown(UnsupportedOperationException.class)

    }

}
