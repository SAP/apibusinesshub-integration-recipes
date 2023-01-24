package net.pricefx.adapter.sap.service


import net.pricefx.connector.common.connection.MockFailedPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXConstants
import spock.lang.Specification

class PingServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    def failedClient = new MockFailedPFXOperationClient(builder)

    def "ping"() {
        when:
        def result = new PingService(pfxClient).execute(null, null)

        then:
        "true" == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        result = new PingService(failedClient).execute(null, null)

        then:
        "true" != result.get(PFXConstants.FIELD_VALUE).textValue()

    }

}
