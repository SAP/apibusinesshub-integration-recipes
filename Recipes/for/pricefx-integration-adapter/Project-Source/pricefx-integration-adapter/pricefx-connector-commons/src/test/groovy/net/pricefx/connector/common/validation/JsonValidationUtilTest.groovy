package net.pricefx.connector.common.validation


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.*
import com.google.common.collect.ImmutableSet
import net.pricefx.connector.common.operation.IPFXObjectFetcher
import net.pricefx.connector.common.util.*
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXConstants.FIELD_FIELDNAME

class JsonValidationUtilTest extends Specification {

    def "validatePayload"() {
        given:
        def schema = new ObjectMapper().readTree(IPFXObjectFetcher.class.getResourceAsStream(PFXJsonSchema.DELETE_REQUEST.getPath()))

        when:
        def request = "{\n" +
                "  \"data\": {\n" +
                "\n" +
                "    \"filterCriteria\": {\n" +
                "      \"_constructor\": \"AdvancedCriteria\",\n" +
                "      \"operator\": \"and\",\n" +
                "      \"criteria\" : [\n" +
                "      {\n" +
                "        \"fieldName\" : \"uniqueName\",\n" +
                "        \"operator\" : \"startsWith\",\n" +
                "        \"value\" : \"P-22804\"\n" +
                "      }\n" +
                "    ]\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "}"

        JsonValidationUtil.validatePayload(schema,
                new ObjectMapper().readTree(request))


        then:
        thrown(RequestValidationException.class)

        when:
        JsonValidationUtil.validatePayload(schema,
                new ObjectMapper().readTree(
                        JsonValidationUtilTest.class.getResourceAsStream("/delete-request.json")))

        then:
        noExceptionThrown()

        when:
        request = "{\n" +
                "\n" +
                "      \"operator\": \"and\",\n" +
                "      \"criteria\" : [\n" +
                "      {\n" +
                "        \"fieldName\" : \"uniqueName\",\n" +
                "        \"operator\" : \"startsWith\",\n" +
                "        \"value\" : \"P-22804\"\n" +
                "      }\n" +
                "    ]\n" +
                "\n" +
                "}\n"
        JsonValidationUtil.validatePayload(schema,
                new ObjectMapper().readTree(request))

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validatePayload(schema, null)

        then:
        thrown(RequestValidationException.class)

    }

    def "validateExtraFields"() {
        given:
        def propertiesNode = new ObjectNode(JsonNodeFactory.instance)
        propertiesNode.set("test", new ObjectNode(JsonNodeFactory.instance))
        propertiesNode.set("test2", new ObjectNode(JsonNodeFactory.instance))

        def inputNode = new ObjectNode(JsonNodeFactory.instance).put("test", "x").put("test2", "y")

        def arrayNode = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put("test", "x").put("test2", "y"),
                new ObjectNode(JsonNodeFactory.instance).put("test", "x").put("test2", "y"))

        when:
        JsonValidationUtil.validateExtraFields(null, null)

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validateExtraFields(propertiesNode, inputNode)

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validateExtraFields(propertiesNode, arrayNode)

        then:
        noExceptionThrown()

        when:
        inputNode.put("test3", "x")
        JsonValidationUtil.validateExtraFields(propertiesNode, inputNode)

        then:
        thrown(RequestValidationException.class)

        when:
        arrayNode.add(inputNode)
        JsonValidationUtil.validateExtraFields(propertiesNode, arrayNode)

        then:
        thrown(RequestValidationException.class)

    }

    def "getMandatoryAttributes"() {
        given:
        def metadata = [attribute1: new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute1").put("requiredField", "true"),
                        attribute2: new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute2").put("requiredField", "false")]

        when:
        def results = JsonValidationUtil.getMandatoryAttributes(metadata)

        then:
        1 == results.size()
        results.contains("attribute1")

        when:
        results = JsonValidationUtil.getMandatoryAttributes(null)

        then:
        0 == results.size()

    }


    def "validateNumericField"() {
        when:
        JsonValidationUtil.validateNumericField(new TextNode("a"), 1, "upperBound")

        then:
        thrown(RequestValidationException.class)

        when:
        JsonValidationUtil.validateNumericField(new TextNode(""), 1, "upperBound")

        then:
        thrown(RequestValidationException.class)

        when:
        JsonValidationUtil.validateNumericField(new TextNode("1"), 1, "upperBound")

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validateNumericField(new IntNode(1), 1, "upperBound")

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validateNumericField(new NullNode(), 1, "upperBound")

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validateNumericField(new BooleanNode(true), 1, "upperBound")

        then:
        thrown(RequestValidationException.class)


    }


    def "validateDataType"() {
        given:
        def metadataNode = new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute1")
                .put("requiredField", false).put("fieldType", 1)

        when:
        JsonValidationUtil.validateDataType(new TextNode("abc"), metadataNode, "attribute1", 0)

        then:
        thrown(RequestValidationException.class)

        when:
        JsonValidationUtil.validateDataType(new TextNode("abc"), null, "attribute1", 0)

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validateDataType(new TextNode("1"), metadataNode, "attribute1", 0)

        then:
        noExceptionThrown()

        when:
        JsonValidationUtil.validateDataType(metadataNode, metadataNode, "attribute1", 0)

        then:
        thrown(RequestValidationException.class)

        when:
        JsonValidationUtil.validateDataType(NullNode.getInstance(), metadataNode, "attribute1", 0)

        then:
        noExceptionThrown()


    }


    def "getMandatoryAttributes - ALL"() {

        given:
        def extensionType = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(6).withTable("TEST")


        def node1 = new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute1").put("requiredField", false)
        def node2 = new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute2").put("requiredField", true)

        Map<String, ObjectNode> metadata = new HashMap<>()
        metadata.put("attribute1", node1)
        metadata.put("attribute2", node2)

        when:
        def results = JsonValidationUtil.getMandatoryAttributes(metadata, extensionType, PFXTypeCode.PRODUCTEXTENSION)

        then:
        2 == results.size()
        results.contains(PFXConstants.FIELD_SKU)
        results.contains("attribute2")

    }

    def "validateMissingMandatoryAttributes - from metadata only"() {
        given:
        def node = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "abc").put("attribute1", "def")
        def mandatory = ImmutableSet.of(PFXConstants.FIELD_SKU, "attribute2")

        when:
        JsonValidationUtil.validateMissingMandatoryAttributes(node, mandatory, -1)

        then:
        thrown(RequestValidationException.class)

        when:
        JsonValidationUtil.validateMissingMandatoryAttributes(node, mandatory, 11)

        then:
        thrown(RequestValidationException.class)

        when:
        node.put("attribute2", "abc")
        JsonValidationUtil.validateMissingMandatoryAttributes(node, mandatory, -1)

        then:
        noExceptionThrown()

    }


}
