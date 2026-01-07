package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXOperation
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

import static net.pricefx.connector.common.util.ConnectionUtil.createPath
import static net.pricefx.connector.common.util.PFXTypeCode.ADVANCED_CONFIG

class AdvancedConfigurationUpsertorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def pfxDummyClient = new MockDummyPFXOperationClient()

    def "getApiPath"() {
        given:
        def upsertor = new AdvancedConfigurationUpsertor(pfxClient)

        when:
        def path = upsertor.getApiPath(false)

        then:
        path == createPath(PFXOperation.UPDATE.getOperation(), ADVANCED_CONFIG.getTypeCode())

        when:
        upsertor.setUpdateOnly(false)
        path = upsertor.getApiPath(false)

        then:
        path == createPath(PFXOperation.ADD.getOperation(), ADVANCED_CONFIG.getTypeCode())

    }

    def "buildUpsertRequest"() {
        given:
        def request = new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test").put(PFXConstants.FIELD_VALUE, "test1")

        when:
        ArrayNode upsertRequest = new AdvancedConfigurationUpsertor(pfxClient).buildUpsertRequest(request)

        then:
        upsertRequest.size() == 1
        JsonUtil.getNumericValue(upsertRequest.get(0).get(PFXConstants.FIELD_VERSION)) == 1
        JsonUtil.getValueAsText(upsertRequest.get(0).get(PFXConstants.FIELD_TYPEDID)) == "1.AP"
        JsonUtil.getValueAsText(upsertRequest.get(0).get(PFXConstants.FIELD_VALUE)) == "test1"
        upsertRequest.get(0).get(PFXConstants.FIELD_UNIQUENAME) == null

        when:
        upsertRequest = new AdvancedConfigurationUpsertor(pfxDummyClient).buildUpsertRequest(request)

        then:
        upsertRequest.get(0) == request

    }

    def "validateRequest"() {
        given:
        def request = new ObjectNode(JsonNodeFactory.instance).
                put(PFXConstants.FIELD_UNIQUENAME, "test-config").put(PFXConstants.FIELD_VALUE, "test1")


        when:
        new AdvancedConfigurationUpsertor(pfxClient).validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_VALUE, "test1")
        new AdvancedConfigurationUpsertor(pfxClient).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ArrayNode(JsonNodeFactory.instance).add(request)
        new AdvancedConfigurationUpsertor(pfxClient).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance).
                put("dummy", "test-config").put(PFXConstants.FIELD_VALUE, "test1").put(PFXConstants.FIELD_UNIQUENAME, "test-config")

        new AdvancedConfigurationUpsertor(pfxClient).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)
    }
}