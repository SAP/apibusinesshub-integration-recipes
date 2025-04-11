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
    def conditionRecordType = pfxClient.createExtensionType(PFXTypeCode.CONDITION_RECORD, "Condition006", "")

    def "validateData"() {
        given:
        def request = new ObjectMapper().readTree(ConditionRecordUpdaterTest.class.getResourceAsStream(requestFile))

        when:
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        noExceptionThrown()

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attribute2", "50")
                        .put("attributeX", "50"),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attribute2", "50")
                        .put("attributeX", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

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
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        noExceptionThrown()

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_ID, 1)
                .put("attribute10", "50")
                .put("attribute2", 50)

        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

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
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attribute2", 50),
                new TextNode("x"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1).put("validFrom", "2024-10-31").put("attribute10", "50")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2).put("validFrom", "2024-10-31").put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1).put("key1", "abc").put("attribute10", "50")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2).put("key1", "abc").put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        thrown(RequestValidationException.class)


        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "")
                        .put("attribute2", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 1)
                        .put("attribute10", "50")
                        .put("attribute2", "50")
                        .put("attributeX", 50),
                new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_ID, 2)
                        .put("attribute10", "50")
                        .put("attributeX", "50")
                        .put("attribute2", "50"))
        new ConditionRecordUpdater(pfxClient, conditionRecordType,  "2024-01-01T00:00:00").validateData(request)

        then:
        thrown(RequestValidationException.class)
    }

    def "update"() {
        given:
        def request = new ObjectMapper().readTree(ConditionRecordUpdaterTest.class.getResourceAsStream(requestFile))

        when:
        def result = new ConditionRecordUpdater(pfxClient, conditionRecordType, "2024-01-01T00:00:00").bulkLoad(request, true)

        then:
        result =="errored: 4,5"

    }

}
