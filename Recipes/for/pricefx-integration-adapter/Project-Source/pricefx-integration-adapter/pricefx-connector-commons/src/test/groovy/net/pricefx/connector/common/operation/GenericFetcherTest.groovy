package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.connection.MockDummyPFXOperationClient
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.*
import net.pricefx.connector.common.validation.RequestValidationException
import spock.lang.Specification

import static net.pricefx.connector.common.util.ConnectionUtil.createPath
import static net.pricefx.connector.common.util.PFXOperation.PRICE_LIST_ITEM_FETCH

class GenericFetcherTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def pfxDummyClient = new MockDummyPFXOperationClient()
    def requestFile = "/fetch-request.json"

    def "constructor"() {
        when:
        new GenericFetcher(pfxClient, "DMF", null, false)

        then:
        thrown(RequestValidationException.class)

        when:
        new GenericFetcher(pfxClient, "1.X", null, false)

        then:
        noExceptionThrown()

        when:
        new GenericFetcher(pfxClient, "1.DMF", null, false)

        then:
        noExceptionThrown()

        when:
        def result = new GenericFetcher(pfxClient, "1234.XPLI", "7978", false)

        then:
        createPath(PRICE_LIST_ITEM_FETCH.getOperation(), "7978") == result.getApiPath()
    }

    def "validateCriteria"() {
        when:
        def result = new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_CRITERIA, "x"))

        then:
        !result

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put("operator", "and").put(PFXConstants.FIELD_CRITERIA, "x"), true, PFXJsonSchema.FILTER_REQUEST)

        then:
        thrown(RequestValidationException.class)

        when:
        result = new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).set(PFXConstants.FIELD_CRITERIA,
                        new ArrayNode(JsonNodeFactory.instance).add("x")))

        then:
        !result

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put("operator", "and").set(PFXConstants.FIELD_CRITERIA,
                        new ArrayNode(JsonNodeFactory.instance).add("x")), true, PFXJsonSchema.FILTER_REQUEST)

        then:
        thrown(RequestValidationException.class)

        when:
        result = new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).set(PFXConstants.FIELD_CRITERIA,
                        JsonUtil.createArrayNode(
                                new ObjectNode(JsonNodeFactory.instance)
                                        .put(PFXConstants.FIELD_FIELDNAME, (String) null)
                                        .put("operator", "startsWith")
                                        .put(PFXConstants.FIELD_VALUE, "USER"))))


        then:
        !result


        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put("operator", "and").set(PFXConstants.FIELD_CRITERIA,
                        JsonUtil.createArrayNode(
                                new ObjectNode(JsonNodeFactory.instance)
                                        .put(PFXConstants.FIELD_FIELDNAME, (String) null)
                                        .put("operator", "startsWith")
                                        .put(PFXConstants.FIELD_VALUE, "USER"))), true, PFXJsonSchema.FILTER_REQUEST)


        then:
        thrown(RequestValidationException.class)

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put("operator", "and").set(PFXConstants.FIELD_CRITERIA,
                        JsonUtil.createArrayNode(
                                new ObjectNode(JsonNodeFactory.instance)
                                        .put(PFXConstants.FIELD_FIELDNAME, "loginName")
                                        .put("operator", "startsWithX")
                                        .put(PFXConstants.FIELD_VALUE, "USER"))), true, PFXJsonSchema.FILTER_REQUEST)


        then:
        thrown(UnsupportedOperationException.class)

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put("operator", "and").set(PFXConstants.FIELD_CRITERIA,
                        JsonUtil.createArrayNode(
                                new ObjectNode(JsonNodeFactory.instance)
                                        .put(PFXConstants.FIELD_FIELDNAME, "loginName")
                                        .put("operator", "inSet")
                                        .put(PFXConstants.FIELD_VALUE, "USER"))), true, PFXJsonSchema.FILTER_REQUEST)


        then:
        thrown(UnsupportedOperationException.class)

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put("operator", "x").set(PFXConstants.FIELD_CRITERIA,
                        JsonUtil.createArrayNode(
                                new ObjectNode(JsonNodeFactory.instance)
                                        .put(PFXConstants.FIELD_FIELDNAME, "loginName")
                                        .put("operator", "startsWith")
                                        .put(PFXConstants.FIELD_VALUE, "USER"))), true, PFXJsonSchema.FILTER_REQUEST)


        then:
        thrown(UnsupportedOperationException.class)

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).validateCriteria(
                new ObjectNode(JsonNodeFactory.instance).put("operator", "and").set(PFXConstants.FIELD_CRITERIA,
                        JsonUtil.createArrayNode(
                                new ObjectNode(JsonNodeFactory.instance)
                                        .put(PFXConstants.FIELD_FIELDNAME, "loginName")
                                        .put("operator", "startsWith")
                                        .put(PFXConstants.FIELD_VALUE, "USER"))), true, PFXJsonSchema.FILTER_REQUEST)


        then:
        noExceptionThrown()
    }

    def "getById"() {
        when:
        def result = new GenericFetcher(pfxClient, "123.P", null, false).getById()

        then:
        "123" == result.get(PFXConstants.FIELD_SKU).textValue()

        when:
        result = new GenericFetcher(pfxDummyClient, "123.P", null, false).getById()

        then:
        null == result

    }

    def "fetch - Formatted"() {
        given:
        def request = new ObjectMapper().readTree(GenericFetcherTest.class.getResourceAsStream(requestFile))

        when:
        def result = new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)


        then:
        "test" == result.get(0).get(PFXConstants.FIELD_SKU).textValue()

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.DATAFEED, null, "1.DMF", false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)


        then:
        "test" == result.get(0).get(PFXConstants.FIELD_SKU).textValue()


        when:
        ((ArrayNode) request.get("sortBy")).add("type")
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)

        then:
        thrown(RequestValidationException.class)

        when:
        new GenericFetcher(pfxClient,
                PFXTypeCode.DATAFEED, null, "1.DMF", false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)


        then:
        noExceptionThrown()

        when:
        ((ObjectNode) request).remove("sortBy")
        new GenericFetcher(pfxClient,
                PFXTypeCode.PRODUCT, null, null, false).
                fetch(request.deepCopy(), 0L, Constants.MAX_RECORDS, true, true)

        then:
        thrown(RequestValidationException.class)

    }

    def "fetchRaw"() {
        given:
        def request = new ObjectMapper().readTree(GenericFetcherTest.class.getResourceAsStream(requestFile))


        when:
        def results = new GenericFetcher(pfxClient,
                PFXTypeCode.DATAFEED, null, "1.DMF", false).
                fetchRaw(request.deepCopy(), "fetch/MPL")

        then:
        1 == results.size()
        "1.MPL" == results.get(0).get(PFXConstants.FIELD_TYPEDID).textValue()

    }


}
