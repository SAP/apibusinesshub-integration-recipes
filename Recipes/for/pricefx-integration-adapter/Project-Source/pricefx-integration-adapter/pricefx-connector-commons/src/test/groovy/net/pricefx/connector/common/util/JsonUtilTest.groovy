package net.pricefx.connector.common.util


import com.fasterxml.jackson.databind.node.*
import net.pricefx.connector.common.validation.ConnectorException
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

class JsonUtilTest extends Specification {

    def "getData"() {
        when:
        def result = JsonUtil.getData(null)

        then:
        result.isMissingNode()

        when:
        result = JsonUtil.getData(new ObjectNode(JsonNodeFactory.instance).set("response",
                new ObjectNode(JsonNodeFactory.instance).set(PFXConstants.FIELD_DATA,
                        new ArrayNode(JsonNodeFactory.instance))))

        then:
        result.isArray()

        when:
        result = JsonUtil.getData(new ObjectNode(JsonNodeFactory.instance).set("response",
                new NullNode()))

        then:
        result.isMissingNode()


    }

    def "getFieldNames"() {
        when:
        def results = JsonUtil.getFieldNames("{\n    \"Product_Details\":{\n        \"label\":\"Product Details\", \n        \"userGroupEdit\":null, \n        \"userGroupViewDetails\":null, \n        \"allowSearch\":true, \n        \"allowPASearch\":true, \n        \"businessKey\":null, \n        \"numberOfAttributes\":6\n    }, \n    \"Product_Origin\":{\n        \"label\":\"Product Origin\", \n        \"userGroupEdit\":null, \n        \"userGroupViewDetails\":null, \n        \"allowSearch\":true, \n        \"allowPASearch\":true, \n        \"businessKey\":null, \n        \"numberOfAttributes\":3\n    }\n}")

        then:
        ["Product_Details", "Product_Origin"] == results

        when:
        results = JsonUtil.getFieldNames("[]")

        then:
        results.isEmpty()
    }

