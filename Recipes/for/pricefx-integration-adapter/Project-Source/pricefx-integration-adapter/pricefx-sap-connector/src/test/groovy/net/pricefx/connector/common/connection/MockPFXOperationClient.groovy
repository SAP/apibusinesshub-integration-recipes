package net.pricefx.connector.common.connection

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.collect.ImmutableList
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.PFXConstants
import net.pricefx.connector.common.util.PFXOperation
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.pckg.client.okhttp.FileUploadProvider

import java.util.function.Function

import static net.pricefx.connector.common.util.PFXConstants.FIELD_UNIQUENAME
import static net.pricefx.connector.common.util.PFXConstants.FIELD_VALUE
import static net.pricefx.connector.common.util.PFXOperation.*

class MockPFXOperationClient extends PFXOperationClient {

    MockPFXOperationClient(Builder builder) {
        super(builder, true)
    }

    @Override
    ObjectNode getAccessToken(String apiPath, Object body) throws IOException {

        switch (findPFXOperation(apiPath)) {
            case TOKEN:
            case REFRESH_TOKEN:
                return new ObjectNode(JsonNodeFactory.instance)
                        .put("access-token", "dummy-access-token")
                        .put("refresh-token", "dummy-refresh-token")


        }

        return new ObjectNode(JsonNodeFactory.instance)
    }

    @Override

    @Override
    ObjectNode post(String apiPath, Object body, Function<Exception, RuntimeException> exceptionFunction) {
        return super.post(apiPath, body, exceptionFunction)
    }

    @Override
    void logout() {
        close()
    }

    @Override
    void doUpload(FileUploadProvider upload, PFXOperation operation, String apiPath, Map<String, String> queryParams) {

    }

    @Override
    ArrayNode doPostBatch(String apiPath, ArrayNode request) {
        return request
    }

    @Override
    JsonNode doPost(String apiPath, ObjectNode request) {
        switch (findPFXOperation(apiPath)) {
            case EXECUTE_FORMULA:
                return JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put("resultName", "finalResult").set("result", request))

            case SAVE_QUOTE:
                return new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance)
                        .put(PFXConstants.FIELD_UNIQUENAME,
                                "TEST")
                        .put("version", "0"))


            case LOOKUPTABLE_FETCH:

                def dataNode = new ArrayNode(JsonNodeFactory.instance)
                String tableName = request.get(PFXConstants.FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue()
                if (tableName == "RangeParameters") {
                    dataNode.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "RangeParameters")
                            .put(PFXConstants.FIELD_TYPE, "RANGE").put(PFXConstants.FIELD_VALUETYPE, "NUMBER").put(PFXConstants.FIELD_ID, 1))
                } else if (tableName == "MatrixParameters") {
                    dataNode.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "MatrixParameters")
                            .put(PFXConstants.FIELD_TYPE, "MATRIX").put(PFXConstants.FIELD_VALUETYPE, "MATRIX2").put(PFXConstants.FIELD_ID, 2))
                } else if (tableName == "MatrixSingleParameters") {
                    dataNode.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "MatrixSingleParameters")
                            .put(PFXConstants.FIELD_TYPE, "MATRIX").put(PFXConstants.FIELD_VALUETYPE, "MATRIX").put(PFXConstants.FIELD_ID, 3))
                }
                return dataNode

            case FETCH:
                if (apiPath.contains("/" + PFXTypeCode.USER.getTypeCode())) {
                    return new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_USER_LOGINNAME,
                            request.get("criteria").get(0).get(FIELD_VALUE).textValue()).put(PFXConstants.FIELD_TYPEDID, "1.U")
                }
                return new ArrayNode(JsonNodeFactory.instance).add(new ObjectNode(JsonNodeFactory.instance).
                        put(PFXConstants.FIELD_TYPEDID, "1." + PFXTypeCode.MANUALPRICELIST.getTypeCode()).
                        put(FIELD_UNIQUENAME, "test"))
            case DELETE:
                if (request.get("filterCriteria") != null) {
                    return new ArrayNode(JsonNodeFactory.instance).add("1")
                } else {
                    return new ArrayNode(JsonNodeFactory.instance).add(request)
                }
            case SUBMIT_QUOTE:
                return new ObjectNode(JsonNodeFactory.instance).put("workflowStatus", "SUBMITTED")
            default:
                return new ObjectNode(JsonNodeFactory.instance)
        }
    }

    @Override
    Iterable<ObjectNode> doAction(String apiPath) {
        switch (findPFXOperation(apiPath)) {
            case PFXOperation.FETCH_QUOTE:
                return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME,
                        apiPath.split("/")[1])
                        .put("version", "0"))
            case PFXOperation.CUSTOMEREXTENSION_LIST:
                return ImmutableList.of(
                        JsonUtil.FACTORY.objectNode().put(FIELD_VALUE, "{\n    \"Customer_Details\":{\n        \"label\":\"Customer Details\", \n        \"userGroupEdit\":null, \n        \"userGroupViewDetails\":null, \n        \"allowSearch\":false, \n        \"allowPASearch\":false, \n        \"businessKey\":[\n            \"customerId\"\n        ], \n        \"numberOfAttributes\":3\n    }\n}"))
            case PING:
                return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put("partition", "test"))
            case PRODUCT_IMAGE_DELETE:
                return ImmutableList.of(
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_VALUE, "1"))
            default:
                return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance))
        }

    }

    @Override
    Iterable<ObjectNode> doFetch(PFXTypeCode typeCode, String apiPath, ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize) {
        if (apiPath.contains("/" + PFXTypeCode.PRODUCT.getMetadataTypeCode())) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).put("fieldName", "attribute1")
                            .put("label", "Length").put("fieldType", 1))
        } else {
            switch (typeCode) {
                case PFXTypeCode.ROLE:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(
                            PFXConstants.FIELD_UNIQUENAME, "PB_PRICELISTS"))

                case PFXTypeCode.DATAFEED:
                case PFXTypeCode.QUOTE:
                    if (advancedCriteria != null && advancedCriteria.get("criteria") != null) {
                        return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME,
                                advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).textValue())
                                .put("version", "0"))
                    } else {
                        return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME,
                                "TEST")
                                .put("version", "0"))
                    }
                case PFXTypeCode.PRODUCT:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU,
                            advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).textValue())
                            .put("version", "0"))
                case PFXTypeCode.LOOKUPTABLE:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance)
                            .put("type", "SIMPLE")
                            .put("valueType", "STRING").put(PFXConstants.FIELD_NAME,
                            advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).textValue())
                            .put("version", "0"))
                default:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put("partition", "test"))
            }
        }


    }

    @Override
    void testConnection() {
        testConnection(getPartition(), true)
    }

    @Override
    String getUploadSlot() {
        return "1000"
    }
}
