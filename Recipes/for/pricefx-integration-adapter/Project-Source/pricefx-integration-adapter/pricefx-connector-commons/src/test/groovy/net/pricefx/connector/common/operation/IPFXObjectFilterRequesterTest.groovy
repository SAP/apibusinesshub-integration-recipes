package net.pricefx.connector.common.operation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.smartgwt.client.types.OperatorId
import net.pricefx.connector.common.connection.MockPFXOperationClient
import net.pricefx.connector.common.util.*
import net.pricefx.connector.common.validation.RequestValidationException
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification

import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion

class IPFXObjectFilterRequesterTest extends Specification {
    def pfxClient = new MockPFXOperationClient()
    def requestFile = "/delete-request.json"


    def "validateCriteria"() {
        given:
        def request = new ObjectMapper().readTree(IPFXObjectFilterRequesterTest.class.getResourceAsStream(requestFile))

        when:
        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_NAME).put("operator", OperatorId.NOT_NULL.getValue())))


        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.ENDS_WITH.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_NAME).put("operator", OperatorId.NOT_NULL.getValue())))


        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        thrown(UnsupportedOperationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_NAME).put("operator", OperatorId.NOT_NULL.getValue())))


        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                new ArrayNode(JsonNodeFactory.instance))


        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, false, PFXJsonSchema.DELETE_REQUEST)

        then:
        noExceptionThrown()


        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue())

        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_NAME).put("operator", OperatorId.NOT_NULL.getValue())))


        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        thrown(RequestValidationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_FIELDNAME, PFXConstants.FIELD_NAME)
                                .put("operator", OperatorId.IN_SET.getValue()).set(PFXConstants.FIELD_VALUE, new ArrayNode(JsonNodeFactory.instance).add("xxx"))))

        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        noExceptionThrown()

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        buildSimpleCriterion(PFXConstants.FIELD_UNIQUENAME, OperatorId.NOT_EQUAL_FIELD.getValue(), "abcde")))
        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        thrown(UnsupportedOperationException.class)

        when:
        request = new ObjectNode(JsonNodeFactory.instance).put(RequestUtil.OPERATOR, OperatorId.AND.getValue()).set(
                PFXConstants.FIELD_CRITERIA,
                JsonUtil.createArrayNode(
                        buildSimpleCriterion(StringUtils.EMPTY, OperatorId.EQUALS.getValue(), "abcde")))
        new GenericDeleter(pfxClient, PFXTypeCode.PRODUCT, null).validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST)

        then:
        thrown(UnsupportedOperationException.class)


    }

}
