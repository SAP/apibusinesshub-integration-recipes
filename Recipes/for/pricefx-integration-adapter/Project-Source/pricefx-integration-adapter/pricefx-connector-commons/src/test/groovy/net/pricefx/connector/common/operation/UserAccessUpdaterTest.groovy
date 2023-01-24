package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.ConnectorException
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class UserAccessUpdaterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/assign-role-request.json"

    def "validateRequest"() {
        given:
        def request = new ObjectMapper().readTree(UserAccessUpdaterTest.class.getResourceAsStream(requestFile))

        when:
        new UserAccessUpdater(pfxClient, PFXTypeCode.ROLE, null, null).validateRequest(request)

        then:
        thrown(ConnectorException.class)

        when:
        new UserAccessUpdater(pfxClient, PFXTypeCode.ROLE, "DUMMY", null).validateRequest(request)

        then:
        thrown(ConnectorException.class)

        when:
        new UserAccessUpdater(pfxClient, PFXTypeCode.ROLE, "DUMMY_NO_ID", null).validateRequest(request)

        then:
        thrown(ConnectorException.class)

        when:
        new UserAccessUpdater(pfxClient, PFXTypeCode.ROLE, "USER", null).validateRequest(new ObjectNode(JsonNodeFactory.instance).put("add", "xxx"))

        then:
        thrown(RequestValidationException.class)

        when:
        new UserAccessUpdater(pfxClient, PFXTypeCode.ROLE, "USER", null).validateRequest(new ObjectNode(JsonNodeFactory.instance).put("xxx", "xxx"))

        then:
        thrown(RequestValidationException.class)

    }

    def "upsert"() {
        given:
        def request = new ObjectMapper().readTree(UserAccessUpdaterTest.class.getResourceAsStream(requestFile))

        when:
        def result = new UserAccessUpdater(pfxClient, PFXTypeCode.ROLE, "USER", null).upsert(request, true, false, false, false)

        then:
        Boolean.TRUE.toString() == result.get(0).get(PFXConstants.FIELD_VALUE).textValue()


    }

}
