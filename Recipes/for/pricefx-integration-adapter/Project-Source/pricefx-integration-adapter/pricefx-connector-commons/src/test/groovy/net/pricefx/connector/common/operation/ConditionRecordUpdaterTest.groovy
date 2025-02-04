package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class ConditionRecordUpdaterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/update-conditionrecord-request.json"
    def requestFile2 = "/update-conditionrecord-request-2.json"
    def conditionRecordType = pfxClient.createExtensionType(PFXTypeCode.CONDITION_RECORD, "Condition006", "")

    def "validateRequest"() {
        given:
        def request = new ObjectMapper().readTree(ConditionRecordUpdaterTest.class.getResourceAsStream(requestFile))

        when:
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attributeX", "50"),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_ID, 1)
                .put("attribute10", "50")
                .put("attribute2", 50)

        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attribute2", 50),
                new TextNode("x"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1).put("validFrom", "2024-10-31").put("attribute10", "50")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1).put("key1", "abc").put("attribute10", "50")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)


        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)
    }

    def "update"() {
        given:
        def request = new ObjectMapper().readTree(ConditionRecordUpdaterTest.class.getResourceAsStream(requestFile))
        def request2 = new ObjectMapper().readTree(ConditionRecordUpdaterTest.class.getResourceAsStream(requestFile2))

        when:
        def result = new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").upsert(request, true, false, false, false, false)

        then:
        1 == result.get(0).get(PFXConstants.FIELD_ID).numberValue()
        2 == result.get(1).get(PFXConstants.FIELD_ID).numberValue()
        3 == result.get(2).get(PFXConstants.FIELD_ID).numberValue()
        4 == result.get(3).get(PFXConstants.FIELD_ID).numberValue()
        5 == result.size()

        result.get(3).get("errors").textValue().contains("conflicted")

        when:
        result = new ConditionRecordUpdater(pfxClient, conditionRecordType, null, "2024-01-01T00:00:00").upsert(request2, true, false, false, false, false)

        then:

        1 == result.get(0).get(PFXConstants.FIELD_ID).numberValue()
        2 == result.get(1).get(PFXConstants.FIELD_ID).numberValue()
        "version conflicted" == result.get(1).get("errors").get("version").get("errorMessage").textValue()


    }


}
