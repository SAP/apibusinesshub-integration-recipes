package net.pricefx.connector.common.operation


import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class ActionItemUpdaterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def pfxDummyClient = new MockDummyPFXOperationClient()

    def "buildUpsertRequest"() {
        given:
        def request = new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test").put("attribute1", "test1"))

        when:
        ArrayNode upsertRequest = new ActionItemUpdater(pfxClient).buildUpsertRequest(request)

        then:
        upsertRequest.size() == 1
        JsonUtil.getNumericValue(upsertRequest.get(0).get("oldValues").get(PFXConstants.FIELD_VERSION)) == 1
        JsonUtil.getValueAsText(upsertRequest.get(0).get("oldValues").get(PFXConstants.FIELD_TYPEDID)) == "1.AI"
        JsonUtil.getValueAsText(upsertRequest.get(0).get(PFXConstants.FIELD_DATA).get(PFXConstants.FIELD_TYPEDID)) == "1.AI"
        JsonUtil.getValueAsText(upsertRequest.get(0).get(PFXConstants.FIELD_DATA).get("attribute1")) == "test1"
        upsertRequest.get(0).get(PFXConstants.FIELD_UNIQUENAME) == null

        when:
        request = new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test").put("attribute1", "test1"))
        upsertRequest = new ActionItemUpdater(pfxDummyClient).buildUpsertRequest(request)

        then:
        upsertRequest.size() == 1
        "Actions to be updated not found:test" == upsertRequest.get(0).get("message").textValue()

    }


    def "validateRequest"() {
        given:
        def request = new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test-config").put("attribute1", "test1"))


        when:
        new ActionItemUpdater(pfxClient).validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request =  new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance).put("attribute1", "test1"))
        new ActionItemUpdater(pfxClient).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request =  new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test-config").put("value", "test1"))
        new ActionItemUpdater(pfxClient).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ArrayNode(JsonNodeFactory.instance).add(request)
        new ActionItemUpdater(pfxClient).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)


    }
}