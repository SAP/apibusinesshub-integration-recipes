package net.pricefx.connector.common.util


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.collect.ImmutableList
import spock.lang.Specification


class ResponseUtilTest extends Specification {

    def "removeNull"() {
        given:
        JsonNode node1 = new ObjectNode(JsonNodeFactory.instance)
                .put("test", "1")
                .put(PFXConstants.FIELD_NAME, "test1")
                .put("attribute1", "a1")
                .put("attribute2", (String) null)
                .put("attribute3", (String) null)
                .put("version", 1)
                .put(PFXConstants.FIELD_TYPEDID, "123")
                .put("createdBy", 123)
                .put("lastUpdateBy", 123)

        JsonNode node2 = new ObjectNode(JsonNodeFactory.instance)
                .put("test", "1")
                .put(PFXConstants.FIELD_NAME, "test1")
                .put("attribute1", "a1")
                .put("version", 1)
                .put("attribute2", (String) null)
                .put("attribute3", (String) null)

        def list = [node1, node2]

        when:
        list.forEach {
            item -> ResponseUtil.removeNull(item)
        }

        then:
        2 == list.size()
        !ImmutableList.of(list.get(0).fieldNames()).contains("attribute2")
        !ImmutableList.of(list.get(1).fieldNames()).contains("attribute2")

        7 == list.get(0).fieldNames().size()
        4 == list.get(1).fieldNames().size()

        when:
        def result = ResponseUtil.removeNull(null)

        then:
        !result


    }

    def "formatResponse"() {
        given:
        JsonNode node1 = new ObjectNode(JsonNodeFactory.instance)
                .put("test", "1")
                .put(PFXConstants.FIELD_NAME, "test1")
                .put("attribute1", "a1")
                .put("attribute2", (String) null)
                .put("attribute3", (String) null)
                .put("version", 1)
                .put(PFXConstants.FIELD_TYPEDID, "123")
                .put("createdBy", 123)
                .put("lastUpdateBy", 123)

        JsonNode node2 = new ObjectNode(JsonNodeFactory.instance)
                .put("test", "1")
                .put(PFXConstants.FIELD_NAME, "test1")
                .put("attribute1", "a1")
                .put("version", 1)
                .put("attribute2", (String) null)
                .put("attribute3", "x")

        def list = [node1, node2]

        when:
        list.forEach {
            item -> ResponseUtil.formatResponse(PFXTypeCode.PRODUCT, null, item, false)
        }

        then:
        2 == list.size()

        3 == list.get(0).fieldNames().size()
        4 == list.get(1).fieldNames().size()

    }

    def "formatResponse - Lookup"() {
        given:
        PFXLookupTableType dummy = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()).withTable("1234")

