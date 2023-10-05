package net.pricefx.adapter.sap.service

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

class CreateServiceTest extends Specification {
    def builder = ConnectionUtil.getPFXClientBuilder("test", "https://dummy.com", "abcdefg")
    def pfxClient = new MockPFXOperationClient(builder)
    String CREATE_REQUEST = "/create-quote-request.json"


    def "create"() {
        given:
        def request = new ObjectMapper().readTree(CreateServiceTest.class.getResourceAsStream(CREATE_REQUEST))

        when:
        def result = new CreateService(pfxClient, PFXTypeCode.QUOTE).execute(request.toString())

        then:
        "TEST" == result.get(PFXConstants.FIELD_UNIQUENAME).textValue()

        when:
        new CreateService(pfxClient, PFXTypeCode.PRODUCTIMAGE).execute("{}")

        then:
        thrown(UnsupportedOperationException.class)

    }

}
