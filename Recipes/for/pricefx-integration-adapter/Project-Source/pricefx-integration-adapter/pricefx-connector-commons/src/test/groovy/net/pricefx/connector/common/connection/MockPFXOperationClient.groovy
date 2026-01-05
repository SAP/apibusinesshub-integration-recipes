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
        try {
            ArrayNode response = new ArrayNode(JsonNodeFactory.instance)
            if (request.get(0).get(FIELD_TYPEDID).textValue().contains(PFXTypeCode.CONDITION_RECORD.getTypeCode())) {
                for (JsonNode node : request) {
                    ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance)
                    objectNode.set(FIELD_RESPONSE, node)
                    response.add(objectNode)
                }
            }
            return response
        } catch (Exception ex) {
        }

        return request
    }

    @Override
    JsonNode doPostRaw(String apiPath, Object request) {
        ObjectNode responseNode = new ObjectNode(JsonNodeFactory.instance)


        switch (findPFXOperation(apiPath)) {
            case FETCH:
                responseNode.set(FIELD_DATA, JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                        put(FIELD_TYPEDID, "1." + PFXTypeCode.MANUALPRICELIST.getTypeCode()).
                        put(FIELD_UNIQUENAME, "test")))

                break
            default:
                responseNode.put("totalRows", 1)
                responseNode.set(FIELD_DATA, JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                        put(FIELD_UNIQUENAME, "test")))
                break


        }

        return new ObjectNode(JsonNodeFactory.instance).set(FIELD_RESPONSE, responseNode)

    }

    @Override
    JsonNode doPost(String apiPath, ObjectNode request) {
        switch (findPFXOperation(apiPath)) {

            case EXECUTE_FORMULA:
                return new ObjectMapper().readTree(MockPFXOperationClient.class.getResourceAsStream("/execute-formula-response.json"))
            case BULK_LOAD:
                return new ArrayNode(JsonNodeFactory.instance).add(request.get(FIELD_DATA).size())
            case UPDATE:
                if ("update/Q".equals(apiPath)) {
                    return request.get("quote")
                } else {
                    return request
                }
            case SUBMIT_QUOTE:
                return new ObjectNode(JsonNodeFactory.instance).put("workflowStatus", "SUBMITTED")
            case WITHDRAW_WORKFLOW:
                return new ObjectNode(JsonNodeFactory.instance).set("workflow",
                        new ObjectNode(JsonNodeFactory.instance).put("workflowStatus", "WITHDRAWN"))
            case CONVERT_QUOTE:
            case COPY_QUOTE:
            case REVISE_QUOTE:
            case SAVE_QUOTE:
                return new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME,
                        "P-1000")
                        .put("version", 0)
                        .set(FIELD_INPUTS, JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance)
                                .put(FIELD_NAME, "Qty").put(FIELD_VALUE, 1000)))
            case LOOKUPTABLE_FETCH:
                ArrayNode dataNode = new ArrayNode(JsonNodeFactory.instance)
                String tableName = request.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue()
                if (tableName == "RangeParameters") {
                    dataNode.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "RangeParameters")
                            .put(FIELD_TYPE, "RANGE").put(FIELD_VALUETYPE, "NUMBER").put(FIELD_ID, 1))
                } else if (tableName == "MatrixParameters") {
                    dataNode.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "MatrixParameters")
                            .put(FIELD_TYPE, "MATRIX").put(FIELD_VALUETYPE, "MATRIX2").put(FIELD_ID, 2))
                } else if (tableName == "MatrixSingleParameters") {
                    dataNode.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "MatrixSingleParameters")
                            .put(FIELD_TYPE, "MATRIX").put(FIELD_VALUETYPE, "MATRIX").put(FIELD_ID, 3))
                }
                return dataNode

            case FETCH:
                if ("fetch/CRCS".equals(apiPath)) {
                    return JsonUtil.createArrayNode(
                            new ObjectNode(JsonNodeFactory.instance)
                                    .put(FIELD_ID, 1)
                                    .put(FIELD_TYPEDID, "1.CRCS")
                                    .put(FIELD_UNIQUENAME, request.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())
                                    .put("keySize", 4))
                } else if ("fetch/U".equals(apiPath)) {
                    if ("DUMMY".equals(request.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())) {
                        return new ArrayNode(JsonNodeFactory.instance)
                    } else if ("DUMMY_NO_ID".equals(request.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())) {
                        return JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                                put(FIELD_USER_LOGINNAME, request.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue()))
                    } else {
                        return JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                                put(FIELD_TYPEDID, "1." + PFXTypeCode.USER.getTypeCode()).
                                put(FIELD_USER_LOGINNAME, request.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue()))
                    }
                } else if ("fetch/F".equals(apiPath)) {
                    if (request.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue() == "DUMMY") {
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
                return request
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
                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_TYPEDID, "1.DMF").set("fields",
                                JsonUtil.createArrayNode(
                                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "type").put("numeric", false).put("key", false),
                                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "sku").put("numeric", false).put("key", true),
                                        new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "label").put("numeric", false).put("key", false))))


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
                    workflowStatus = PFXConstants.WorkflowStatus.SUBMITTED
                } else if (uniqueName.equalsIgnoreCase("P-12345")) {
                    workflowStatus = PFXConstants.WorkflowStatus.WITHDRAWN
                }

                ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, uniqueName)
                        .put(FIELD_WORKFLOWSTATUS, workflowStatus.name())
                        .put("version", 0).put(FIELD_TYPEDID, uniqueName.replaceAll("P-", "") + ".Q")

                ArrayNode inputNode = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "Qty").put(FIELD_VALUE, 1000))
                objectNode.set(FIELD_INPUTS, inputNode)

                objectNode.set(FIELD_LINEITEMS, JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put("lineId", "lH22S1jS6MKG1Pt")
                                .set(FIELD_INPUTS, inputNode)))

                return ImmutableList.of(objectNode)
            case FETCH_RBA:

                String uniqueName = apiPath.replace(FETCH_RBA.getOperation() + "/", "")


                ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, uniqueName)
                        .put(FIELD_WORKFLOWSTATUS, "APPROVED")
                        .put("version", 0).put(FIELD_TYPEDID, uniqueName.replaceAll("R-", "") + ".RBA")

                ArrayNode inputNode = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "Qty").put(FIELD_VALUE, 1000))
                objectNode.set(FIELD_INPUTS, inputNode)

                objectNode.set(FIELD_LINEITEMS, JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put("clicId", 1)
                                .set(FIELD_INPUTS, inputNode)))

                return ImmutableList.of(objectNode)
            case FETCH_CONTRACT:

                String uniqueName = apiPath.replace(FETCH_CONTRACT.getOperation() + "/", "")


                ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, uniqueName)
                        .put(FIELD_WORKFLOWSTATUS, "APPROVED")
                        .put("version", 0).put(FIELD_TYPEDID, uniqueName.replaceAll("C-", "") + ".CT")

                ArrayNode inputNode = JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "Qty").put(FIELD_VALUE, 1000))
                objectNode.set(FIELD_INPUTS, inputNode)

                objectNode.set(FIELD_LINEITEMS, JsonUtil.createArrayNode(
                        new ObjectNode(JsonNodeFactory.instance).put("clicId", 1)
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
                                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "type"),
                                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "sku"),
                                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_NAME, "label"))))

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
        } else if (apiPath.contains(PFXTypeCode.REBATEAGREEMENT.getMetadataTypeCode())) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, ATTRIBUTE_EXT_PREFIX + "ProjectName")
                            .put(FIELD_LABEL, "ProjectName"))
        } else if (apiPath.contains(PFXTypeCode.ROLE.getMetadataTypeCode()) && typeCode == PFXTypeCode.ROLE) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME, "ADMIN")
                            .put(FIELD_LABEL, "Administration").put("module", "ADMIN"))
        } else if (apiPath.contains(PFXTypeCode.CONDITION_RECORD.getMetadataTypeCode())) {
            return ImmutableList.of(
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute9")
                            .put(FIELD_LABEL, "Length").put("fieldType", 3),
                    new ObjectNode(JsonNodeFactory.instance).put(FIELD_FIELDNAME, "attribute10")
                            .put(FIELD_LABEL, "size").put("requiredField", true))

        } else {
            switch (typeCode) {
                case PFXTypeCode.CONDITION_RECORD:
                    if (!apiPath.contains(PFXTypeCode.CONDITION_RECORD.getMetadataTypeCode())) {
                        return ImmutableList.of(
                                new ObjectNode(JsonNodeFactory.instance).put(FIELD_TYPEDID, "1.CRCI4").put(FIELD_VERSION, 1),
                                new ObjectNode(JsonNodeFactory.instance).put(FIELD_TYPEDID, "2.CRCI4").put(FIELD_VERSION, 2),
                                new ObjectNode(JsonNodeFactory.instance).put(FIELD_TYPEDID, "3.CRCI4").put(FIELD_VERSION, 3).
                                        put(FIELD_LASTUPDATEDATE, "2024-01-01T00:00:00"),
                                new ObjectNode(JsonNodeFactory.instance).put(FIELD_TYPEDID, "4.CRCI4").put(FIELD_VERSION, 1).
                                        put(FIELD_LASTUPDATEDATE, "9999-12-31T00:00:00"))
                    }
                    break
                case PFXTypeCode.ROLE:

                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME,
                            advancedCriteria.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).get(0).textValue())
                            .put("module", advancedCriteria.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).get(0).textValue())
                            .put(FIELD_LABEL, advancedCriteria.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).get(0).textValue()))

                case PFXTypeCode.QUOTE:
                    if (!apiPath.contains(PFXTypeCode.QUOTE.getMetadataTypeCode())) {
                        return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME,
                                advancedCriteria.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())
                                .put(FIELD_TYPEDID, "1000.Q")
                                .put(FIELD_VERSION, 0))
                    }
                    break
                case PFXTypeCode.REBATEAGREEMENT:
                    if (!apiPath.contains(PFXTypeCode.REBATEAGREEMENT.getMetadataTypeCode())) {
                        return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME,
                                advancedCriteria.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())
                                .put(FIELD_TYPEDID, "1000.RBA")
                                .put(FIELD_VERSION, 0))
                    }
                    break
                case PFXTypeCode.CONTRACT:
                    if (!apiPath.contains(PFXTypeCode.CONTRACT.getMetadataTypeCode())) {
                        return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(FIELD_UNIQUENAME,
                                advancedCriteria.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())
                                .put(FIELD_TYPEDID, "1000.CT")
                                .put(FIELD_VERSION, 0))
                    }
                    break
                case PFXTypeCode.PRODUCT:
                    if (!apiPath.contains(PFXTypeCode.PRODUCT.getMetadataTypeCode())) {
                        return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(FIELD_SKU,
                                advancedCriteria.get(FIELD_CRITERIA).get(0).get(FIELD_VALUE).textValue())
                                .put(FIELD_VERSION, 0))
                    }
                    break
                default:
                    return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance)
                            .put(FIELD_UNIQUENAME, "test").put(FIELD_VERSION, 1).put(FIELD_TYPEDID, "1." + typeCode.getTypeCode()))
            }
        }
        return null


    }

    @Override
    void testConnection() {
        testConnection(getPartition(), true)
    }

}
