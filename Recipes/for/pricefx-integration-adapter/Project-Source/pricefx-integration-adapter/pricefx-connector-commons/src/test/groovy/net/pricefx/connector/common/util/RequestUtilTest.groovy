package net.pricefx.connector.common.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.google.common.collect.ImmutableList
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion

class RequestUtilTest extends Specification {
    private ObjectMapper mapper = new ObjectMapper()

    def "buildSimpleCriterion"() {
        when:
        def result = RequestUtil.buildSimpleCriterion(PFXConstants.FIELD_SKU, OperatorId.NOT_NULL.getValue())

        then:
        PFXConstants.FIELD_SKU == result.get(PFXConstants.FIELD_FIELDNAME).textValue()
        OperatorId.NOT_NULL.getValue() == result.get("operator").textValue()
    }


    def "getPageSize"() {
        when:
        def result = RequestUtil.getPageSize(null, Constants.MAX_RECORDS)

        then:
        Constants.MAX_RECORDS == result

        when:
        result = RequestUtil.getPageSize("100", Constants.MAX_RECORDS)

        then:
        100 == result

        when:
        RequestUtil.getPageSize("x", Constants.MAX_RECORDS)

        then:
        thrown(RuntimeException.class)

        when:
        RequestUtil.getPageSize("501", Constants.MAX_RECORDS)

        then:
        thrown(RuntimeException.class)

        when:
        RequestUtil.getPageSize("-1", Constants.MAX_RECORDS)

        then:
        thrown(RuntimeException.class)
    }


    def "getStartRow"(page, pageSize, result) {
        expect:
        result == RequestUtil.getStartRow(page, pageSize)

        where:
        page | pageSize | result
        "1"  | 100      | 0
        null | 100      | 0
        null | 100      | 0
        "1"  | 100      | 0
        "2"  | 100      | 100
    }

    def "getStartRow Failed case"() {

        when:
        RequestUtil.getStartRow("x", 100)

        then:
        thrown(RuntimeException.class)

        when:
        RequestUtil.getStartRow("-1", 100)

        then:
        thrown(RuntimeException.class)

    }


    def "createSimpleFetchRequest"() {
        given:
        def expected = "{" +
                "\"operator\":\"and\", " +
                "\"_constructor\":\"AdvancedCriteria\", " +
                "\"criteria\":[" +
                "{" +
                "\"fieldName\":\"uniqueName\"," +
                "\"operator\":\"equals\"," +
                "\"value\":" +
                "\"P-1\"" +
                "}" +
                "" +
                "]" +
                "}"

        expected = mapper.readTree(expected)

        when:
        List<ObjectNode> criterion = ImmutableList.of(
                buildSimpleCriterion(PFXConstants.FIELD_UNIQUENAME, OperatorId.EQUALS.getValue(), "P-1"))

        def result = RequestUtil.createSimpleFetchRequest(OperatorId.AND.getValue(), criterion)

        then:
        expected.equals(result)
    }

    def "createSimpleFetchRequest empty criterions"() {
        given:
        def expected = "{" +
                "\"operator\":\"and\", " +
                "\"_constructor\":\"AdvancedCriteria\", " +
                "\"criteria\":[" +
                "]" +
                "}"

        expected = mapper.readTree(expected)

        when:
        def result = RequestUtil.createSimpleFetchRequest(OperatorId.AND.getValue(), new ArrayList<>())

        then:
        expected == result
    }


    def "createSimpleDataRequest (map)"() {
        given:
        def map = [type: "TRUNCATE", version: 0, targetName: "DM.Product", test: true]

        when:
        JsonNode result = RequestUtil.createSimpleDataRequest(map)

        then:
        result.get(PFXConstants.FIELD_DATA) != null
        result.get(PFXConstants.FIELD_DATA).isObject()

        "TRUNCATE" == result.get(PFXConstants.FIELD_DATA).get(PFXConstants.FIELD_TYPE).textValue()
        result.get(PFXConstants.FIELD_DATA).get("version").intValue() == 0
        result.get(PFXConstants.FIELD_DATA).get("test").booleanValue() == true
        "DM.Product" == result.get(PFXConstants.FIELD_DATA).get("targetName").textValue()

    }

