package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

class GenericDeleterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/delete-request.json"

    def "delete"() {
        given:
        def request = new ObjectMapper().readTree(GenericDeleterTest.class.getResourceAsStream(requestFile))

        when:
        def result = new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).delete(request)

        then:
        "1" == result


    }

}
