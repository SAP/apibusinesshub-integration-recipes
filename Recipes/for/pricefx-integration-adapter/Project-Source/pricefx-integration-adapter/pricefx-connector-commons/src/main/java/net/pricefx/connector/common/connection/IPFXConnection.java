package net.pricefx.connector.common.connection;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.smartgwt.client.types.OperatorId;
import net.pricefx.connector.common.operation.DataloadRunner;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import net.pricefx.pckg.client.okhttp.FileUploadProvider;
import net.pricefx.pckg.client.okhttp.PfxCommonService;
import okhttp3.MediaType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static com.smartgwt.client.types.OperatorId.AND;
import static com.smartgwt.client.types.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.*;
import static net.pricefx.connector.common.util.JsonUtil.getFirstDataNode;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.*;
import static net.pricefx.connector.common.util.RequestUtil.createSimpleFetchRequest;
import static net.pricefx.connector.common.validation.ConnectorException.ErrorType.TABLE_NOT_FOUND;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;

public interface IPFXConnection {

    void logout(boolean forcedLogout, boolean jwt, boolean oauth);

    default String getUploadSlot() {
        JsonNode node = Iterables.get(doAction(FILE_UPLOAD_SLOT.getOperation()), 0);
        return JsonUtil.getValueAsText(node.get(FIELD_ID));
    }

    void testConnection();

    default JsonNode action(String apiPath) {
        return Iterables.get(doAction(apiPath), 0);
    }

    Iterable<ObjectNode> doAction(String apiPath);

    JsonNode doPostRaw(String apiPath, ObjectNode request) throws IOException;

    ObjectNode post(String apiPath, Object body, Function<Exception, RuntimeException> exceptionFunction);

    default JsonNode post(ObjectNode request, String apiPath, PFXTypeCode typeCode, IPFXExtensionType extensionType) {
        if (typeCode != null) {
            RequestUtil.validateExtensionType(typeCode, extensionType);
        }

        return doPost(apiPath, request);
    }

    JsonNode doPost(String apiPath, ObjectNode request);

    default IPFXExtensionType createExtensionType(PFXTypeCode typeCode, String tableName, String validAfter) {
        if (typeCode == null) return null;
        if (StringUtils.isEmpty(tableName)) return null;

        IPFXExtensionType extensionType = null;

        if (typeCode.isExtension()) {
            extensionType = getExtensionType(typeCode, tableName);
        }

        if (extensionType == null && typeCode == PFXTypeCode.LOOKUPTABLE) {
            extensionType = getLookupTableType(tableName, validAfter);
        }

        if (extensionType == null && (typeCode.isExtension() || typeCode == PFXTypeCode.LOOKUPTABLE)) {
            throw new ConnectorException(TABLE_NOT_FOUND);
        }

        return extensionType;
    }

    default IPFXExtensionType getExtensionType(PFXTypeCode typeCode, String tableName) {
        if (typeCode != null && typeCode.isExtension() && !StringUtils.isEmpty(tableName)) {
            try {
                JsonNode node = getExtensionTables(typeCode);

                if (!node.isMissingNode()) {
                    JsonNode fieldNode = JsonUtil.getSelectedField(JsonUtil.getValueAsText(node.get(FIELD_VALUE)), tableName);
                    if (fieldNode != null && !fieldNode.isMissingNode()) {

                        return new PFXExtensionType(typeCode).
                                withAttributes(JsonUtil.getNumericValue(fieldNode.get("numberOfAttributes"))).
                                withBusinessKeys(JsonUtil.getStringArray(fieldNode.get("businessKey"))).withTable(tableName);
                    }
                }

            } catch (Exception ex) {
                throw new ConnectorException("Failed to read " + typeCode, ex);
            }
        }

        return null;
    }

    default PFXLookupTableType getLookupTableType(String tableName, String validAfter) {
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(validAfter)) return null;


        ObjectNode node = getLookupTable(tableName, validAfter);

        if (node != null) {
            PFXLookupTableType lookupTableType = JsonUtil.getPFXLookupTableType(node);
            if (lookupTableType.getLookupTableType() != null) {
                return (PFXLookupTableType) lookupTableType.withTable(JsonUtil.getValueAsText(node.get(FIELD_ID)));
            }
        }

