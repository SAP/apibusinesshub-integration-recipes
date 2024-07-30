package net.pricefx.connector.common.connection

import com.fasterxml.jackson.databind.node.*
import com.google.common.collect.ImmutableList
import com.smartgwt.client.types.OperatorId
import net.pricefx.connector.common.util.*
import net.pricefx.connector.common.validation.RequestValidationException
import net.pricefx.pckg.client.okhttp.PfxCommonService
import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

import static com.smartgwt.client.types.OperatorId.AND

class RequestFactoryTest extends Specification {


    def "buildDeleteRequest"() {
        given:
        def criterion = PfxCommonService.buildSimpleCriterion(PFXConstants.FIELD_UNIQUENAME, OperatorId.EQUALS.getValue(), "TEST")
        def criteria = RequestUtil.createSimpleFetchRequest(AND.getValue(), ImmutableList.of(criterion))
        def filter = new ObjectNode(JsonNodeFactory.instance).set("filterCriteria", criteria)

        when:
        def result = RequestFactory.buildDeleteRequest(PFXTypeCode.PRODUCT, filter, null)

        then:
        filter == result

        when:
        result = RequestFactory.buildDeleteRequest(
                PFXTypeCode.PRODUCTEXTENSION, filter,
                new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withTable("TESTING"))

        then:
        "TESTING" == result.get("filterCriteria").get("criteria").last().get("criteria").first()?.value?.textValue()
        criteria == result.get("filterCriteria").get("criteria").first()

    }

    def "buildBulkLoadRequest"() {
        given:
        def header = new ArrayNode(JsonNodeFactory.instance).add(PFXConstants.FIELD_SKU).add(PFXConstants.FIELD_LABEL)
        def data = new ArrayNode(JsonNodeFactory.instance).add(new ArrayNode(JsonNodeFactory.instance).add("TEST").add("ABC")).add(new ArrayNode(JsonNodeFactory.instance).add("TEST2").add("ABC2"))
        ObjectNode request = JsonUtil.buildObjectNode(Pair.of("header", header), Pair.of(PFXConstants.FIELD_DATA, data))

        when:
        def result = RequestFactory.buildBulkLoadRequest(PFXTypeCode.PRODUCT, request, null)

        then:
        request == result

        when:
        result = RequestFactory.buildBulkLoadRequest(PFXTypeCode.PRODUCTEXTENSION, request,
                new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION)
                        .withBusinessKeys([PFXConstants.FIELD_SKU, PFXConstants.FIELD_LABEL, PFXConstants.FIELD_LINEID,PFXConstants.FIELD_CUSTOMER_ID])
                        .withTable("TEST"))

        then:
        PFXConstants.FIELD_NAME == result.get("header").last()?.textValue()
        "TEST" == result.get(PFXConstants.FIELD_DATA).get(0).last()?.textValue()
        "TEST" == result.get(PFXConstants.FIELD_DATA).get(1).last()?.textValue()

        "TEST" == result.get(PFXConstants.FIELD_DATA).get(0).first()?.textValue()
        "TEST2" == result.get(PFXConstants.FIELD_DATA).get(1).first()?.textValue()

        PFXConstants.FIELD_CUSTOMER_ID == result.get("options").get("joinFields").get(0).textValue()
        5 == result.get("options").get("joinFields").size()

        PFXConstants.FIELD_NAME == result.get("options").get("maxJoinFieldsLengths").get(0).get("joinField").textValue()
        204 == result.get("options").get("maxJoinFieldsLengths").get(0).get("maxLength").intValue()
        PFXConstants.FIELD_CUSTOMER_ID == result.get("options").get("maxJoinFieldsLengths").get(1).get("joinField").textValue()
        5 == result.get("options").get("maxJoinFieldsLengths").size()

