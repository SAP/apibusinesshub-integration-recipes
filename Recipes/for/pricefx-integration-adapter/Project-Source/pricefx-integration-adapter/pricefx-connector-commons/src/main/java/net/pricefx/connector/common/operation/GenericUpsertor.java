package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.IUpsertRequestValidator;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.pricefx.connector.common.util.Constants.MAX_UPSERT_RECORDS;
import static net.pricefx.connector.common.util.JsonSchemaUtil.createMetadataFields;


public class GenericUpsertor implements IPFXObjectUpsertor, IUpsertRequestValidator {

    private final PFXOperationClient pfxClient;
    private final PFXTypeCode typeCode;

    private boolean updateOnly = false;
    private final IPFXExtensionType extensionType;
    private final JsonNode schema;
    private int maximumRecords = MAX_UPSERT_RECORDS;

    public GenericUpsertor(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, JsonNode schema) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
        this.extensionType = extensionType;

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(typeCode, extensionType, null);
        List<Pair<String, String>> attributes = createMetadataFields(metadata);
        this.schema = (schema != null) ? schema :
                JsonSchemaUtil.loadSchema(PFXJsonSchema.getUpsertRequestSchema(typeCode, extensionType), typeCode, extensionType, attributes,
                        true, true, false, true);

    }

    public GenericUpsertor(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, JsonNode schema, boolean showAdditionalKeys, boolean showAdditionalAttributes) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
        this.extensionType = extensionType;

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(typeCode, extensionType, null);
        List<Pair<String, String>> attributes = createMetadataFields(metadata);
        this.schema = (schema != null) ? schema :
                JsonSchemaUtil.loadSchema(PFXJsonSchema.getUpsertRequestSchema(typeCode, extensionType), typeCode, extensionType, attributes,
                        showAdditionalKeys, showAdditionalAttributes, false, true);

    }


    public GenericUpsertor withMaximumRecords(int maximumRecords) {
        this.maximumRecords = maximumRecords;
        return this;
    }

    public GenericUpsertor withUpdateOnly(boolean updateOnly) {
        this.updateOnly = updateOnly;
        return this;
    }

    protected ArrayNode buildUpsertRequest(JsonNode request) {
        return RequestFactory.buildUpsertRequest(typeCode, extensionType, request);
    }

    @Override
    public List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields, boolean rawPost) {
        if (validate) {
            RequestUtil.validateExtensionType(typeCode, extensionType);
            validateRequest(request, replaceNullKey);
        }

        if (request == null) {
            return Collections.emptyList();
        }

        ArrayNode upsertRequest = buildUpsertRequest(request);

        List<JsonNode> results = doUpsert(upsertRequest, rawPost);
        if (isSimple) {
            return ImmutableList.of(new TextNode(results.size() + ""));
        }

        results.forEach(result -> {
            if (JsonUtil.isObjectNode(result)) {
                if (showSystemFields) {
                    ResponseUtil.preformatResponse(typeCode, extensionType, (ObjectNode) result);
                    ResponseUtil.postformatResponse(typeCode, (ObjectNode) result, convertValueToString);
                } else {
                    ResponseUtil.formatResponse(typeCode, extensionType, (ObjectNode) result, convertValueToString);
                }
            }
        });
        return results;

    }

    protected void validateRequest(JsonNode inputNode, boolean replaceNullKey) {

        int count = JsonUtil.countJson(inputNode);
        if (count > maximumRecords) {
            throw new RequestValidationException("Too many elements. Please make sure no of elements < " + maximumRecords);
        }

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(typeCode, extensionType, null);
        validate(inputNode, metadata, schema, typeCode, extensionType, replaceNullKey);

    }

    protected List<JsonNode> doUpsert(ArrayNode request, boolean rawPost) {
        if (request != null && request.size() > 1) {
            return pfxClient.postBatch(getApiPath(true), request, rawPost);
        } else if (request != null && request.size() == 1 && request.get(0) != null && request.get(0).isObject()) {
            List<JsonNode> list = new ArrayList<>();

            JsonNode result;
            if (rawPost) {
                result = pfxClient.doPostRaw(getApiPath(false), request.get(0));
                result = JsonUtil.getData(result);
            } else {
                result = pfxClient.doPost(getApiPath(false), (ObjectNode) request.get(0));
            }

            if (result == null || result instanceof MissingNode) {
                return list;
            } else if (result.isArray()) {
                list.add(result.get(0));
            } else {
                list.add(result);
            }

            return list;
        }

        throw new UnsupportedOperationException("Request is not a valid object");
    }

    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }

    public String getApiPath(boolean batch) {
        return RequestPathFactory.buildUpsertPath(extensionType, typeCode, batch, updateOnly);

    }

    public PFXTypeCode getTypeCode() {
        return typeCode;
    }

    public boolean isUpdateOnly() {
        return updateOnly;
    }

    public void setUpdateOnly(boolean updateOnly) {
        this.updateOnly = updateOnly;
    }
}