    def "createSimpleDataRequest (criterions)"() {
        given:
        def map = [incremental: Boolean.class, version: Number.class, date: Date.class]


        when:
        def criterions = [
                buildSimpleCriterion(PFXConstants.FIELD_LABEL, OperatorId.EQUALS.getValue(), "Product"),
                buildSimpleCriterion("incremental", OperatorId.EQUALS.getValue(), "true"),
                buildSimpleCriterion("date", OperatorId.EQUALS.getValue(), "2020-01-01"),
                buildSimpleCriterion("version", OperatorId.EQUALS.getValue(), "1")
        ]
        JsonNode result = RequestUtil.createSimpleDataRequest(criterions, map)

        then:
        result.get(PFXConstants.FIELD_DATA) != null
        result.get(PFXConstants.FIELD_DATA).isObject()

        "Product" == result.get(PFXConstants.FIELD_DATA).get(PFXConstants.FIELD_LABEL).textValue()
        "2020-01-01" == result.get(PFXConstants.FIELD_DATA).get("date").textValue()
        result.get(PFXConstants.FIELD_DATA).get("incremental").booleanValue()
        result.get(PFXConstants.FIELD_DATA).get("version").intValue() == 1

        when:
        criterions = [
                buildSimpleCriterion(PFXConstants.FIELD_LABEL, OperatorId.EQUALS.getValue(), "Product"),
                buildSimpleCriterion("incremental", OperatorId.EQUALS.getValue(), "true"),
                buildSimpleCriterion("date", OperatorId.EQUALS.getValue(), "2020-01-01"),
                buildSimpleCriterion("version", OperatorId.EQUALS.getValue(), "1x")
        ]
        RequestUtil.createSimpleDataRequest(criterions, map)

        then:
        thrown(RequestValidationException.class)


    }

    def "isOperatorSupported"(operator, result1) {
        expect:
        result1 == RequestUtil.isOperatorSupported(operator)

        where:
        operator                   | result1
        OperatorId.ENDS_WITH.value | true
        OperatorId.BETWEEN.value   | true
        OperatorId.BETWEEN_INCLUSIVE.value   | true
        null                       | false
    }

    def "isValidValue"(operator, node, start, end, result2) {
        expect:
        result2 == RequestUtil.isValidValue(operator, node, start, end)

        where:
        operator                 | node                                                | start| end | result2
        OperatorId.EQUALS.value  | null                                                | null | null |false
        OperatorId.EQUALS.value  | MissingNode.instance                                | null | null |false
        OperatorId.EQUALS.value  | new TextNode("xxx") | null | null |true
        OperatorId.IS_NULL.value | null                                                | null | null |true
        OperatorId.IN_SET.value  | null                                                | null | null |false
        OperatorId.IN_SET.value  | new ObjectNode(JsonNodeFactory.instance)            | null | null |false
        OperatorId.IN_SET.value  | new ArrayNode(JsonNodeFactory.instance)             | null | null |false
        OperatorId.IN_SET.value  | new ArrayNode(JsonNodeFactory.instance).add("test") | null | null |true
        "xx"                     | null                                                | null | null |false
        OperatorId.BETWEEN.value  | null                                               | new TextNode("1") | new TextNode("2")  |true
        OperatorId.BETWEEN_INCLUSIVE.value  | null                                     | new TextNode("1")  | null |true
        OperatorId.BETWEEN_INCLUSIVE.value  | null                                     | null | null |false
        OperatorId.BETWEEN.value  | null                                               | null | new TextNode("2")  |true
    }

    def "getOperatorId"(operator, result3) {
        expect:
        result3 == RequestUtil.getOperatorId(operator)

        where:
        operator                | result3
        null                    | null
        "x"                     | null
        OperatorId.EQUALS.value | OperatorId.EQUALS
    }

    def "isValidUrl"() {
        when:
        def result = RequestUtil.isValidUrl("https://www.google.com")

        then:
        result

        when:
        result = RequestUtil.isValidUrl("www.google.com")

        then:
        !result

    }

    def "validate"() {
        when:
        RequestUtil.validateExtensionType(PFXTypeCode.PRODUCT, null)

        then:
        noExceptionThrown()

        when:
        RequestUtil.validateExtensionType(PFXTypeCode.PRODUCTEXTENSION, null)

        then:
        thrown(RuntimeException.class)

        when:
        RequestUtil.validateExtensionType(PFXTypeCode.PRODUCTEXTENSION, new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION))

        then:
        thrown(RuntimeException.class)

        when:
        RequestUtil.validateExtensionType(PFXTypeCode.LOOKUPTABLE, null)

        then:
        thrown(RuntimeException.class)

        when:
        def matrix = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name())

        RequestUtil.validateExtensionType(PFXTypeCode.LOOKUPTABLE, matrix)

        then:
        thrown(RuntimeException.class)

        when:
        matrix = PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()).withTable("xx")
        RequestUtil.validateExtensionType(PFXTypeCode.LOOKUPTABLE, matrix)

        then:
        noExceptionThrown()

    }

}