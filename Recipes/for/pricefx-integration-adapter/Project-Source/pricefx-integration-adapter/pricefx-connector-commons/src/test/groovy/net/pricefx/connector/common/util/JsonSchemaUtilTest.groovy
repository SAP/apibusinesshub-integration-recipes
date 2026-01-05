package net.pricefx.connector.common.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.google.common.collect.ImmutableList
import spock.lang.Specification

import static net.pricefx.connector.common.util.PFXTypeCode.LOOKUPTABLE

class JsonSchemaUtilTest extends Specification {
    def schema = "{\n" +
            "  \"\$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
            "  \"id\": \"ProductQuery\",\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"sku\": {\n" +
            "      \"type\": \"string\"\n" +
            "    }\n" +
            "  }\n" +
            "}"

    def arraySchema = "{\n" +
            "  \"\$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
            "  \"id\": \"ProductExtensionUpsertRequest\",\n" +
            "  \"type\": \"array\",\n" +
            "  \"default\": [],\n" +
            "  \"items\": {\n" +
            "    \"type\": \"object\",\n" +
            "    \"required\": [\n" +
            "      \"sku\"\n" +
            "    ],\n" +
            "    \"properties\": {\n" +
            "      \"sku\": {\n" +
            "        \"type\": \"string\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}"

    def schemaNode = new ObjectMapper().readTree(schema)
    def arraySchemaNode = new ObjectMapper().readTree(arraySchema)


    def "getFields"() {
        given:
        JsonNode productSchemaNode = JsonSchemaUtil.loadSchema(PFXJsonSchema.PRODUCT_UPSERT_REQUEST, true)
        JsonSchemaUtil.updateSchemaWithMetadata(productSchemaNode, PFXTypeCode.PRODUCT, null, null, true, true, false)

        when:
        def results = JsonSchemaUtil.getFields(productSchemaNode)

        then:
        results.contains(PFXConstants.FIELD_SKU)
        results.size() == 36

        when:
        results = JsonSchemaUtil.getFields(schemaNode)

        then:
        results.first() == PFXConstants.FIELD_SKU

        when:
        results = JsonSchemaUtil.getFields(arraySchemaNode)

        then:
        results.first() == PFXConstants.FIELD_SKU

        when:
        def empty = ((ObjectNode) schemaNode.deepCopy()).remove(JsonSchemaUtil.SCHEMA_PROPERTIES)
        results = JsonSchemaUtil.getFields(empty)

        then:
        results.isEmpty()

        when:
        results = JsonSchemaUtil.getFields(null)

        then:
        results.isEmpty()

    }

    def "updateSchemaWithMetadata"() {
        given:
        def matrix = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX2.name())
        def tempSchemaNode = schemaNode.deepCopy()

        when:
        JsonSchemaUtil.updateSchemaWithMetadata(tempSchemaNode, null)

        then:
        tempSchemaNode == schemaNode

        when:
        def field = new HashMap<String, String>()
        field.put(PFXConstants.FIELD_NAME, "testing")
        field.put("numeric", true)

        List<Map<String, Object>> meta = ImmutableList.of(field)
        JsonSchemaUtil.updateSchemaWithMetadata(tempSchemaNode, meta)

        then:
        tempSchemaNode.get(JsonSchemaUtil.SCHEMA_PROPERTIES).get("testing") != null

        when:
        tempSchemaNode = schemaNode.deepCopy()
        JsonSchemaUtil.updateSchemaWithMetadata(tempSchemaNode, null, null, null, false, false, true)

        then:
        tempSchemaNode == schemaNode

        when:
        JsonSchemaUtil.updateSchemaWithMetadata(tempSchemaNode, LOOKUPTABLE, matrix, null, true, true, true)

        then:
        tempSchemaNode.toString().count("\"attribute1\"") == 1
        tempSchemaNode.toString().count("\"attribute10\"") == 1
        tempSchemaNode.toString().count("\"key1\"") == 1

        when:
        tempSchemaNode = new TextNode("x")
        JsonSchemaUtil.updateSchemaWithMetadata(tempSchemaNode, LOOKUPTABLE, matrix, null, true, true, true)

        then:
        tempSchemaNode.textValue() == "x"

        when:
        tempSchemaNode = new ObjectMapper().readTree(arraySchema)
        JsonSchemaUtil.updateSchemaWithMetadata(tempSchemaNode, LOOKUPTABLE, matrix, null, true, true, true)

        then:
        tempSchemaNode.toString().count("\"attribute1\"") == 1
        tempSchemaNode.toString().count("\"attribute10\"") == 1
        tempSchemaNode.toString().count("\"key1\"") == 1

        when:
        tempSchemaNode = new ObjectMapper().readTree(schema)
        JsonSchemaUtil.updateSchemaWithMetadata(tempSchemaNode, LOOKUPTABLE, matrix, null, true, true, false)

        then:
        tempSchemaNode.toString().count("\"attribute1\"") == 1
        tempSchemaNode.toString().count("\"attribute10\"") == 1
        tempSchemaNode.toString().count("\"key1\"") == 1
        tempSchemaNode.toString().count("\"type\"") == 2
    }
}
