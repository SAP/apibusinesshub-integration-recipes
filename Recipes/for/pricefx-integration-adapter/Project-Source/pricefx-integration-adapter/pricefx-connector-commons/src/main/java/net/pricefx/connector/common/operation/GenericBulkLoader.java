package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.pricefx.connector.common.util.Constants.MAX_BULKLOAD_RECORDS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.*;


public class GenericBulkLoader implements IPFXObjectBulkLoader {

    private final PFXOperationClient pfxClient;
    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final String tableName;
    private int maximumRecords = MAX_BULKLOAD_RECORDS;

    public GenericBulkLoader(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, String tableName) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;

        if (StringUtils.isEmpty(tableName) && extensionType != null) {
            this.tableName = extensionType.getTable();
        } else {
            this.tableName = tableName;
        }

        this.extensionType = extensionType;

    }

    public GenericBulkLoader withMaximumRecords(int maximumRecords) {
        this.maximumRecords = maximumRecords;
        return this;
    }

    @Override
    public String bulkLoad(JsonNode request, boolean validate) {

        validateRequest(request, validate);

        String apiPath = RequestPathFactory.buildBulkLoadPath(extensionType, typeCode, tableName);

        JsonNode resp = pfxClient.doPost(apiPath, RequestFactory.buildBulkLoadRequest(typeCode, (ObjectNode) request, extensionType));

        if (typeCode == PFXTypeCode.DATAFEED) {
            return "0"; //server isn't returning the number of records updated
        } else {
            return JsonUtil.getValueAsText(JsonUtil.getFirstDataNode(resp), "0");
        }

    }

    protected void validateRequest(JsonNode inputNode, boolean validate) {

        RequestUtil.validateExtensionType(typeCode, extensionType);

        JsonNode schema = JsonSchemaUtil.loadSchema(PFXJsonSchema.BULK_LOAD_REQUEST, true);

        JsonValidationUtil.validatePayload(schema, inputNode);


        int count = JsonUtil.countJson(inputNode.get(FIELD_DATA));
        if (count > maximumRecords) {
            throw new RequestValidationException("Too many elements. Please make sure no of elements < " + maximumRecords);
        }

        //validate if any fields not stated in schema exist in request. only header and data is allowed
        if (inputNode.size() != 2) {
            throw new RequestValidationException(SCHEMA_VALIDATION_ERROR);
        }

        //validate no of data fields = no of column header
        validateDataLength(inputNode.get(FIELD_DATA), inputNode.get(HEADER).size());

        if (typeCode == PFXTypeCode.CONDITION_RECORD_STAGING) {
            validateKeyFields(inputNode.get(HEADER));
            validateExtraFields(inputNode.get(HEADER));
        }

        if (validate) {

            Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(typeCode, extensionType, null);

            Map<String, ObjectNode> metadataMap = new HashMap<>();

            if (metadata != null) {
                metadataMap = StreamSupport.stream(metadata.spliterator(), false)
                        .collect(Collectors.toMap((ObjectNode obj) -> JsonUtil.getValueAsText(obj.get(FIELD_FIELDNAME)), obj -> obj));
            }
            if (JsonUtil.isArrayNode(inputNode.get(HEADER))) {
                validateMandatoryAttributes((ArrayNode) inputNode.get(HEADER), inputNode.get(FIELD_DATA), metadataMap);
            }
        }


    }


    private void validateDataLength(JsonNode dataNode, int columns) {
        if (columns < 1) {
            throw new RequestValidationException(SCHEMA_VALIDATION_ERROR, "Header is not defined");
        }

        if (JsonUtil.countJson(dataNode) < 1) {
            throw new RequestValidationException(SCHEMA_VALIDATION_ERROR, "Missing Data");

        }

        for (int i = 0; i < dataNode.size(); i++) {

            JsonNode node = dataNode.get(i);
            if (!JsonUtil.isArrayNode(node)) {
                throw new RequestValidationException(i + 1, SCHEMA_VALIDATION_ERROR,
                        "Data field should be array of arrays(rows)");
            }

            if (node.size() != columns) {
                throw new RequestValidationException(i + 1, SCHEMA_VALIDATION_ERROR,
                        "No of columns in data does not match the header definition");
            }

        }
    }

    private void validateMandatoryAttributes(ArrayNode headerNode, JsonNode dataNode,
                                             Map<String, ObjectNode> metadataMap) {

        if (typeCode == PFXTypeCode.DATAFEED) {
            return;
        }

        Set<String> mandatory = getMandatoryAttributes(metadataMap);

        List<String> existing = new ArrayList<>();
        List<Integer> mandatoryPositions = new ArrayList<>();
        List<Integer> keyPositions = new ArrayList<>();
        int pos = 0;
        for (JsonNode node : headerNode) {
            String header = JsonUtil.getValueAsText(node);
            if (!StringUtils.isEmpty(header)) {
                existing.add(header);
                if (mandatory.contains(header)) {
                    mandatoryPositions.add(pos);
                }

                if (extensionType instanceof IPFXExtensionType && extensionType.getBusinessKeys() != null &&
                        extensionType.getBusinessKeys().contains(header)) {
                    keyPositions.add(pos);
                }

            }
            pos++;
        }

        if (!existing.containsAll(mandatory)) {
            mandatory.removeAll(existing);
            throw new RequestValidationException(MISSING_MANDATORY_ATTRIBUTES, mandatory.toString());
        }

        validateAttributes(headerNode, dataNode, metadataMap, mandatoryPositions, existing, keyPositions);


    }

    private Set<String> getMandatoryAttributes(Map<String, ObjectNode> metadataMap) {
        Set<String> mandatoryFields = (extensionType != null) ? extensionType.getMandatoryFields() : typeCode.getMandatoryFields();


        Set<String> mandatory = new HashSet<>();
        if (mandatoryFields != null) {
            mandatory.addAll(mandatoryFields);
        }

        mandatory.addAll(JsonValidationUtil.getMandatoryAttributes(metadataMap));

        return mandatory;
    }

    private void validateAttributes(JsonNode headerNode, JsonNode dataNode, Map<String, ObjectNode> metadataMap, List<Integer> mandatoryPositions, List<String> existing, List<Integer> keyPositions) {
        if (CollectionUtils.isEmpty(mandatoryPositions)) return;

        for (int i = 0; i < dataNode.size(); i++) {
            JsonNode lineNode = dataNode.get(i);
            int pos = 0;
            int totalKeyLength = 0;
            for (JsonNode node : lineNode) {
                if (mandatoryPositions.contains(pos) &&
                        (node.isNull() || StringUtils.isEmpty(JsonUtil.getValueAsText(node)))) {
                    throw new RequestValidationException(i + 1, MISSING_MANDATORY_ATTRIBUTES, existing.get(pos));
                }

                totalKeyLength += keyPositions.contains(pos) ? JsonUtil.getValueAsText(node).length() : 0;
                validateKeyLength(existing.get(pos), i + 1, keyPositions, totalKeyLength);

                validateDataType(node, metadataMap.get(headerNode.get(pos).textValue()), headerNode.get(pos).textValue(), i + 1);
                pos++;
            }
        }
    }

    private void validateKeyLength(String existing, int pos, List<Integer> keyPositions, int totalKeyLength) {
        if ((extensionType instanceof PFXExtensionType && keyPositions.size() >= 3 && totalKeyLength + extensionType.getTable().length() > 255) ||
                (extensionType instanceof PFXLookupTableType && keyPositions.size() >= 4 && totalKeyLength > 255)) {
            throw new RequestValidationException(pos, KEY_EXCEED_MAX, existing);
        }

    }

    private void validateDataType(JsonNode node, JsonNode metadataNode, String header, int line) {
        if (!node.isValueNode() && !node.isNull()) {
            throw new RequestValidationException(line, SCHEMA_VALIDATION_ERROR, "Only null or string or number is accepted.");
        }

        if (extensionType instanceof PFXLookupTableType) {
            JsonValidationUtil.validatePPDataType((PFXLookupTableType) extensionType, node, line, header);
        }

        JsonValidationUtil.validateDataType(node, metadataNode, header, line);

    }

    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }

    private void validateKeyFields(JsonNode headerNode) {
        Set<String> keyFields = new HashSet<>();
        Collections.addAll(keyFields, typeCode.getIdentifierFieldNames());
        if (extensionType != null) keyFields.addAll(extensionType.getBusinessKeys());

        if (headerNode != null && headerNode.isArray()) {
            List<String> headers = JsonUtil.getStringArray(headerNode);
            keyFields.removeAll(headers);
        }

        if (!keyFields.isEmpty()) {
            throw new RequestValidationException(MISSING_MANDATORY_ATTRIBUTES, keyFields.toString());
        }
    }

    private void validateExtraFields(JsonNode headerNode) {
        PFXJsonSchema schema = PFXJsonSchema.getFetchResponseSchema(typeCode, extensionType);
        Set<String> validFields = JsonSchemaUtil.getFields(
                JsonSchemaUtil.loadSchema(schema, typeCode, extensionType, null, true, true, false, false));

        if (headerNode != null && headerNode.isArray()) {
            List<String> headers = JsonUtil.getStringArray(headerNode);
            headers.removeAll(validFields);

            if (!headers.isEmpty()) {
                throw new RequestValidationException(
                        SCHEMA_VALIDATION_ERROR, "Contains Extra Fields not stated in schema:" + headers);
            }
        }
    }
}




