package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.smartgwt.client.types.OperatorId;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXTypeCode.CONDITION_RECORD;


public class ConditionRecordUpdater extends GenericUpsertor {

    private final PFXConditionRecordType conditionRecordType;
    private static final int MAX_FETCH_BATCH_SIZE = 100;


    public ConditionRecordUpdater(PFXOperationClient pfxClient, PFXConditionRecordType conditionRecordType, JsonNode schema) {
        super(pfxClient,
                RequestPathFactory.buildUpdatePath(CONDITION_RECORD, conditionRecordType, null),
                CONDITION_RECORD, conditionRecordType, schema);


        this.conditionRecordType = conditionRecordType;

    }

    private Map<String, Map<String,Object>> getUpdateEntities(ArrayNode request){
        String path = RequestPathFactory.buildFetchPath(conditionRecordType, CONDITION_RECORD,null,null);
        List<String> ids = JsonUtil.getStringArray(request, FIELD_ID);
        List<List<String>> idLists = ListUtils.partition(ids, MAX_FETCH_BATCH_SIZE);
        Map<String, Map<String,Object>> results = new HashMap<>();
        for (List<String> idList : idLists){
            ObjectNode criterion = new ObjectNode(JsonNodeFactory.instance);
            criterion.put(FIELD_FIELDNAME, FIELD_ID);
            criterion.put("operator", OperatorId.IN_SET.getValue());
            criterion.set(FIELD_VALUE, JsonUtil.createArrayNodeFromStrings(idList));
            ObjectNode fetchRequest = RequestUtil.createSimpleFetchRequest(criterion);
            List<ObjectNode> fetchResults = new GenericFetcher(getPfxClient(), path,
                    CONDITION_RECORD, conditionRecordType, null, false).fetch(
                    fetchRequest, ImmutableList.of("key1"), false, false);

            for (ObjectNode fetchResult : fetchResults){

                String typedId = null;
                Number number = null;
                if (JsonUtil.isObjectNode(fetchResult)){
                    typedId = JsonUtil.getValueAsText(fetchResult.get(FIELD_TYPEDID));
                    number = JsonUtil.getNumericValue(fetchResult.get(FIELD_VERSION));
                }

                if (StringUtils.isEmpty(typedId) || number == null){
                    throw new RequestValidationException("Some objects to be updated do not exist.");
                }

                Map<String, Object> map = new HashMap<>();
                map.put(FIELD_TYPEDID, typedId);
                map.put(FIELD_VERSION, number.intValue());
                results.put(StringUtil.getIdFromTypedId(typedId), map);
            }
        }
        return results;
    }
    @Override
    protected List<JsonNode> doUpsert(ArrayNode request) {
        Map<String, Map<String,Object>> metadatas = getUpdateEntities(request);

        ArrayNode requestArray = new ArrayNode(JsonNodeFactory.instance);
        List<String> typedIds = new ArrayList<>();

        for (int i = 0; i < request.size(); i ++) {
            JsonNode node = request.get(i);

            String id = JsonUtil.getValueAsText(node.get(FIELD_ID));
            Map<String,Object> metadata = metadatas.get(id);

            String typedId = null;
            int number = -1;
            if (metadata != null){
                typedId = (String) metadata.get(FIELD_TYPEDID);
                number = (Integer) metadata.get(FIELD_VERSION);
            }

            if (StringUtils.isEmpty(typedId) || number == -1){
                throw new RequestValidationException("The object to be updated does not exist. Error line:"+i);
            }

            typedIds.add(typedId);

            ((ObjectNode) node).put(FIELD_VERSION, number).put(FIELD_TYPEDID, typedId);
            ((ObjectNode) node).remove(FIELD_ID);

            requestArray.add(node);
        }

        List<JsonNode> updateResult = super.doUpsert(requestArray);

        if (typedIds.size() != updateResult.size()){
            return updateResult;
        }

        List<JsonNode> finalResult = new ArrayList<>();

        for (int i = 0; i < updateResult.size(); i ++) {
            JsonNode updatedNode = updateResult.get(i);
            if (JsonUtil.isObjectNode(updatedNode) && updatedNode.has("errors")){
                ((ObjectNode) updatedNode).put(FIELD_TYPEDID, typedIds.get(i));
            }
            finalResult.add(updatedNode);
        }

        return finalResult;
    }

}