        return null;
    }

    default JsonNode getExtensionTables(PFXTypeCode typeCode) {
        String operation = (typeCode == PFXTypeCode.PRODUCTEXTENSION) ? PRODUCTEXTENSION_LIST.getOperation() : CUSTOMEREXTENSION_LIST.getOperation();

        Iterable<ObjectNode> results = doAction(operation);

        return Iterables.get(results, 0);
    }

    default ObjectNode getLookupTable(String tableName, String validAfter) {
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(validAfter)) return null;

        List<ObjectNode> criterions = new ArrayList<>();
        criterions.add(PfxCommonService.buildSimpleCriterion(FIELD_UNIQUENAME, EQUALS.getValue(), tableName));
        criterions.add(PfxCommonService.buildSimpleCriterion(FIELD_VALIDAFTER, EQUALS.getValue(), validAfter));
        criterions.add(PfxCommonService.buildSimpleCriterion(STATUS, EQUALS.getValue(), PFXConstants.EntityStatus.ACTIVE.name()));
        ObjectNode request = createSimpleFetchRequest(AND.getValue(), criterions);

        JsonNode result = doPost(LOOKUPTABLE_FETCH.getOperation(), request);
        result = getFirstDataNode(result);
        if (JsonUtil.isObjectNode(result)){
            return (ObjectNode) result;
        }
        return null;
    }

    default Iterable<ObjectNode> doFetchMetadata(PFXTypeCode typeCode, IPFXExtensionType extensionType, String uniqueKey) {
        ObjectNode advancedCriterion = RequestFactory.buildFetchMetadataRequest(typeCode, extensionType, uniqueKey);
        return doFetchMetadata(typeCode, extensionType, 0l, MAX_METADATA_RECORDS, advancedCriterion);
    }

    default Iterable<ObjectNode> doFetchMetadata(PFXTypeCode typeCode, IPFXExtensionType extensionType, long startRow, int pageSize,
                                                 ObjectNode advancedCriterion) {
        if (!ConnectionUtil.haveMetaData(typeCode, extensionType)) {
            return null;
        }

        String metadataTypeCode = typeCode.getMetadataTypeCode();

        if (StringUtils.isEmpty(metadataTypeCode)) {
            return null;
        }

        return doFetch(typeCode, createPath(FETCH.getOperation(), metadataTypeCode), advancedCriterion, null, null, startRow, pageSize);
    }

    Iterable<ObjectNode> doFetch(PFXTypeCode typeCode, String apiPath, ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize);

    default Iterable<ObjectNode> getDataLoad(DataloadRunner.DataloadType type) {
        if (type == null) {
            throw new ConnectorException("Dataload type must be specified");
        }

        return doFetch(PFXTypeCode.DATALOAD, createPath(FETCH.getOperation(), PFXTypeCode.DATALOAD.getTypeCode()),
                createSimpleFetchRequest(buildSimpleCriterion(FIELD_TYPE, EQUALS.getValue(), type.name())), null, null, 0L, 200);

    }

    default List<Map<String, Object>> getDataFields(PFXTypeCode typeCode, String typedId) {
        if (StringUtils.isEmpty(typedId)) {
            return Collections.emptyList();
        }

        Iterable<ObjectNode> results = doAction(createPath(GET_FCS.getOperation(), typeCode.getTypeCode()));
        if (results != null) {
            Optional<ObjectNode> node = StreamSupport.stream(results.spliterator(), false).filter(
                    (ObjectNode v) -> typedId.equalsIgnoreCase(JsonUtil.getValueAsText(v.get(FIELD_TYPEDID)))
            ).findFirst();

            if (node.isPresent()) {
                List<Map<String, Object>> dataFields = new ArrayList<>();
                Iterator<? extends JsonNode> itr = node.get().get("fields").iterator();
                while (itr != null && itr.hasNext()) {
                    JsonNode n = itr.next();
                    if (n.isObject()) {
                        String name = JsonUtil.getValueAsText(n.get(FIELD_NAME));
                        boolean key = JsonUtil.getBooleanValue(n.get("key"));
                        boolean numeric = JsonUtil.getBooleanValue(n.get("numeric"));
                        Map<String, Object> map = new HashMap<>();
                        map.put(FIELD_NAME, name);
                        map.put("key", key);
                        map.put("numeric", numeric);
                        dataFields.add(map);
                    }
                }
                return dataFields;
            }
        }

        return Collections.emptyList();
    }

    default Collection<ObjectNode> getLookupTables() {
        List<ObjectNode> lookupTables = new ArrayList<>();
        Iterable<ObjectNode> results = doAction(LOOKUPTABLE_FETCH.getOperation());
        if (results != null) {
            results.forEach((ObjectNode v) -> {
                String uniqueName = JsonUtil.getValueAsText(v.get(FIELD_UNIQUENAME));
                String status = JsonUtil.getValueAsText(v.get(STATUS));
                String type = JsonUtil.getValueAsText(v.get(FIELD_TYPE));
                if (!StringUtils.isEmpty(uniqueName) && !"INACTIVE".equalsIgnoreCase(status) && !"JSON".equalsIgnoreCase(type)) {
                    lookupTables.add(v);
                }
            });
        }

        return lookupTables;
    }

    default void validateUserRoles(List<String> userRoles, PFXTypeCode typeCode) {
        if (!CollectionUtils.isEmpty(userRoles)) {
            ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
            userRoles.forEach(arrayNode::add);
            ObjectNode advancedCriteria = RequestUtil.createSimpleFetchRequest(JsonUtil.buildObjectNode(Pair.of(FIELD_VALUE, arrayNode)).put(FIELD_FIELDNAME, FIELD_UNIQUENAME)
                    .put(RequestUtil.OPERATOR, OperatorId.IN_SET.getValue()));

            Iterable<ObjectNode> results = doFetch(typeCode, createPath(FETCH.getOperation(), typeCode.getTypeCode()),
                    advancedCriteria,
                    null, ImmutableList.of(FIELD_UNIQUENAME), 0L, MAX_RECORDS);

            List<String> roles = JsonUtil.getStringArray(results, FIELD_UNIQUENAME);
            if (roles.size() < userRoles.size()) {
                throw new RequestValidationException("Some " + typeCode.name() + "(s) in your list do not exist in server");
            }
        }
    }

    default List<JsonNode> postBatch(String apiPath, ArrayNode request) {
        if (request == null || request.size() == 0) {
            return Collections.emptyList();
        }

        ArrayNode batches = doPostBatch(apiPath, request);
        List<JsonNode> nodes = new ArrayList<>();
        batches.forEach((JsonNode obj) -> {
            if (JsonUtil.isObjectNode(obj)) {
                JsonNode result = JsonUtil.getFirstDataNode(JsonUtil.getData(obj));
                if (!JsonUtil.isObjectNode(result)) {
                    result = obj;
                }
                nodes.add(result);
            }
        });

        return nodes;
    }

    ArrayNode doPostBatch(String apiPath, ArrayNode request);

    default void upload(PFXOperation operation, String apiPath, final byte[] data, MediaType okhttp3MediaType, final String filename, Map<String, String> queryParams) {
        if (queryParams == null) {
            queryParams = new HashMap<>();
        }

        FileUploadProvider upload = ConnectionUtil.createFileUploadProvider(okhttp3MediaType, DEFAULT_UPLOAD_FIELDNAME, filename, data);

        doUpload(upload, operation, apiPath, queryParams);

    }

    void doUpload(FileUploadProvider upload, PFXOperation operation, String apiPath, Map<String, String> queryParams);

    default void testConnection(String partition, boolean keepConnectionAlive) {
        try {
            Iterable<ObjectNode> results = doAction(PING.getOperation());
            JsonNode response = Iterables.get(results, 0);
            if (!partition.equalsIgnoreCase(JsonUtil.getValueAsText(response.get("partition")))) {
                throw new ConnectorException(ConnectorException.ErrorType.CONNECTION_ERROR);
            }
        } catch (Exception ex) {
            throw new ConnectorException(ConnectorException.ErrorType.CONNECTION_ERROR);
        } finally {
            if (!keepConnectionAlive) {
                closeConnection();
            }
        }
    }

    void closeConnection();

    default ObjectNode fetchFirstObject(String apiPath, String key, String id) {

        ObjectNode criteria = buildSimpleCriterion(key, EQUALS.getValue(), id);
        ObjectNode fetchRequest = RequestUtil.createSimpleFetchRequest(criteria);
        JsonNode fetchResult = doPost(apiPath, fetchRequest);
        JsonNode result = JsonUtil.getFirstDataNode(fetchResult);
        return JsonUtil.isObjectNode(result) ? (ObjectNode) result : null;

    }

    default List<String> fetchFormulas() {
        Iterable<ObjectNode> nodes = doAction(GET_FORMULA_NAMES.getOperation());

        final List<String> results = new ArrayList<>();
        if (nodes != null) {
            nodes.forEach((ObjectNode n) -> results.add(JsonUtil.getValueAsText(n.get(FIELD_UNIQUENAME))));
        }
        return results;
    }

}