        when:
        result = RequestFactory.buildBulkLoadRequest(PFXTypeCode.LOOKUPTABLE, request,
                PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()).withTable("TEST"))

        then:
        "lookupTable" == result.get("header").last()?.textValue()
        "TEST" == result.get(PFXConstants.FIELD_DATA).get(0).last()?.textValue()
        "TEST" == result.get(PFXConstants.FIELD_DATA).get(1).last()?.textValue()


        when:
        header = new ArrayNode(JsonNodeFactory.instance).add(PFXConstants.FIELD_VALUE).add("lowerBound").add("upperBound")
        data = new ArrayNode(JsonNodeFactory.instance).add(new ArrayNode(JsonNodeFactory.instance).add("VALUE1").add(1).add(2))
                .add(new ArrayNode(JsonNodeFactory.instance).add("VALUE2").add(3).add(4))
        request = JsonUtil.buildObjectNode(Pair.of("header", header), Pair.of(PFXConstants.FIELD_DATA, data))


        result = RequestFactory.buildBulkLoadRequest(PFXTypeCode.LOOKUPTABLE, request,
                PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableType.RANGE.name()).withTable("TEST"))

        then:
        "name" == result.get("header").last()?.textValue()
        "lookupTable" == result.get("header").get(3)?.textValue()

        "TEST" == result.get(PFXConstants.FIELD_DATA).get(0).get(3)?.textValue()
        "TEST" == result.get(PFXConstants.FIELD_DATA).get(1).get(3)?.textValue()

        "1-2" == result.get(PFXConstants.FIELD_DATA).get(0).last()?.textValue()
        "3-4" == result.get(PFXConstants.FIELD_DATA).get(1).last()?.textValue()


        when:
        result = RequestFactory.buildBulkLoadRequest(PFXTypeCode.DATASOURCE, request, null)

        then:
        true == result.get("options").get("direct2ds").booleanValue()


        when:
        header = new ArrayNode(JsonNodeFactory.instance).add(PFXConstants.FIELD_NAME).add("lowerBound").add("upperBound")
        data = new ArrayNode(JsonNodeFactory.instance).add(new ArrayNode(JsonNodeFactory.instance).add("VALUE1").add(1).add(2))
                .add(new ArrayNode(JsonNodeFactory.instance).add("VALUE2").add(3).add(4))
        request = JsonUtil.buildObjectNode(Pair.of("header", header), Pair.of(PFXConstants.FIELD_DATA, data))


        RequestFactory.buildBulkLoadRequest(PFXTypeCode.LOOKUPTABLE, request,
                PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.RANGE.name(), PFXLookupTableType.LookupTableType.RANGE.name()).withTable("TEST"))

        then:
        thrown(RequestValidationException.class)

    }

    def "buildFetchMetadataRequest"() {
        when:
        def result = RequestFactory.buildFetchMetadataRequest(PFXTypeCode.PRODUCT, null, null)

        then:
        !result

        when:
        result = RequestFactory.buildFetchMetadataRequest(PFXTypeCode.PRODUCTEXTENSION,
                new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withTable("TEST"), null)

        then:
        PFXConstants.FIELD_NAME == result.get(PFXConstants.FIELD_CRITERIA).get(0).get(PFXConstants.FIELD_FIELDNAME).textValue()
        "TEST" == result.get(PFXConstants.FIELD_CRITERIA).get(0).get(PFXConstants.FIELD_VALUE).textValue()

        when:
        result = RequestFactory.buildFetchMetadataRequest(PFXTypeCode.LOOKUPTABLE,
                PFXLookupTableType.valueOf(PFXLookupTableType.LookupTableType.MATRIX.name(), PFXLookupTableType.LookupTableType.MATRIX.name()).withTable("1234"), null)

        then:
        "lookupTableId" == result.get(PFXConstants.FIELD_CRITERIA).get(0).get(PFXConstants.FIELD_FIELDNAME).textValue()
        "1234" == result.get(PFXConstants.FIELD_CRITERIA).get(0).get(PFXConstants.FIELD_VALUE).textValue()

    }

    def "buildCreateRequest"() {
        given:
        def folders = new ArrayNode(JsonNodeFactory.instance).add("FOLDER1").add("FOLDER2")
        def inputs = JsonUtil.createArrayNode(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_NAME, "config")
                        .set("valueObject", new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_LABEL, "abc")))

        def lineItems = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "item1").set(PFXConstants.FIELD_INPUTS, inputs))
        ObjectNode request = JsonUtil.buildObjectNode(Pair.of("folders", folders),
                Pair.of(PFXConstants.FIELD_INPUTS, inputs), Pair.of("lineItems", lineItems)).put(PFXConstants.FIELD_LABEL, "ABC")

        when:
        def result = RequestFactory.buildCreateRequest(PFXTypeCode.PRODUCTEXTENSION, request.deepCopy())

        then:
        request == result

        when:
        result = RequestFactory.buildCreateRequest(PFXTypeCode.QUOTE, request.deepCopy())
        result = result.get("quote")

        then:
        2 == result.get("folders").size()
        "FOLDER1" == result.get("folders").get(0).get(PFXConstants.FIELD_LABEL).textValue()
        "FOLDER2" == result.get("folders").get(1).get(PFXConstants.FIELD_LABEL).textValue()

        result.get("folders").get(0).get("folder").booleanValue()
        result.get("folders").get(1).get("folder").booleanValue()

        result.get("folders").get(0).get("lineId").textValue()
        result.get("folders").get(1).get("lineId").textValue()

        null == result.get(PFXConstants.FIELD_INPUTS).get(0).get("valueObject")
        "abc" == result.get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).get(PFXConstants.FIELD_LABEL).textValue()

        null == result.get("lineItems").get(0).get(PFXConstants.FIELD_INPUTS).get(0).get("valueObject")
        "abc" == result.get("lineItems").get(0).get(PFXConstants.FIELD_INPUTS).get(0).get(PFXConstants.FIELD_VALUE).get(PFXConstants.FIELD_LABEL).textValue()

        when:
        def request2 = (ObjectNode) request.deepCopy()
        ((ObjectNode) request2.get(PFXConstants.FIELD_INPUTS).get(0)).put(PFXConstants.FIELD_VALUE, "def")

        RequestFactory.buildCreateRequest(PFXTypeCode.QUOTE, request2)

        then:
        thrown(RequestValidationException.class)

        when:
        request2 = (ObjectNode) request.deepCopy()
        ((ObjectNode) request2.get("lineItems").get(0).get(PFXConstants.FIELD_INPUTS).get(0)).put(PFXConstants.FIELD_VALUE, "def")
        RequestFactory.buildCreateRequest(PFXTypeCode.QUOTE, request2)

        then:
        thrown(RequestValidationException.class)

        when:
        request.remove("folders")
        result = RequestFactory.buildCreateRequest(PFXTypeCode.QUOTE, request)
        result = result.get("quote")

        then:
        0 == result.get("folders").size()

        when:
        result = RequestFactory.buildCreateRequest(PFXTypeCode.QUOTE, new ArrayNode(JsonNodeFactory.instance).add("x"))

        then:
        !result
    }

    def "buildAssignRoleRequests"() {
        when:
        def results = RequestFactory.buildAssignRoleRequest(null, true, "user")

        then:
        results.isEmpty()

        when:
        results = RequestFactory.buildAssignRoleRequest(["role1", "role2"], true, "user")

        then:
        2 == results.size()
        results.get(0).get("assign")
        results.get(1).get("assign")

        "role1" == results.get(0).get(PFXConstants.FIELD_UNIQUENAME).textValue()
        "role2" == results.get(1).get(PFXConstants.FIELD_UNIQUENAME).textValue()
    }

    def "buildAssignRoleRequests(list, list, string)"() {
        when:
        def results = RequestFactory.buildAssignRoleRequest(["role1", "role2"], ["role1", "role3"], "user")

        then:
        //first delete role2, then add role3
        2 == results.size()
        [new BooleanNode(false), new BooleanNode(true)] == results?.assign
        [new TextNode("role2"), new TextNode("role3")] == results?.uniqueName
    }

    def "buildUpsertRequest"() {
        given:
        def node = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue())
                .set("criteria", JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.NOT_NULL.getValue()).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_SKU)))
        def userNode = new ObjectNode(JsonNodeFactory.instance).put("loginName", "TEST").set("productFilterCriteria", node)
        def extensionType = new PFXExtensionType(PFXTypeCode.PRODUCTEXTENSION).withAttributes(6).withTable("TEST")

        when:
        def results = RequestFactory.buildUpsertRequest(PFXTypeCode.USER, null, userNode)

        then:
        results.get(0).get("productFilterCriteria").isTextual()
        results.get(0).get("productFilterCriteria").textValue().contains("AdvancedCriteria")

        when:
        results = RequestFactory.buildUpsertRequest(PFXTypeCode.USER, null, null)

        then:
        !results

        when:
        results = RequestFactory.buildUpsertRequest(PFXTypeCode.USER, null, new TextNode(null))

        then:
        !results

        when:
        results = RequestFactory.buildUpsertRequest(PFXTypeCode.USER, null, new ArrayNode(JsonNodeFactory.instance))

        then:
        !results

        when:
        results = RequestFactory.buildUpsertRequest(PFXTypeCode.PRODUCTEXTENSION, extensionType, new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU, "1234"))

        then:
        "1234" == results.get(0).get(PFXConstants.FIELD_SKU).textValue()
        "TEST" == results.get(0).get(PFXConstants.FIELD_NAME).textValue()

    }

}
