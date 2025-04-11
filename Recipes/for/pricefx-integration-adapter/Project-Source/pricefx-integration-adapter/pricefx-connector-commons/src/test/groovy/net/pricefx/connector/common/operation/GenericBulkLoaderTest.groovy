package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.util.ClassUtil
import com.google.common.collect.ImmutableList
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.*
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class GenericBulkLoaderTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/bulkload-request.json"

    def "bulk load"() {
        given:
        def request = new ObjectMapper().readTree(GenericBulkLoaderTest.class.getResourceAsStream(requestFile))
        def original = request.deepCopy()

        when:
        def result = new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCTEXTENSION,
                new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(10).withTable("testTable"), null).bulkLoad(request, true)

        then:
        original.get(PFXConstants.FIELD_DATA).size() + "" == result

    }

    def "validateStructure"() {
        when:
        def data = new ArrayNode(JsonNodeFactory.instance)
        (1..10000).forEach { data.add("1") }

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(
                new ObjectNode(JsonNodeFactory.instance).set(PFXConstants.FIELD_DATA, data)
        )

        then:
        thrown(RequestValidationException.class)

        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2").add("attribute1"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance)))
        request.set("header", new ArrayNode(JsonNodeFactory.instance))


        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)


        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, new ArrayNode(JsonNodeFactory.instance))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)

        then:
        thrown(RequestValidationException.class)


        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, new ArrayNode(JsonNodeFactory.instance).add("x").add(new ArrayNode(JsonNodeFactory.instance)))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add(1).add("large")))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2"))
        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set("headerX",
                new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2"))
        request.set("header",
                new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2"))
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add("1")))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateStructure(request)


        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectMapper().readTree(GenericBulkLoaderTest.class.getResourceAsStream(requestFile))
        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).withMaximumRecords(1).validateStructure(request)

        then:
        thrown(RequestValidationException.class)

    }

    def "validateData - Product"() {

        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateData(request)

        then:
        thrown(RequestValidationException.class)


        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set("header",
                new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute2"))
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add(1)))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateData(request)

        then:
        thrown(RequestValidationException.class)


        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add(1).add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute1").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateData(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, new ArrayNode(JsonNodeFactory.instance).add(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add("1").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute1").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateData(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add(1).add((String) null)))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute1").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add(1).add("")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute1").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCT, null, null).validateData(request)

        then:
        thrown(RequestValidationException.class)


    }

    def "validateData - extensions"() {
        given:
        def ext = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withBusinessKeys(["sku", "attribute1"]).withAttributes(6)
                .withTable("dummy")


        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add("").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute1").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCTEXTENSION, ext, null).validateData(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add((String) null).add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute1").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCTEXTENSION, ext, null).validateData(request)

        then:
        noExceptionThrown()

        when:
        ext = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION)
                .withBusinessKeys(ImmutableList.of("sku", "attribute1", "attribute2", "attribute3")).withAttributes(5)
                .withTable("test")
        request = new ObjectMapper().readTree(ClassUtil.getResourceAsStream("/bulkload-px-request.json"))
        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCTEXTENSION, ext, null).validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add("x").add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add("sku").add("attribute30").add("attribute2"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.PRODUCTEXTENSION, ext, null).validateData(request)

        then:
        thrown(RequestValidationException.class)
    }

    def "validateRequest - lookup"() {
        when:
        def request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add(1).add("x")))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add(PFXConstants.FIELD_VALUE).add("lowerBound").add("upperBound"))
        def range = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), "STRING")
        range.table = "dummy"
        new GenericBulkLoader(pfxClient, PFXTypeCode.LOOKUPTABLE, range, "range").validateData(request)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add("0001").add(1).add(2)))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add(PFXConstants.FIELD_VALUE).add("lowerBound").add("upperBound"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.LOOKUPTABLE, range, "range").validateData(request)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance)
        request.set(PFXConstants.FIELD_DATA, JsonUtil.createArrayNode(
                new ArrayNode(JsonNodeFactory.instance).add((String) null).add(1).add(2)))
        request.set("header", new ArrayNode(JsonNodeFactory.instance).add(PFXConstants.FIELD_VALUE).add("lowerBound").add("upperBound"))

        new GenericBulkLoader(pfxClient, PFXTypeCode.LOOKUPTABLE, range, "range").validateData(request)

        then:
        thrown(RequestValidationException.class)

    }


}
