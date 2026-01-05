package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.collect.ImmutableList
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.*
import net.pricefx.connector.common.validation.RequestValidationException
import org.apache.tika.utils.StringUtils
import spock.lang.Specification

class GenericUpsertorTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/upsert-product-request.json"

    def "replaceNullKeyWithEmptyString"() {

        given:
        ObjectNode request = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "test2").put("attribute1", (String) null)
        PFXExtensionType type = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION)
                .withBusinessKeys(ImmutableList.of("attribute2", "attribute1"))
                .withAttributes(6).withTable("tableTest")

        when:
        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCTEXTENSION, type, null).replaceNullKeyWithEmptyString(type, request)

        then:
        request.get("attribute1").textValue() == StringUtils.EMPTY
        request.get("attribute2").textValue() == StringUtils.EMPTY
        request.get(PFXConstants.FIELD_SKU).textValue() == "test2"

    }

    def "getExistingFields"() {
        given:
        def request = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "test2").put("attribute1", "test2")

        when:
        def result = new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).getExistingFields(request)

        then:
        result.size() == 2
        result.contains(PFXConstants.FIELD_SKU)
        result.contains("attribute1")
    }

    def "buildUpsertRequest"() {
        given:
        def request = new ArrayNode(JsonNodeFactory.instance)
        request.add(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "test").put("attribute1", "test")).
                add(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "test2").put("attribute1", "test2"))

        def type = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(6).withTable("tableTest")
        when:
        ArrayNode upsertRequest = new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCTEXTENSION, type, null).buildUpsertRequest(request)

        then:
        JsonUtil.getValueAsText(upsertRequest.get(0).get(PFXConstants.FIELD_NAME)) == "tableTest"
        JsonUtil.getValueAsText(upsertRequest.get(1).get(PFXConstants.FIELD_NAME)) == "tableTest"
        JsonUtil.getValueAsText(upsertRequest.get(0).get(PFXConstants.FIELD_SKU)) == "test"
        JsonUtil.getValueAsText(upsertRequest.get(1).get(PFXConstants.FIELD_SKU)) == "test2"


    }

    def "validateRequest"() {
        when:
        def request = new ArrayNode(JsonNodeFactory.instance)
        (0..1000).each { request.add(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")) }


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute1", "1").put("attribute2", "1").put("attributeX", "x"))


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute1", "1").put("attribute2", "1"))


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute1", "1").put("attribute2", ""))


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute1", "1").put("attribute2", (String) null))

        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_USER_LOGINNAME, "")
                .put("email", "abc@abc.com"))


        new GenericUpsertor(pfxClient, PFXTypeCode.USER, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_USER_LOGINNAME, "abc")
                .put("email", "abc@abc.com"))


        new GenericUpsertor(pfxClient, PFXTypeCode.USER, null, null).validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x").put("attribute1", "").put("attribute2", "x"))

        PFXExtensionType extensionType = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION)
                .withBusinessKeys(ImmutableList.of(PFXConstants.FIELD_SKU, "attribute1", "attribute2")).withAttributes(3)

        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCTEXTENSION, extensionType, null).validateRequest(request, false)
        then:
        noExceptionThrown()

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "").put("attribute1", "x").put("attribute2", "x"))

        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCTEXTENSION, extensionType, null).validateRequest(request, false)
        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute2", "1"))


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        noExceptionThrown()

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute1", "x").put("attribute2", "1"))


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute1", "1").put("attribute2", 1))


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "x")
                .put("attribute1", "1"))


        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).validateRequest(request, false)

        then:
        thrown(RequestValidationException.class)

        when:
        request = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_VALUE, "x").put("lowerBound", 0).put("upperBound", 1))

        def range = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableValueType.STRING.name())

        new GenericUpsertor(pfxClient, PFXTypeCode.LOOKUPTABLE, range, null).validateRequest(request, false)
        then:
        noExceptionThrown()


        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "")
                .put("attribute1", "1").put("attribute2", "1")

        JsonNode schemaNode = JsonSchemaUtil.loadSchema(PFXJsonSchema.POST_REQUEST, true)

        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, schemaNode).validateRequest(request, false)

        then:
        noExceptionThrown()

    }


    def "upsert"() {
        given:
        def request = new ObjectMapper().readTree(GenericUpsertorTest.class.getResourceAsStream(requestFile))
        def original = request.deepCopy()

        when:
        def result = new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).upsert(request, true, false, false, false, false, false)

        then:
        result.get(0) == original.get(0)

        when:
        new GenericUpsertor(pfxClient, PFXTypeCode.PRODUCT, null, null).withMaximumRecords(1).upsert(request, true, false, false, false, false, false)

        then:
        thrown(RequestValidationException.class)


    }
}