        JsonNode node = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_NAME, "test1")
                .put(PFXConstants.FIELD_RAWVALUE, "10")
                .put(PFXConstants.FIELD_VALUE, 10)
                .put(PFXConstants.FIELD_TYPEDID, "123")
                .put(PFXConstants.FIELD_TYPE, "SIMPLE")
                .put(PFXConstants.FIELD_VALUETYPE, "STRING")

        JsonNode node2 = node.deepCopy()
                .put(PFXConstants.FIELD_TYPE, "MATRIX")
                .put(PFXConstants.FIELD_VALUETYPE, "MATRIX2")
                .put("attribute1", (String) null)
                .put("attribute2", "test")
                .put("key1", "test")
                .put("key2", "test")
        node2.remove(PFXConstants.FIELD_RAWVALUE)
        node2.remove(PFXConstants.FIELD_VALUE)

        JsonNode node3 = node.deepCopy()
                .put(PFXConstants.FIELD_TYPE, "MATRIX")
                .put(PFXConstants.FIELD_VALUETYPE, "MATRIX")
                .put("attribute1", (String) null)
                .put("attribute2", "test")
        node3.remove(PFXConstants.FIELD_RAWVALUE)
        node3.remove(PFXConstants.FIELD_VALUE)

        JsonNode node4 = node.deepCopy()
                .put(PFXConstants.FIELD_TYPE, "RANGE")
                .put(PFXConstants.FIELD_VALUETYPE, "NUMBER")
                .put(PFXConstants.FIELD_VALUE, "test")
                .put("lowerBound", 10)
                .put("upperBound", 15)
                .put(PFXConstants.FIELD_RAWVALUE, "test")

        JsonNode node5 = node.deepCopy()
        node5.remove(PFXConstants.FIELD_RAWVALUE)

        when:
        ResponseUtil.formatResponse(PFXTypeCode.LOOKUPTABLE, dummy, node5, false)

        then:
        2 == node5.fieldNames().size()
        10 == node5.get(PFXConstants.FIELD_VALUE).intValue()

        when:
        ResponseUtil.formatResponse(PFXTypeCode.LOOKUPTABLE, dummy, node, false)

        then:
        2 == node.fieldNames().size()
        !node.get(PFXConstants.FIELD_RAWVALUE)
        "10" == node.get(PFXConstants.FIELD_VALUE).textValue()

        when:
        ResponseUtil.formatResponse(PFXTypeCode.LOOKUPTABLE,dummy,  node2, false)

        then:
        3 == node2.fieldNames().size()
        !node2.get(PFXConstants.FIELD_NAME)
        !node2.get("attribute1")
        "test" == node2.get("attribute2").textValue()

        when:
        ResponseUtil.formatResponse(PFXTypeCode.LOOKUPTABLE,dummy,  node3, false)

        then:
        2 == node3.fieldNames().size()
        "test1" == node3.get(PFXConstants.FIELD_NAME).textValue()
        !node3.get("attribute1")
        "test" == node3.get("attribute2").textValue()

        when:
        ResponseUtil.formatResponse(PFXTypeCode.LOOKUPTABLE,dummy,  node4, false)

        then:
        3 == node4.fieldNames().size()
        !node4.get(PFXConstants.FIELD_NAME)
        !node4.get("attribute1")
        10 == node4.get("lowerBound").intValue()

    }


    def "formatCalculableCollectionResponse"() {
        given:
        def lineItemInput = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_ID, "input1")
                .put("param", (String) null)
                .put(PFXConstants.FIELD_VALUE, 1)

        def lineItemInputObject = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_ID, "input2")
                .put("param", (String) null)
                .set(PFXConstants.FIELD_VALUE, new ObjectNode(JsonNodeFactory.instance).put("isSold", true))

        def lineItemInputArray = JsonUtil.createArrayNode(lineItemInput, lineItemInputObject)

        def lineItemOutput = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_ID, "output1")
                .put("param", (String) null)
                .put("result", 1)

        def lineItemOutputObject = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_ID, "output2")
                .put("param", (String) null)
                .set("result", new ObjectNode(JsonNodeFactory.instance).put("isSold", true))

        def lineItemOutputArray = JsonUtil.createArrayNode(lineItemOutput, lineItemOutputObject)

        def lineItem = new ObjectNode(JsonNodeFactory.instance)
                .put("parent", (String) null)
                .put(PFXConstants.FIELD_VALUE, "abc")
                .put(PFXConstants.FIELD_TYPEDID, "123.QLI")
        lineItem.set(PFXConstants.FIELD_INPUTS, lineItemInputArray)
        lineItem.set(PFXConstants.FIELD_OUTPUTS, lineItemOutputArray)

        def lineItemArray = JsonUtil.createArrayNode(lineItem)

        def node = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_UNIQUENAME, "abcde")
                .put(PFXConstants.FIELD_TYPEDID, "abcde")
                .put(PFXConstants.ATTRIBUTE_EXT_PREFIX + "ProjectName", "abcde")
        node.set(PFXConstants.FIELD_LINEITEMS, lineItemArray)
        node.set(PFXConstants.FIELD_INPUTS, lineItemInputArray.deepCopy())
        node.set(PFXConstants.FIELD_OUTPUTS, lineItemOutputArray.deepCopy())

        when:
        ResponseUtil.formatResponse(PFXTypeCode.QUOTE, null,node, true)

        then:
        !node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get("param")
        !node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_OUTPUTS).get(0).get("param")
        node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).isTextual()
        node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_OUTPUTS).get(0).get("result").isTextual()

        "1" == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).textValue()
        "1" == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_OUTPUTS).get(0).get("result").textValue()
        "input1" == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_ID).textValue()
        "output1" == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_OUTPUTS).get(0).get(PFXConstants.FIELD_ID).textValue()


        null == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(1).get(PFXConstants.FIELD_VALUE)
        true == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_INPUTS).get(1).get("valueObject").get("isSold").asBoolean()

        null == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_OUTPUTS).get(1).get("result")
        true == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_OUTPUTS).get(1).get("resultObject").get("isSold").asBoolean()

        !node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_TYPEDID)
        "abc" == node.get(PFXConstants.FIELD_LINEITEMS).get(0).get(PFXConstants.FIELD_VALUE).textValue()

        !node.get(PFXConstants.FIELD_INPUTS).get(0).get("param")
        !node.get(PFXConstants.FIELD_OUTPUTS).get(0).get("param")
        node.get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).isTextual()
        node.get(PFXConstants.FIELD_OUTPUTS).get(0).get("result").isTextual()

        "1" == node.get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).textValue()
        "1" == node.get(PFXConstants.FIELD_OUTPUTS).get(0).get("result").textValue()
        "input1" == node.get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_ID).textValue()
        "output1" == node.get(PFXConstants.FIELD_OUTPUTS).get(0).get(PFXConstants.FIELD_ID).textValue()

        !node.get(PFXConstants.FIELD_TYPEDID)
        "abcde" == node.get(PFXConstants.FIELD_UNIQUENAME).textValue()

        "abcde" == node.get("ProjectName").textValue()
        !node.get(PFXConstants.ATTRIBUTE_EXT_PREFIX + "ProjectName")

        when:
        node = new ObjectNode(JsonNodeFactory.instance)
        node.put(PFXConstants.FIELD_LINEITEMS, "abc")
        node.put(PFXConstants.FIELD_INPUTS, "abc")
        node.put(PFXConstants.FIELD_UNIQUENAME, "abcde")
        ResponseUtil.formatResponse(PFXTypeCode.QUOTE, null, node, false)

        then:
        "abc" == node.get(PFXConstants.FIELD_LINEITEMS).textValue()
        "abc" == node.get(PFXConstants.FIELD_INPUTS).textValue()
        "abcde" == node.get(PFXConstants.FIELD_UNIQUENAME).textValue()


    }

    def "formatDataCollectionResponse"() {
        given:
        def field = new ObjectNode(JsonNodeFactory.instance)
                .put(PFXConstants.FIELD_ID, "input1")
                .put("param", (String) null)
                .put(PFXConstants.FIELD_VALUE, 1)

        def fields = JsonUtil.createArrayNode(field)

        def node = new ObjectNode(JsonNodeFactory.instance).put("abc", "def")
        node.set("fields", fields)

        when:
        ResponseUtil.formatResponse(PFXTypeCode.DATAFEED, null,node, false)

        then:
        "def" == node.get("abc").textValue()
        2 == node.get("fields").get(0).size()

        when:
        node = new ObjectNode(JsonNodeFactory.instance)
                .put("fields", "abc")
                .put("abc", "def")

        ResponseUtil.formatResponse(PFXTypeCode.DATAFEED, null, node, false)

        then:
        "def" == node.get("abc").textValue()
        "abc" == node.get("fields").textValue()
    }

    def "formatFormulaResponse"() {
        given:
        JsonNode node = new ObjectNode(JsonNodeFactory.instance)
                .put("result", "1")
                .put("attribute1", "a1")


        when:
        ResponseUtil.formatFormulaResponse(node)

        then:
        "1" == node.get("result").asText()
        "a1" == node.get("attribute1").asText()
        null == node.get("resultObject")
        null == node.get("resultArray")

        when:
        node = new ObjectNode(JsonNodeFactory.instance).put("attribute1", "a1")
                .set("result", new ObjectNode(JsonNodeFactory.instance).put("attribute1", "a2"))

        ResponseUtil.formatFormulaResponse(node)

        then:
        "a2" == node.get("resultObject").get("attribute1").textValue()
        "a1" == node.get("attribute1").asText()
        null == node.get("result")
        null == node.get("resultArray")

        when:
        node = new ObjectNode(JsonNodeFactory.instance).put("attribute1", "a1")
                .set("result", new ArrayNode(JsonNodeFactory.instance).add("a2"))

        ResponseUtil.formatFormulaResponse(node)

        then:
        "a2" == node.get("resultArray").get(0).textValue()
        "a1" == node.get("attribute1").asText()
        null == node.get("result")
        null == node.get("resultObject")
    }
}