package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.smartgwt.client.types.OperatorId;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXTypeCode.CONDITION_RECORD;


public class ConditionRecordUpdater extends GenericUpsertor {

    private final PFXConditionRecordType conditionRecordType;
    private static final int MAX_FETCH_BATCH_SIZE = 100;

    private final String lastUpdateTimestamp;

    private static final String ERRORS = "errors";

    public ConditionRecordUpdater(PFXOperationClient pfxClient, PFXConditionRecordType conditionRecordType, JsonNode schema, String lastUpdateTimestamp) {
        super(pfxClient,
                RequestPathFactory.buildUpdatePath(CONDITION_RECORD, conditionRecordType, null),
                CONDITION_RECORD, conditionRecordType, schema, false, true);

        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.conditionRecordType = conditionRecordType;

    }

    private List<String> findConflictedIds(List<String> idList, List<ObjectNode> fetchResults) {
        List<String> fetchedIds = JsonUtil.getStringArray(fetchResults, FIELD_TYPEDID);


        final List<String> results = fetchedIds.stream()
                .map(s -> s.replace("." + conditionRecordType.getTypeCodeSuffix(), ""))
                .collect(Collectors.toList());

        return ListUtils.removeAll(idList, results);
    }

    private Map<String, Map<String, Object>> getUpdateEntities(ArrayNode request, List<String> conflictedIds) {
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

            if (conflictedIds == null) {
                conflictedIds = new ArrayList<>();
            }
            conflictedIds.addAll(findConflictedIds(idList, fetchResults));

            for (ObjectNode fetchResult : fetchResults) {

                String typedId = null;
                Number number = null;

                if (JsonUtil.isObjectNode(fetchResult)) {
                    typedId = JsonUtil.getValueAsText(fetchResult.get(FIELD_TYPEDID));
                    number = JsonUtil.getNumericValue(fetchResult.get(FIELD_VERSION));
                }

                if (!StringUtils.isEmpty(typedId)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(FIELD_TYPEDID, typedId);

                    if (number != null) {
                        map.put(FIELD_VERSION, number.intValue());
                    }

                    map.put(FIELD_LASTUPDATEDATE, JsonUtil.getValueAsText(fetchResult.get(FIELD_LASTUPDATEDATE)));
                    results.put(StringUtil.getIdFromTypedId(typedId), map);
                }
            }
        }
        return results;
    }

    private void validateUpdateRecord(String typedId, String lastUpdateDate) throws ParseException {

        if (!StringUtils.isEmpty(lastUpdateDate) && !StringUtils.isEmpty(lastUpdateTimestamp) &&
                DateUtil.isAfterTimestamp(lastUpdateDate, lastUpdateTimestamp)) {
            throw new RequestValidationException(
                    "The object to be updated is conflicted with existing version. Error line:" + typedId);
        }
    }

    @Override
    protected List<JsonNode> doUpsert(ArrayNode request) {
        List<String> conflictedIds = new ArrayList<>();
        Map<String, Map<String, Object>> metadatas = getUpdateEntities(request, conflictedIds);

        ArrayNode requestArray = new ArrayNode(JsonNodeFactory.instance);
        List<String> typedIds = new ArrayList<>();
        List<ObjectNode> errorNodes = new ArrayList<>();

        for (JsonNode node : request) {

            String id = JsonUtil.getValueAsText(node.get(FIELD_ID));
            Map<String, Object> metadata = metadatas.get(id);

            String lastUpdateDate = MapUtils.getString(metadata, FIELD_LASTUPDATEDATE);
            String typedId = MapUtils.getString(metadata, FIELD_TYPEDID);
            Integer number = MapUtils.getInteger(metadata, (FIELD_VERSION));

            if (!StringUtils.isEmpty(typedId) && number != null && number >= 0) {
                try {
                    validateUpdateRecord(typedId, lastUpdateDate);
                    typedIds.add(typedId);

                    ((ObjectNode) node).put(FIELD_VERSION, number).put(FIELD_TYPEDID, typedId);
                    ((ObjectNode) node).remove(FIELD_ID);
                    requestArray.add(node);

                } catch (ParseException ex) {
                    throw new ConnectorException("Error in updating condition records. Please check logs:" + lastUpdateDate + ":" + lastUpdateTimestamp, ex);
                } catch (RequestValidationException ex) {
                    errorNodes.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_TYPEDID, typedId).
                            put(ERRORS, ex.getMessage()).put("request", node.toPrettyString()));
                }
            }

        }

        for (String id : conflictedIds) {
            errorNodes.add(new ObjectNode(JsonNodeFactory.instance).put(FIELD_TYPEDID, id + "." + conditionRecordType.getTypeCodeSuffix()).
                    put(ERRORS, "record not found or version conflicted"));
        }

        List<JsonNode> updateResult = super.doUpsert(requestArray);

        List<JsonNode> finalResult = new ArrayList<>();

        for (int i = 0; i < updateResult.size(); i++) {
            JsonNode updatedNode = updateResult.get(i);
            if (JsonUtil.isObjectNode(updatedNode) && updatedNode.has(ERRORS)) {
                ((ObjectNode) updatedNode).put(FIELD_TYPEDID, typedIds.get(i));
            }
            finalResult.add(updatedNode);
        }

        finalResult.addAll(errorNodes);

        return finalResult;
    }

}