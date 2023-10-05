package net.pricefx.adapter.sap.service


import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.CustomOperation
import net.pricefx.connector.common.util.PFXConstants
import spock.lang.Specification

class ExecuteServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)


    def "execute"() {
        when:
        def result = new ExecuteService(pfxClient, CustomOperation.QUOTE_SUBMIT, "P-1").execute(null)

        then:
        "true" == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        new ExecuteService(pfxClient, CustomOperation.METADATA, "P-1").execute(null)

        then:
        thrown(UnsupportedOperationException.class)

    }

}
