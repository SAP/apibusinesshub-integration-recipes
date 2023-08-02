package net.pricefx.adapter.sap.service


import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.ConnectorException
import spock.lang.Specification

class RefreshServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)

    def "refresh"() {
        when:
        def result = new RefreshService(pfxClient, PFXTypeCode.TOKEN, "dummy", null).refresh()

        then:
        "dummy-access-token" == result.get("access-token").textValue()

        when:
        new RefreshService(pfxClient, PFXTypeCode.TOKEN, null, null).refresh()

        then:
        thrown(ConnectorException.class)


        when:
        new RefreshService(pfxClient, PFXTypeCode.PRODUCT, null, null).refresh()

        then:
        thrown(UnsupportedOperationException.class)

    }

}
