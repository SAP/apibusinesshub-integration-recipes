package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.smartgwt.client.types.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.MAX_RECORDS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.GET_FCS;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;


public class GenericFetcher implements IPFXObjectFetcher, IPFXObjectFilterRequester {


    private final PFXOperationClient pfxClient;

    private final String apiPath;
    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final String uniqueId;
    private final boolean convertValueToString;

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

    protected GenericFetcher(PFXOperationClient pfxClient, String apiPath, PFXTypeCode typeCode, IPFXExtensionType extensionType, String uniqueId, boolean convertValueToString) {
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

    public List<ObjectNode> fetch(boolean formatted) {
        Iterable<ObjectNode> fetched = pfxClient.doAction(apiPath);
        if (IterableUtils.isEmpty(fetched)) {
            return new ArrayList<>();
        }

        if (formatted) {
            return ResponseUtil.formatResponse(typeCode, fetched, convertValueToString);
        } else {
            return ImmutableList.copyOf(fetched);
        }

    }

    @Override
    public List<ObjectNode> fetch(ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize, boolean validate, boolean formatted) {
        Iterable<ObjectNode> fetched = fetch(advancedCriteria, sortBy, valueFields, startRow, pageSize, validate);

        if (formatted) {
            return ResponseUtil.formatResponse(typeCode, fetched, convertValueToString);
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
    public List<ObjectNode> fetch(ObjectNode request, Long startRow, int pageSize, boolean validate, boolean formatted) {
        JsonNode sortByNode = request.get("sortBy");
        List<String> sortBy = getArrayNodeFields(sortByNode);

        JsonNode resultFieldsNode = request.get("resultFields");
        List<String> resultFields = getArrayNodeFields(resultFieldsNode);

        validateRequest(request, resultFields, sortBy);

        RequestUtil.addAdvancedCriteria(((ObjectNode) request.get(FIELD_DATA)));
        if (formatted) {
            return fetch((ObjectNode) request.get(FIELD_DATA), sortBy, resultFields, startRow, pageSize, validate, true);
        } else {
            return fetchRaw(request, getApiPath());
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

        JsonNode schemaNode = loadSchema(PFXJsonSchema.getFetchResponseSchema(typeCode, extensionType), typeCode, extensionType, new ArrayList<>(),
                true, true, false);

        final List<String> fields = new ArrayList<>();
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