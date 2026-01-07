package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.OperatorId;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.util.RequestUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;


public abstract class ActionUpdater extends GenericUpsertor {

    protected ActionUpdater(PFXOperationClient pfxClient, PFXTypeCode typeCode) {
        super(pfxClient, typeCode, null, null);
        withUpdateOnly(true);
    }

    @Override
    public String getApiPath(boolean batch) {
        return super.getApiPath(false);
    }

    @Override
    protected ArrayNode buildUpsertRequest(JsonNode request) {
        request = super.buildUpsertRequest(request);
        if (request == null) {
            return null;
        }

        ArrayNode updateRequests = new ArrayNode(JsonNodeFactory.instance);
        request.forEach(req -> {
            String uniqueName = JsonUtil.getValueAsText(req.get(FIELD_UNIQUENAME));
            ObjectNode criterion = RequestUtil.createSimpleFetchRequest(buildSimpleCriterion(FIELD_UNIQUENAME, OperatorId.EQUALS.getValue(), uniqueName));

            List<ObjectNode> results = new GenericFetcher(getPfxClient(), getTypeCode(), null, null, false).
                    fetch(criterion, ImmutableList.of(FIELD_UNIQUENAME),
                            false, false);

            ObjectNode updateRequest = new ObjectNode(JsonNodeFactory.instance);
            ObjectNode oldValues = new ObjectNode(JsonNodeFactory.instance);
            String errorMessage = "";
            if (CollectionUtils.isEmpty(results) || results.get(0) == null) {
                errorMessage = getTypeCode().getLabel() + " to be updated not found:" + uniqueName;
            } else {
                ObjectNode result = results.get(0);

                String typedId = JsonUtil.getValueAsText(result.get(FIELD_TYPEDID));
                Number number = JsonUtil.getNumericValue(result.get(FIELD_VERSION));

                if (StringUtils.isEmpty(typedId) || number == null) {
                    errorMessage = "TypedID or version number not found in the " + getTypeCode().getLabel() + ":" + uniqueName;
                } else {
                    oldValues.put(FIELD_TYPEDID, typedId);
                    oldValues.set(FIELD_VERSION, new IntNode(number.intValue()));

                    ((ObjectNode) req).remove(FIELD_UNIQUENAME);
                    ((ObjectNode) req).put(FIELD_TYPEDID, JsonUtil.getValueAsText(result.get(FIELD_TYPEDID)));
                }

            }

            if (StringUtils.isEmpty(errorMessage)) {
                updateRequest.set("oldValues", oldValues);
                updateRequest.set(FIELD_DATA, req);
            } else {
                updateRequest.put(FIELD_UNIQUENAME, uniqueName);
                updateRequest.put("message", errorMessage);
            }

            updateRequests.add(updateRequest);
        });
        return updateRequests;
    }

    @Override
    protected List<JsonNode> doUpsert(ArrayNode request, boolean rawPost) {
        List<JsonNode> errored = new ArrayList<>();

        ArrayNode updateRequest = new ArrayNode(JsonNodeFactory.instance);
        request.forEach(node -> {
            if (JsonUtil.isObjectNode(node.get("oldValues"))){
                updateRequest.add(node);
            } else {
                errored.add(node);
            }
        });

        List<JsonNode> results = new ArrayList<>();
        if (updateRequest.size() > 0) {
            results = super.doUpsert(updateRequest, rawPost);
        }
        results.addAll(errored);
        return results;

    }
}