    def "countJson"(node1, result1) {
        expect:
        result1 == JsonUtil.countJson(node1)

        where:
        node1                                                                                                    | result1
        JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put("test", "1").put(PFXConstants.FIELD_NAME, "test1"),
                new ObjectNode(JsonNodeFactory.instance).put("test", "2").put(PFXConstants.FIELD_NAME, "test2")) | 2
        MissingNode.instance                                                                                     | 0
        new ObjectNode(JsonNodeFactory.instance)                                                                 | 1
        null                                                                                                     | 0
    }

    def "getStringArray"() {
        when:
        ObjectNode dataNode = new ObjectNode(JsonNodeFactory.instance)
        dataNode.putArray("sortBy").add("test1").add("test2")

        def results = JsonUtil.getStringArray(dataNode.get("sortBy"))

        then:
        "test1" == results.get(0)
        "test2" == results.get(1)

        when:
        results = JsonUtil.getStringArray(null)

        then:
        !results

    }


    def "getFirstDataNode from single node"() {
        given:
        ObjectNode node1 = new ObjectNode(JsonNodeFactory.instance).put("test", "1").put(PFXConstants.FIELD_NAME, "test1")
        ObjectNode node2 = new ObjectNode(JsonNodeFactory.instance).put("test", "2").put(PFXConstants.FIELD_NAME, "test2")
        ArrayNode arrayNode = JsonUtil.createArrayNode(node1, node2)

        when:
        def result = arrayNode.get(0)

        then:
        "1" == result.get("test").textValue()

    }

    def "getPFXLookupTableType"() {
        given:
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance)

        when:
        JsonUtil.getPFXLookupTableType(node)

        then:
        thrown(ConnectorException.class)

        when:
        node.put(PFXConstants.FIELD_TYPE, "SIMPLE")
        def result = JsonUtil.getPFXLookupTableType(node)

        then:
        "SIMPLE" == result.getLookupTableType().name()

        when:
        node.put(PFXConstants.FIELD_TYPE, "SIMPLE")
        node.put(PFXConstants.FIELD_VALUETYPE, "STRING")
        result = JsonUtil.getPFXLookupTableType(node)

        then:
        "SIMPLE" == result.getLookupTableType().name()

    }

    def "getSelectedField"() {
        when:
        def result = JsonUtil.getSelectedField("{\n    \"Product_Details\":{\n        \"label\":\"Product Details\", \n        \"userGroupEdit\":null, \n        \"userGroupViewDetails\":null, \n        \"allowSearch\":true, \n        \"allowPASearch\":true, \n        \"businessKey\":null, \n        \"numberOfAttributes\":6\n    }, \n    \"Product_Origin\":{\n        \"label\":\"Product Origin\", \n        \"userGroupEdit\":null, \n        \"userGroupViewDetails\":null, \n        \"allowSearch\":true, \n        \"allowPASearch\":true, \n        \"businessKey\":null, \n        \"numberOfAttributes\":3\n    }\n}", "Product_Origin")

        then:
        result.isObject()
        3 == result.get("numberOfAttributes").intValue()
    }


    def "getValueAsText"(node4, result4) {
        expect:
        result4 == JsonUtil.getValueAsText(node4)

        where:
        node4                                                  | result4
        new TextNode("xxx")                                    | "xxx"
        new BooleanNode(false)                                 | "false"
        JsonNodeFactory.instance.numberNode(1)                 | "1"
        new TextNode((String) null)                            | ""
        null                                                   | ""
        new ObjectNode(JsonNodeFactory.instance).put("x", "x") | ""

    }

    def "getRandomId"() {
        when:
        def result = JsonUtil.getRandomId("abcde")

        then:
        new String(Base64.decoder.decode(result.getBytes())).contains("abcde")
    }

    def "isObjectNode"(node5, result5) {
        expect:
        result5 == JsonUtil.isObjectNode(node5)

        where:
        node5                                    | result5
        new ObjectNode(JsonNodeFactory.instance) | true
        new ArrayNode(JsonNodeFactory.instance)  | false
        null                                     | false
    }

    def "isArrayNode"(node6, result6) {
        expect:
        result6 == JsonUtil.isArrayNode(node6)

        where:
        node6                                    | result6
        new ObjectNode(JsonNodeFactory.instance) | false
        new ArrayNode(JsonNodeFactory.instance)  | true
        null                                     | false
    }

    def "isValidValueNode"(node7, result7) {
        expect:
        result7 == JsonUtil.isValidValueNode(node7)

        where:
        node7                                    | result7
        new ObjectNode(JsonNodeFactory.instance) | false
        new ArrayNode(JsonNodeFactory.instance)  | false
        new TextNode("")                         | false
        new TextNode("x")                        | true
        new BooleanNode(false)                   | true
        JsonNodeFactory.instance.numberNode(0)   | true
        null                                     | false
    }

    def "convertNameValuePairsToMap"() {
        given:
        def arrayNode = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "quantity").put(PFXConstants.FIELD_VALUE, 10).set(PFXConstants.FIELD_VALUEOBJECT, new ObjectNode(JsonNodeFactory.instance)),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config").set(PFXConstants.FIELD_VALUEOBJECT, new ObjectNode(JsonNodeFactory.instance).put("test", true)),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, (String) null).put(PFXConstants.FIELD_VALUE, 11),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "name").put(PFXConstants.FIELD_VALUE, (String) null))

        when:
        def result = JsonUtil.convertNameValuePairsToMap(arrayNode)

        then:
        10 == result.get("quantity").numberValue()
        true == result.get("config").get("test").asBoolean()
        result.get("name").isNull()
    }

    def "updateInputs"() {
        given:
        def newValues = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "quantity").put(PFXConstants.FIELD_VALUE, "10").set(PFXConstants.FIELD_VALUEOBJECT, new ObjectNode(JsonNodeFactory.instance)),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config").set(PFXConstants.FIELD_VALUEOBJECT, new ObjectNode(JsonNodeFactory.instance).put("test", true)),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, (String) null).put(PFXConstants.FIELD_VALUE, 11),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "name").put(PFXConstants.FIELD_VALUE, (String) null),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "flag").put(PFXConstants.FIELD_VALUE, "true"),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "label").put(PFXConstants.FIELD_VALUE, "hello"),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "numericstring").put(PFXConstants.FIELD_VALUE, 2),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config2").set(PFXConstants.FIELD_VALUEOBJECT, new ObjectNode(JsonNodeFactory.instance).put("test", "hello")))

        def inputNode = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "quantity").put(PFXConstants.FIELD_VALUE, 100)
                        .put("label", "QTY"),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config").put(PFXConstants.FIELD_VALUE, (String) null),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "dummy").put(PFXConstants.FIELD_VALUE, "dummy"),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "name").put(PFXConstants.FIELD_VALUE, "xxxxx"),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "flag").put(PFXConstants.FIELD_VALUE, false),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "label").put(PFXConstants.FIELD_VALUE, "world"),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "numericstring").put(PFXConstants.FIELD_VALUE, "1"),
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config2").set(PFXConstants.FIELD_VALUE, new ObjectNode(JsonNodeFactory.instance).put("test", "world"))
        )

        when:
        JsonUtil.updateInputs(inputNode, newValues)

        then:
        10 == inputNode.get(0).get(PFXConstants.FIELD_VALUE).numberValue()
        "QTY" == inputNode.get(0).get("label").textValue()

        true == inputNode.get(1).get(PFXConstants.FIELD_VALUE).get("test").asBoolean()

        "dummy" == inputNode.get(2).get(PFXConstants.FIELD_VALUE).textValue()

        null == inputNode.get(3).get(PFXConstants.FIELD_VALUE).textValue()
        true == inputNode.get(4).get(PFXConstants.FIELD_VALUE).booleanValue()

        "hello" == inputNode.get(5).get(PFXConstants.FIELD_VALUE).textValue()

        "2" == inputNode.get(6).get(PFXConstants.FIELD_VALUE).textValue()

        "hello" == inputNode.get(7).get(PFXConstants.FIELD_VALUE).get("test").textValue()


        when:
        newValues = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config3").set(PFXConstants.FIELD_VALUEOBJECT,
                        new ObjectNode(JsonNodeFactory.instance).put("test", "hello")))

        inputNode = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config3").set(PFXConstants.FIELD_VALUE,
                        new ArrayNode(JsonNodeFactory.instance)))


        JsonUtil.updateInputs(inputNode, newValues)

        then:
        thrown(RequestValidationException.class)
    }

    def "getStringArray from list of nodes "() {
        given:
        def nodes = [new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "attribute1").put(PFXConstants.FIELD_LABEL, "attribute1 label"),
                     new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "attribute2").put(PFXConstants.FIELD_LABEL, "attribute2 label")]

        when:
        def results = JsonUtil.getStringArray(nodes, PFXConstants.FIELD_NAME)

        then:
        2 == results.size()
        "attribute1" == results.get(0)
        "attribute2" == results.get(1)

        when:
        results = JsonUtil.getStringArray(nodes, null)

        then:
        results.isEmpty()


    }

    def "getLabelTranslations"() {
        when:
        def result = JsonUtil.getLabelTranslations("{\"value\":\" Hello\\n\\rWorld \"}")

        then:
        "HelloWorld" == result

        when:
        result = JsonUtil.getLabelTranslations("x")

        then:
        !result

    }

    def "getBooleanValue"() {
        when:
        def result = JsonUtil.getBooleanValue(new BooleanNode(true))

        then:
        true == result

        when:
        result = JsonUtil.getBooleanValue(new NullNode())

        then:
        false == result
    }

    def "findDuplicates"() {
        given:
        ObjectNode node1 = new ObjectNode(JsonNodeFactory.instance)
                .put("x", "a").put("y", "y1")


        ObjectNode node2 = new ObjectNode(JsonNodeFactory.instance)
                .put("x", "b").put("y", "y2")


        ObjectNode node3 = new ObjectNode(JsonNodeFactory.instance)
                .put("x", "b").put("y", "y2")
        ArrayNode arr = JsonUtil.createArrayNode(node1, node2, node3)

        when:
        def result = JsonUtil.findDuplicates(arr, "x")

        then:
        [b: 2] == result
    }


}