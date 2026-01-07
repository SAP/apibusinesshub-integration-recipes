package net.pricefx.connector.common.operation


import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import spock.lang.Specification

class GenericSingleUpsertorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()


    def "upsert"() {

        given:
        def request = new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test").put(PFXConstants.FIELD_VALUE, "test1")
        def upsertor = new GenericSingleUpsertor(pfxClient, PFXTypeCode.ADVANCED_CONFIG, null, null)
        upsertor.setUpdateOnly(true)

        when:
        def result = upsertor.
                upsert(request, true, false, false, false, false)

        then:
        result == request


    }
}
