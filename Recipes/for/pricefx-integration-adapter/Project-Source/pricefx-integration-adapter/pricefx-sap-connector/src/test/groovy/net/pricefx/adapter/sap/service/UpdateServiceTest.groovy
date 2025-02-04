package net.pricefx.adapter.sap.service

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

class UpdateServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)

    def "update"() {
        given:
        def request = new ObjectMapper().readTree(UpdateServiceTest.class.getResourceAsStream("/assign-role-request.json"))

        when:
        def result = new UpdateService(pfxClient, PFXTypeCode.ROLE, "USER").execute(request.toString())

        then:
        Boolean.TRUE.toString() == result.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        new UpdateService(pfxClient, PFXTypeCode.PRODUCT, "TEST").execute(request.toString())

        then:
        thrown(UnsupportedOperationException.class)

    }

}
