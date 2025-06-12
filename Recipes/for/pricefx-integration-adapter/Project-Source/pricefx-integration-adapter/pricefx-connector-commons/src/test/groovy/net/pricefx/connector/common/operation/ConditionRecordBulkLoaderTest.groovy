package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class ConditionRecordBulkLoaderTest extends Specification {
    def pfxClient = new MockPFXOperationClient()

    def requestFile = "/bulkload-conditionrecord-request.json"
    def requestFileConditionRecordExtraFields = "/bulkload-conditionrecord-request-extrafields.json"
    def requestFileConditionRecordMissingKeys = "/bulkload-conditionrecord-request-missingkeys.json"
    def ext = pfxClient.createExtensionType(PFXTypeCode.CONDITION_RECORD, "Condition006", "")


    def "bulk load"() {
        when:
        def request = new ObjectMapper().readTree(GenericBulkLoaderTest.class.getResourceAsStream(requestFile))
        new ConditionRecordBulkLoader(pfxClient,  ext).bulkLoad(request, true)

        then:
        noExceptionThrown()

        when:
        request = new ObjectMapper().readTree(GenericBulkLoaderTest.class.getResourceAsStream(requestFileConditionRecordMissingKeys))
        new ConditionRecordBulkLoader(pfxClient,  ext).bulkLoad(request, true)

        then:
        thrown(RequestValidationException.class)

        when:
        ext = pfxClient.createExtensionType(PFXTypeCode.CONDITION_RECORD, "Condition006", "")
        request = new ObjectMapper().readTree(GenericBulkLoaderTest.class.getResourceAsStream(requestFileConditionRecordExtraFields))
        new ConditionRecordBulkLoader(pfxClient,  ext).bulkLoad(request, true)

        then:
        thrown(RequestValidationException.class)

    }

    def "validateData" () {
        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x").add("x").add("x").add("x").add("x").add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("key1").add("key2").add("key3").add("key4").add("validTo").add("validFrom").add("attribute2").add("attribute10"))

        new ConditionRecordBulkLoader(pfxClient,  ext).validateData(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x").add("x").add("x").add("x").add("x").add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("key1").add("key2").add("key3").add("key4").add("validTo").add("attribute1").add("attribute2").add("attribute10"))

        new ConditionRecordBulkLoader(pfxClient,  ext).validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x").add("x").add("x").add("x").add("x").add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("key1").add("key2").add("key3").add("key4").add("validTo").add("validFrom").add("attributeX").add("attribute10"))

        new ConditionRecordBulkLoader(pfxClient,  ext).validateData(request)

        then:
        thrown(RequestValidationException.class)

    }
    def "validateStructure"() {


        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x").add("x").add("x").add("x").add("x").add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("key1").add("key2").add("key3").add("key4").add("validTo").add("validFrom").add("attribute2").add("attribute10"))

        new ConditionRecordBulkLoader(pfxClient,  ext).validateStructure(request)

        then:
        noExceptionThrown()


        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x").add("x").add("x").add("x").add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("key1").add("key2").add("key3").add("key4").add("validTo").add("validFrom").add("attribute2").add("attribute10"))

        new ConditionRecordBulkLoader(pfxClient,  ext).validateStructure(request)

        then:
        thrown(RequestValidationException.class)






    }




}
