package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.MAX_UPSERT_RECORDS;
import static net.pricefx.connector.common.util.JsonSchemaUtil.createMetadataFields;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXLookupTableType.LOWERBOUND;
import static net.pricefx.connector.common.util.PFXLookupTableType.UPPERBOUND;
import static net.pricefx.connector.common.util.PFXOperation.BATCH;


public class GenericUpsertor implements IPFXObjectUpsertor {

    private final PFXOperationClient pfxClient;
    private final String apiPath;
    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final JsonNode schema;
    private int maximumRecords = MAX_UPSERT_RECORDS;

    public GenericUpsertor(PFXOperationClient pfxClient, String apiPath, PFXTypeCode typeCode, IPFXExtensionType extensionType, JsonNode schema) {
        this.pfxClient = pfxClient;
        this.apiPath = apiPath;
        this.typeCode = typeCode;
        this.extensionType = extensionType;

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(typeCode, extensionType, null);
        List<Pair<String, String>> attributes = createMetadataFields(metadata);
        this.schema = (schema != null) ? schema :
                JsonSchemaUtil.loadSchema(PFXJsonSchema.getUpsertRequestSchema(typeCode, extensionType), typeCode, extensionType, attributes,
                        true, true, false, true);

    }

    public GenericUpsertor(PFXOperationClient pfxClient, String apiPath, PFXTypeCode typeCode, IPFXExtensionType extensionType, JsonNode schema, boolean showAdditionalKeys, boolean showAdditionalAttributes) {
        this.pfxClient = pfxClient;
        this.apiPath = apiPath;
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

    @Override
    public List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields) {
        if (validate) {
            RequestUtil.validateExtensionType(typeCode, extensionType);
            validateRequest(request, replaceNullKey);
        }

        ArrayNode upsertRequest = RequestFactory.buildUpsertRequest(typeCode, extensionType, request);

        if (request == null) {
            return Collections.emptyList();
        }

        List<JsonNode> results = doUpsert(upsertRequest);
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

        JsonValidationUtil.validatePayload(schema, inputNode);
        validateExtraFields(schema, inputNode);

        Set<String> schemaFields = getSchemaFields(schema);


        int count = JsonUtil.countJson(inputNode);
        if (count > maximumRecords) {
            throw new RequestValidationException("Too many elements. Please make sure no of elements < " + maximumRecords);
        }

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(typeCode, extensionType, null);

        Map<String, ObjectNode> metadataMap = new HashMap<>();

        if (metadata != null) {
            metadataMap = StreamSupport.stream(metadata.spliterator(), false)
                    .collect(Collectors.toMap((ObjectNode obj) -> JsonUtil.getValueAsText(obj.get(FIELD_FIELDNAME)), obj -> obj));
        }

        validateAttributes(inputNode, metadataMap, schemaFields, extensionType, typeCode, replaceNullKey);

    }

    protected List<JsonNode> doUpsert(ArrayNode request) {
        if (request != null && request.size() > 1) {
            return pfxClient.postBatch(createPath(apiPath, BATCH.getOperation()), request);
        } else if (request != null && request.size() == 1 && request.get(0) != null && request.get(0).isObject()) {
            List<JsonNode> list = new ArrayList<>();
            JsonNode result = pfxClient.doPost(apiPath, (ObjectNode) request.get(0));
            if (result == null) {
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

    private void validateAttributes(JsonNode inputNode, Map<String, ObjectNode> metadataMap, Set<String> schemaFields,
                                    IPFXExtensionType extensionType, PFXTypeCode typeCode, boolean replaceNullKey) {

        Set<String> mandatory = JsonValidationUtil.getMandatoryAttributes(metadataMap, extensionType, typeCode);
        mandatory = SetUtils.intersection(mandatory, schemaFields).toSet();

        if (!JsonUtil.isArrayNode(inputNode)) {
            return;
        }

        for (int i = 0; i < inputNode.size(); i++) {
            JsonNode node = inputNode.get(i);
            if (JsonUtil.isObjectNode(node)) {
                final int lineNo = i + 1;
                JsonValidationUtil.validateMissingMandatoryAttributes((ObjectNode) node, mandatory, lineNo);

                node.fields().forEachRemaining(field -> {
                    if (field != null) {
                        JsonValidationUtil.validateDataType(field.getValue(), metadataMap.get(field.getKey()), field.getKey(), lineNo);
                    }
                });

                if (extensionType instanceof PFXLookupTableType) {

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(FIELD_VALUE), lineNo, FIELD_VALUE);

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(FIELD_NAME), lineNo, FIELD_NAME);

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(LOWERBOUND), lineNo, LOWERBOUND);

                    JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType,
                            node.get(UPPERBOUND), lineNo, UPPERBOUND);

                } else if (extensionType instanceof PFXExtensionType && replaceNullKey) {
                    replaceNullKeyWithEmptyString((PFXExtensionType) extensionType, (ObjectNode) inputNode.get(i));
                }
            }
        }
    }

    private void replaceNullKeyWithEmptyString(PFXExtensionType extensionType, ObjectNode node) {
        //replace null key with empty string
        Set<String> existingFields = getExistingFields(node);

        Set<String> missingFields = extensionType.getBusinessKeys();
        missingFields.removeAll(existingFields);

        for (String missingField : missingFields) {
            if (node.get(missingField) == null || node.get(missingField).isMissingNode() ||
                    node.get(missingField).textValue() == null) {
                node.put(missingField, StringUtils.EMPTY);
            }
        }
    }

    private Set<String> getExistingFields(ObjectNode inputNode) {

        Iterable<Map.Entry<String, JsonNode>> fields =
                Iterables.filter(
                        JsonUtil.fields(inputNode), item ->
                                !StringUtils.isEmpty(JsonUtil.getValueAsText(inputNode.get(item.getKey())))
                );
        return ImmutableMap.copyOf(fields).keySet();

    }

    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }

    public String getApiPath() {
        return apiPath;
    }

}