package net.pricefx.connector.common.connection

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.collect.ImmutableList
import net.pricefx.connector.common.util.*
import net.pricefx.pckg.client.okhttp.FileUploadProvider

import java.util.function.Function

import static net.pricefx.connector.common.util.PFXConstants.*
import static net.pricefx.connector.common.util.PFXOperation.*

class MockPFXOperationClient extends PFXOperationClient {

    MockPFXOperationClient() {
        super(ConnectionUtil.getPFXClientBuilder("dummy", "http://dummy.com", "dummy"), true)
    }

    @Override
    ObjectNode post(String apiPath, Object body, Function<Exception, RuntimeException> exceptionFunction) {
        return super.post(apiPath, body, exceptionFunction)
    }

    @Override
    void logout() {

    }

    @Override
    void doUpload(FileUploadProvider upload, PFXOperation operation, String apiPath, Map<String, String> queryParams) {

    }

    @Override
    ArrayNode doPostBatch(String apiPath, ArrayNode request) {
        return request
    }

    @Override
    JsonNode doPostRaw(String apiPath, ObjectNode request) {
        ObjectNode responseNode = new ObjectNode(JsonNodeFactory.instance)


        switch (findPFXOperation(apiPath)) {
            case FETCH:
                responseNode.set(FIELD_DATA, JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                        put(PFXConstants.FIELD_TYPEDID, "1." + PFXTypeCode.MANUALPRICELIST.getTypeCode()).
                        put(FIELD_UNIQUENAME, "test")))

                break
            default:
                responseNode.set(FIELD_DATA, JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                        put(FIELD_UNIQUENAME, "test")))
                break


        }

        return new ObjectNode(JsonNodeFactory.instance).set("response", responseNode)

    }

    @Override
    JsonNode doPost(String apiPath, ObjectNode request) {
        switch (findPFXOperation(apiPath)) {

            case EXECUTE_FORMULA:
                return new ObjectMapper().readTree(MockPFXOperationClient.class.getResourceAsStream("/execute-formula-response.json"))
            case BULK_LOAD:
                return new ArrayNode(JsonNodeFactory.instance).add(request.get(FIELD_DATA).size())
                break
            case UPDATE:
                if ("update/Q".equals(apiPath)) {
                    return request.get("quote")
                }
                break
            case SUBMIT_QUOTE:
                return new ObjectNode(JsonNodeFactory.instance).put("workflowStatus", "SUBMITTED")
            case WITHDRAW_WORKFLOW:
                return new ObjectNode(JsonNodeFactory.instance).set("workflow",
                        new ObjectNode(JsonNodeFactory.instance).put("workflowStatus", "WITHDRAWN"))
            case CONVERT_QUOTE:
            case COPY_QUOTE:
            case REVISE_QUOTE:
            case SAVE_QUOTE:
                return new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME,
                        "P-1000")
                        .put("version", "0")
                        .set(FIELD_INPUTS, JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance)
                                .put(FIELD_NAME, "Qty").put(FIELD_VALUE, 1000)))
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
                if ("fetch/U".equals(apiPath)) {
                    if ("DUMMY".equals(request.get(PFXConstants.FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())) {
                        return new ArrayNode(JsonNodeFactory.instance)
                    } else if ("DUMMY_NO_ID".equals(request.get(PFXConstants.FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())) {
                        return JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                                put(FIELD_USER_LOGINNAME, request.get(PFXConstants.FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue()))
                    } else {
                        return JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                                put(PFXConstants.FIELD_TYPEDID, "1." + PFXTypeCode.USER.getTypeCode()).
                                put(FIELD_USER_LOGINNAME, request.get(PFXConstants.FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue()))
                    }
                } else if ("fetch/F".equals(apiPath)) {
                    if (request.get("criteria").get(0).get("value").textValue() == "DUMMY") {
                        return new ArrayNode(JsonNodeFactory.instance)
                    }
                    return JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                            put(FIELD_UNIQUENAME, "test").put(FIELD_VALIDAFTER, "2020-01-01")
                            .put(FIELD_TYPEDID, "1.F"))
                } else {
                    return JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                            put(FIELD_UNIQUENAME, "test"))
                }
            case DELETE:
                if (request.get("filterCriteria") != null) {
                    return new ArrayNode(JsonNodeFactory.instance).add("1")
                } else {
                    return JsonUtil.createArrayNode(request)
                }
            default:
                return new ObjectNode(JsonNodeFactory.instance)
        }
    }


    @Override
    Iterable<ObjectNode> doAction(String apiPath) {
        switch (findPFXOperation(apiPath)) {
            case GET_FORMULA_NAMES:
                return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).
                        put(FIELD_UNIQUENAME, "test").put(FIELD_VALIDAFTER, "2020-01-01")
                        .put(FIELD_TYPEDID, "1.F"))
            case LOOKUPTABLE_FETCH:

                return ImmutableList.of(
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "SimpleParameters").put(FIELD_ID, 1).put(FIELD_TYPE, "SIMPLE").put(FIELD_VALIDAFTER, "2020-01-01").put("status", "ACTIVE"),
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "RangeParameters").put(FIELD_ID, 1).put(FIELD_TYPE, "RANGE").put(FIELD_VALIDAFTER, "2020-01-01").put("status", "ACTIVE"),
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "MatrixParameters").put(FIELD_ID, 2).put(FIELD_TYPE, "MATRIX").put(FIELD_VALUETYPE, "MATRIX6").put(FIELD_VALIDAFTER, "2020-01-01").put("status", "ACTIVE"),
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "MatrixSingleParameters").put(FIELD_ID, 3).put(FIELD_TYPE, "MATRIX").put(FIELD_VALUETYPE, "MATRIX").put(FIELD_VALIDAFTER, "2020-01-01").put("status", "INACTIVE"),
                )

            case GET_FCS:
                return ImmutableList.of(
                        new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_TYPEDID, "1.DMF").set("fields",
                                JsonUtil.createArrayNode(
                                        new ObjectNode(JsonNodeFactory.instance).put("name", "type").put("numeric", false).put("key", false),
                                        new ObjectNode(JsonNodeFactory.instance).put("name", "sku").put("numeric", false).put("key", true),
                                        new ObjectNode(JsonNodeFactory.instance).put("name", "label").put("numeric", false).put("key", false))))


            case FILE_UPLOAD_SLOT:
                return ImmutableList.of(
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_ID, "1000"))
            case FILE_UPLOAD_SLOT_DELETE:
                return ImmutableList.of(
                        new ObjectNode(JsonNodeFactory.instance).put("0"))
            case FILE_UPLOAD_STATUS:
                if (apiPath.contains(FileUploadStatus.INPROGRESS.name())) {
                    return ImmutableList.of(
                            new ObjectNode(JsonNodeFactory.instance).put("status", FileUploadStatus.INPROGRESS.name())
                                    .put(FIELD_DATA,
                                            "[{\"name\":\"Product\",\"total\":2,\"loaded\":2,\"failed\":0,\"duplicates\":0,\"op\":\"Complete\"}]"))
                } else if (apiPath.contains("NOT_EXIST")) {
                    return null
                } else if (apiPath.contains("WRONG_DATA")) {
                    return ImmutableList.of(
                            new ObjectNode(JsonNodeFactory.instance).put("status", "POSTPROCESSING_DONE")
                                    .put(FIELD_DATA, "xxx"))

                } else {
                    return ImmutableList.of(
                            new ObjectNode(JsonNodeFactory.instance).put("status", "POSTPROCESSING_DONE")
                                    .put(FIELD_DATA,
                                            "[{\"name\":\"Product\",\"total\":2,\"loaded\":2,\"failed\":0,\"duplicates\":0,\"op\":\"Complete\"}]"))

                }

            case FETCH_WORKFLOW_DETAILS:
                return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).set("workflow",
                        new ObjectNode(JsonNodeFactory.instance).put("currentStepId", "12345")))
            case FETCH_QUOTE:

                String uniqueName = apiPath.replace(FETCH_QUOTE.getOperation() + "/", "")
                PFXConstants.WorkflowStatus workflowStatus = PFXConstants.WorkflowStatus.DRAFT
                if (uniqueName.equalsIgnoreCase("P-10000")) {
                    workflowStatus == PFXConstants.WorkflowStatus.SUBMITTED
                } else if (uniqueName.equalsIgnoreCase("P-12345")) {
                    workflowStatus == PFXConstants.WorkflowStatus.WITHHDRAWN
                }

                ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME, uniqueName)
                        .put(PFXConstants.FIELD_WORKFLOWSTATUS, workflowStatus.name())
                        .put("version", "0").put(FIELD_TYPEDID, uniqueName.replaceAll("P-", "") + ".Q")

                ArrayNode inputNode = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "Qty").put(FIELD_VALUE, 1000))
                objectNode.set(FIELD_INPUTS, inputNode)

                objectNode.set(FIELD_LINEITEMS, JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put("lineId", "lH22S1jS6MKG1Pt")
                                .set(FIELD_INPUTS, inputNode)))

                return ImmutableList.of(objectNode)
            case CUSTOMEREXTENSION_LIST:
                return ImmutableList.of(
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_VALUE, "{\n    \"Customer_Details\":{\n        \"label\":\"Customer Details\", \n        \"userGroupEdit\":null, \n        \"userGroupViewDetails\":null, \n        \"allowSearch\":false, \n        \"allowPASearch\":false, \n        \"businessKey\":[\n            \"customerId\"\n        ], \n        \"numberOfAttributes\":3\n    }\n}"))
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
        if (findPFXOperation(apiPath) == GET_FCS) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).set("fields",
                            JsonUtil.createArrayNode(
                                    new ObjectNode(JsonNodeFactory.instance).put("name", "type"),
                                    new ObjectNode(JsonNodeFactory.instance).put("name", "sku"),
                                    new ObjectNode(JsonNodeFactory.instance).put("name", "label"))))

        } else if (apiPath.contains(PFXTypeCode.PRODUCT.getMetadataTypeCode())) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute1")
                            .put(FIELD_LABEL, "Length").put("fieldType", 1),
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute2")
                            .put(FIELD_LABEL, "size").put("requiredField", true))
        } else if (apiPath.contains(PFXTypeCode.QUOTE.getMetadataTypeCode())) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, ATTRIBUTE_EXT_PREFIX + "ProjectName")
                            .put(FIELD_LABEL, "ProjectName"))
        } else if (apiPath.contains(PFXTypeCode.ROLE.getMetadataTypeCode())) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "ADMIN")
                            .put(FIELD_LABEL, "Administration").put("module", "ADMIN"))
        } else {
            switch (typeCode) {
                case PFXTypeCode.ROLE:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME,
                            advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).get(0).textValue())
                            .put("module", advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).get(0).textValue())
                            .put("label", advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).get(0).textValue()))

                case PFXTypeCode.QUOTE:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_UNIQUENAME,
                            advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).textValue())
                            .put("typedId", "1000.Q")
                            .put("version", "0"))
                case PFXTypeCode.PRODUCT:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(PFXConstants.FIELD_SKU,
                            advancedCriteria.get("criteria").get(0).get(FIELD_VALUE).textValue())
                            .put("version", "0"))
                default:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance)
                            .put(FIELD_UNIQUENAME, "test").put("typedId", "1." + typeCode.getTypeCode()))
            }
        }


    }

    @Override
    void testConnection() {
        testConnection(getPartition(), true)
    }

}
