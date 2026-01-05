package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.IUpsertRequestValidator;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.*;

import static net.pricefx.connector.common.util.Constants.MAX_UPSERT_RECORDS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXTypeCode.CONDITION_RECORD;


public class ConditionRecordUpdater extends GenericBulkLoader implements IUpsertRequestValidator {

    private final IPFXExtensionType conditionRecordType;
    private static final int MAX_FETCH_BATCH_SIZE = 100;

    private final String lastUpdateTimestamp;

    private final JsonNode schema;

    public ConditionRecordUpdater(PFXOperationClient pfxClient, IPFXExtensionType conditionRecordType, String lastUpdateTimestamp) {
        super(pfxClient, CONDITION_RECORD, conditionRecordType, null);

        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.conditionRecordType = conditionRecordType;

        this.schema = JsonSchemaUtil.loadSchema(PFXJsonSchema.CONDITION_RECORD_UPDATE_REQUEST, CONDITION_RECORD, conditionRecordType, null,
                false, true, false, true);

    }


    private Map<String, Map<String, Object>> getUpdateEntities(ArrayNode request) {
        String path = RequestPathFactory.buildFetchPath(conditionRecordType, CONDITION_RECORD, null, null);
        List<String> ids = JsonUtil.getStringArray(request, FIELD_ID);
        List<List<String>> idLists = ListUtils.partition(ids, MAX_FETCH_BATCH_SIZE);
        Map<String, Map<String, Object>> results = new HashMap<>();
        for (List<String> idList : idLists) {
            ObjectNode criterion = new ObjectNode(JsonNodeFactory.instance);
            criterion.put(FIELD_FIELDNAME, FIELD_ID);
            criterion.put("operator", OperatorId.IN_SET.getValue());
            criterion.set(FIELD_VALUE, JsonUtil.createArrayNodeFromStrings(idList));
            ObjectNode fetchRequest = RequestUtil.createSimpleFetchRequest(criterion);
            List<ObjectNode> fetchResults = new GenericFetcher(getPfxClient(), path,
                    CONDITION_RECORD, conditionRecordType, null, false).fetch(
                    fetchRequest, ImmutableList.of("key1"), false, false);


            for (ObjectNode fetchResult : fetchResults) {

                String typedId = null;

                if (JsonUtil.isObjectNode(fetchResult)) {
                    typedId = JsonUtil.getValueAsText(fetchResult.get(FIELD_TYPEDID));
                }

                if (!StringUtils.isEmpty(typedId)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(FIELD_TYPEDID, typedId);
                    map.put(FIELD_LASTUPDATEDATE, JsonUtil.getValueAsText(fetchResult.get(FIELD_LASTUPDATEDATE)));
                    results.put(StringUtil.getIdFromTypedId(typedId), map);
                }
            }
        }
        return results;
    }

    private void validateLastUpdateDate(String typedId, String lastUpdateDate) throws ParseException {

        if (!StringUtils.isEmpty(lastUpdateDate) && !StringUtils.isEmpty(lastUpdateTimestamp) &&
                DateUtil.isAfterTimestamp(lastUpdateDate, lastUpdateTimestamp)) {
            throw new RequestValidationException(
                    "The object to be updated is conflicted with existing version. Error line:" + typedId);
        }
    }

    private List<String> validateLastUpdateDate(JsonNode request) {
        List<String> errorNodes = new ArrayList<>();
        Map<String, Map<String, Object>> metadatas = getUpdateEntities((ArrayNode) request);
        for (JsonNode node : request) {

            String id = JsonUtil.getValueAsText(node.get(FIELD_ID));

            Map<String, Object> metadata = metadatas.get(id);
            String lastUpdateDate = MapUtils.getString(metadata, FIELD_LASTUPDATEDATE);
            String typedId = MapUtils.getString(metadata, FIELD_TYPEDID);
            if (StringUtils.isEmpty(typedId)) {
                errorNodes.add(id);
            } else {
                try {
                    validateLastUpdateDate(typedId, lastUpdateDate);
                } catch (ParseException ex) {
                    throw new ConnectorException("Error in updating condition records. Please check logs:" + lastUpdateDate + ":" + lastUpdateTimestamp, ex);
                } catch (RequestValidationException ex) {
                    errorNodes.add(id);
                }
            }
        }
        return errorNodes;
    }


    private ObjectNode buildBulkLoadRequest(List<JsonNode> requestArray, List<String> allFieldNames) {
        ArrayNode allHeaders = new ArrayNode(JsonNodeFactory.instance);
        for (String fieldName : allFieldNames) {
            allHeaders.add(fieldName);
        }

        ArrayNode allData = new ArrayNode(JsonNodeFactory.instance);
        for (JsonNode node : requestArray) {
            ArrayNode values = new ArrayNode(JsonNodeFactory.instance);
            for (JsonNode header : allHeaders) {
                String fieldName = header.textValue();
                values.add(JsonUtil.getValueAsText(node.get(fieldName)));
            }
            allData.add(values);
        }

        ObjectNode dataLoadReq = new ObjectNode(JsonNodeFactory.instance);
        dataLoadReq.set(FIELD_DATA, allData);
        dataLoadReq.set("header", allHeaders);

        return dataLoadReq;
    }

    @Override
    protected void validateData(JsonNode inputNode) {

        RequestUtil.validateExtensionType(CONDITION_RECORD, conditionRecordType);
        JsonValidationUtil.validateMaxElements(inputNode, MAX_UPSERT_RECORDS);

        Iterable<ObjectNode> metadata = getPfxClient().doFetchMetadata(CONDITION_RECORD, conditionRecordType, null);
        validate(inputNode, metadata, schema, CONDITION_RECORD, conditionRecordType, false);

    }

    private void validateFields(List<String> allFieldNames, List<String> thisFieldNames) {
        Collection<String> diff = CollectionUtils.removeAll(thisFieldNames, allFieldNames);
        if (!CollectionUtils.isEmpty(diff)) {
            throw new RequestValidationException(
                    "All objects to be updated should contain same set of fields");
        }
    }

    @Override
    public JsonNode bulkLoad(JsonNode request, boolean validate) {
        if (!JsonUtil.isArrayNode(request)) {
            return super.bulkLoad(request, false);
        }

        List<String> errorNodes = new ArrayList<>();
        if (validate) {
            validateData(request);
            errorNodes = validateLastUpdateDate(request);
        }

        List<JsonNode> requestArray = new ArrayList<>();
        List<String> allFieldNames = new ArrayList<>();
        for (JsonNode node : request) {
            List<String> thisFieldNames = IteratorUtils.toList(node.fieldNames());
            if (CollectionUtils.isEmpty(allFieldNames)) {
                allFieldNames.addAll(thisFieldNames);
            }
            if (!errorNodes.contains(JsonUtil.getValueAsText(node.get(FIELD_ID)))) {
                validateFields(allFieldNames, thisFieldNames);
                requestArray.add(node);
            }
        }

        if (!CollectionUtils.isEmpty(requestArray)) {
            ObjectNode dataLoadReq = buildBulkLoadRequest(requestArray, allFieldNames);
            super.bulkLoad(dataLoadReq, false);
        }

        ArrayNode ids = new ArrayNode(JsonNodeFactory.instance);
        for (String errorNode : errorNodes) {
            ids.add(errorNode);
        }

        ObjectNode resultNode = new ObjectNode(JsonNodeFactory.instance);
        resultNode.set("errored", ids);
        if (!ids.isEmpty()){
            resultNode.put("erroredMessage", "Latest updates already in Pricefx. Update ignored");
        }
        return resultNode;

    }

}