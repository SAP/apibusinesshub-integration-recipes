package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.MAX_RECORDS;
import static net.pricefx.connector.common.util.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.GET_FCS;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;


public class GenericFetcher implements IPFXObjectFetcher, IPFXObjectFilterRequester {


    private final PFXOperationClient pfxClient;

    private static final String RESULT_FIELDS = "resultFields";

    private final String apiPath;
    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final String uniqueId;
    private final boolean convertValueToString;


    private static final String TOTAL_ROWS = "totalRows";

    public GenericFetcher(PFXOperationClient pfxClient, String typedId,
                          String secondaryKey, boolean convertValueToString) {
        this.pfxClient = pfxClient;

        String[] ids = typedId.split("\\.");
        if (ids == null || ids.length != 2) {
            throw new RequestValidationException("TypedId is invalid. The format must be <id>.<TypeCode>.");
        }

        this.uniqueId = ids[0];
        this.typeCode = PFXTypeCode.findByTypeCodeOrName(ids[1], PFXTypeCode.TYPEDID);

        this.apiPath = RequestPathFactory.buildFetchPath(null, this.typeCode, secondaryKey, ids[1]);

        this.extensionType = null;
        this.convertValueToString = convertValueToString;


    }

    public GenericFetcher(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, String uniqueId, boolean convertValueToString) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
        this.extensionType = extensionType;
        this.uniqueId = uniqueId;
        this.convertValueToString = convertValueToString;
        this.apiPath = RequestPathFactory.buildFetchPath(extensionType, typeCode, uniqueId, null);
    }

    public GenericFetcher(PFXOperationClient pfxClient, String apiPath, PFXTypeCode typeCode, IPFXExtensionType extensionType, String uniqueId, boolean convertValueToString) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
        this.extensionType = extensionType;
        this.uniqueId = uniqueId;
        this.convertValueToString = convertValueToString;
        this.apiPath = apiPath;
    }

    public PFXTypeCode getTypeCode() {
        return typeCode;
    }

    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }

    public ObjectNode getById() {

        ObjectNode request = RequestUtil.createSimpleFetchRequest(buildSimpleCriterion(FIELD_ID, EQUALS.getValue(), uniqueId));

        List<ObjectNode> results = fetch(request, Collections.emptyList(), false, true);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        } else {
            return results.get(0);
        }
    }


    @Override
    public List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate, boolean formatted) {
        Iterable<ObjectNode> fetched = fetch(advancedCriteria, sortBy, valueFields, startRow, pageSize, validate);

        if (formatted) {
            return ResponseUtil.formatResponse(typeCode, extensionType, fetched, convertValueToString);
        } else {
            return ImmutableList.copyOf(fetched);
        }

    }

    private Iterable<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate) {
        if (validate) {
            RequestUtil.validateExtensionType(typeCode, extensionType);
        }

        Iterable<ObjectNode> fetched = pfxClient.doFetch(typeCode, apiPath, advancedCriteria, sortBy, valueFields, startRow, pageSize);
        if (IterableUtils.isEmpty(fetched)) {
            return new ArrayList<>();
        }

        return fetched;
    }

    @Override
    public List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, boolean validate, boolean formatted) {
        return fetch(advancedCriteria, sortBy, Collections.emptyList(), 0L, MAX_RECORDS, validate, formatted);
    }

    @Override
    public int fetchCount(ObjectNode request) {
        JsonNode sortByNode = request.get("sortBy");
        List<String> sortBy = getArrayNodeFields(sortByNode);

        JsonNode resultFieldsNode = request.get(RESULT_FIELDS);
        List<String> resultFields = getArrayNodeFields(resultFieldsNode);

        validateRequest(request, resultFields, sortBy);

        ObjectNode dataRequest = RequestFactory.buildFetchDataRequest(((ObjectNode) request.get(FIELD_DATA)), typeCode, extensionType);
        RequestUtil.addAdvancedCriteria(dataRequest);
        request.put(FIELD_STARTROW, 0).put(FIELD_ENDROW, 1);
        request.set(FIELD_DATA, dataRequest);

        JsonNode node = pfxClient.doPostRaw(apiPath, request);
        if (JsonUtil.isObjectNode(node.get(FIELD_RESPONSE)) &&
                node.get(FIELD_RESPONSE).get(TOTAL_ROWS) != null && node.get(FIELD_RESPONSE).get(TOTAL_ROWS).isInt()) {
            return node.get(FIELD_RESPONSE).get(TOTAL_ROWS).intValue();
        } else {
            return 0;
        }

    }

    @Override
    public List<ObjectNode> fetch(ObjectNode request, Long startRow, int pageSize, boolean validate, boolean formatted) {
        return fetch(request,startRow,pageSize,validate,formatted,false);
    }

    @Override
    public List<ObjectNode> fetch(ObjectNode request, Long startRow, int pageSize, boolean validate, boolean formatted, boolean rawPost) {
        JsonNode sortByNode = request.get("sortBy");
        List<String> sortBy = getArrayNodeFields(sortByNode);

        JsonNode resultFieldsNode = request.get(RESULT_FIELDS);
        List<String> resultFields = getArrayNodeFields(resultFieldsNode);

        if (validate) {
            validateRequest(request, resultFields, sortBy);
        }

        if ((!CollectionUtils.isEmpty(resultFields)) && typeCode != null &&
                (typeCode == PFXTypeCode.MANUALPRICELIST || typeCode == PFXTypeCode.PAYOUT || typeCode.isConditionRecordTypeCodes())){
            ((ArrayNode) request.get(RESULT_FIELDS)).add(FIELD_TYPEDID);
        }


        ObjectNode dataRequest = RequestFactory.buildFetchDataRequest(((ObjectNode) request.get(FIELD_DATA)), typeCode, extensionType);
        RequestUtil.addAdvancedCriteria(dataRequest);
        request.set(FIELD_DATA, dataRequest);

        if (rawPost){
            request.put(FIELD_STARTROW, startRow).put(FIELD_ENDROW, pageSize + startRow);
            return fetchRaw(request, getApiPath());
        } else {
            return fetch((ObjectNode) request.get(FIELD_DATA), sortBy, resultFields, startRow, pageSize, validate, formatted);
        }

    }

    private List<String> getArrayNodeFields(JsonNode node) {
        List<String> results = null;
        if (JsonUtil.isArrayNode(node)) {
            results = JsonUtil.getStringArray(node);
        }
        return results;
    }

    private void validateRequest(ObjectNode request, List<String> resultFields, List<String> sortBy) {
        this.validateCriteria(request.get(FIELD_DATA), false, PFXJsonSchema.FILTER_REQUEST);

        if (CollectionUtils.isEmpty(sortBy)) {
            throw new RequestValidationException("Sort By is mandatory");
        }


        JsonNode schemaNode = JsonSchemaUtil.loadSchema(PFXJsonSchema.getFetchResponseSchema(typeCode, extensionType), typeCode, extensionType, new ArrayList<>(),
                true, true, false, false);

        final Set<String> fields = new HashSet<>();
        if (PFXTypeCode.isDataCollectionTypeCodes(typeCode) && !StringUtils.isEmpty(uniqueId)) {
            ObjectNode tableDefinition = Iterables.get(pfxClient.doFetch(typeCode,
                    createPath(GET_FCS.getOperation(), typeCode.getTypeCode()),
                    RequestUtil.createSimpleFetchRequest(buildSimpleCriterion(FIELD_UNIQUENAME, EQUALS.getValue(), uniqueId)),
                    ImmutableList.of(FIELD_UNIQUENAME), Collections.emptyList(), 0L, MAX_RECORDS), 0);

            tableDefinition.get("fields").forEach(field -> fields.add(field.get(FIELD_NAME).textValue()));
        } else {
            fields.addAll(JsonSchemaUtil.getFields(schemaNode));
        }

        if ((resultFields != null && !fields.containsAll(resultFields)) ||
                !fields.containsAll(sortBy)) {
            throw new RequestValidationException("Contains fields which do not exist in the target table");
        }

    }

    public List<ObjectNode> fetchRaw(ObjectNode request, String apiPath) {
        JsonNode node = pfxClient.doPostRaw(apiPath, request);
        node = JsonUtil.getData(node);
        List<ObjectNode> results = new ArrayList<>();
        if (JsonUtil.isArrayNode(node)) {
            node.forEach((JsonNode result) -> {
                if (JsonUtil.isObjectNode(result)) {
                    results.add((ObjectNode) result);
                }
            });
        }
        return results;
    }

    public String getApiPath() {
        return apiPath;
    }

    public boolean isConvertValueToString() {
        return convertValueToString;
    }
}