package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.*;
import static net.pricefx.connector.common.util.PFXTypeCode.USER;


public class UserUpsertor extends GenericUpsertor {

    public UserUpsertor(PFXOperationClient pfxClient) {
        super(pfxClient, USER, null, null);
        setUpdateOnly(true);
        withMaximumRecords(5);
    }

    @Override
    protected ArrayNode buildUpsertRequest(JsonNode request) {

        request = super.buildUpsertRequest(request);
        if (request == null) {
            return null;
        }

        request.forEach((JsonNode node) -> {
            addUserFilterCriteria(node, "productFilterCriteria");
            addUserFilterCriteria(node, "customerFilterCriteria");
        });

        return (ArrayNode) request;

    }

    private static void addUserFilterCriteria(JsonNode request, String fieldName) {
        JsonNode criteriaNode = request.get(fieldName);
        if (criteriaNode != null && criteriaNode.isObject()) {
            RequestUtil.addAdvancedCriteria((ObjectNode) criteriaNode);
            ((ObjectNode) request).put(fieldName, criteriaNode.toString());
        }
    }

    @Override
    protected List<JsonNode> doUpsert(ArrayNode request, boolean rawPost) {
        ArrayNode updateList = new ArrayNode(JsonNodeFactory.instance);
        ArrayNode addList = new ArrayNode(JsonNodeFactory.instance);

        request.forEach((JsonNode node) -> {

            String loginName = JsonUtil.getValueAsText(node.get(FIELD_USER_LOGINNAME));
            if (!StringUtils.isEmpty(loginName)) {


                ObjectNode toBeUpdated = getPfxClient().fetchFirstObject(createPath(FETCH.getOperation(), USER.getTypeCode()),
                        FIELD_USER_LOGINNAME, loginName);

                if (toBeUpdated != null) {
                    String typedId = JsonUtil.getValueAsText(toBeUpdated.get(FIELD_TYPEDID));
                    Number version = JsonUtil.getNumericValue(toBeUpdated.get(FIELD_VERSION));
                    if (!StringUtils.isEmpty(typedId) && version != null) {
                        ((ObjectNode) node).put(FIELD_TYPEDID, typedId).put(FIELD_VERSION, version.intValue());
                        updateList.add(node);
                    } else {
                        addList.add(node);
                    }
                } else {
                    addList.add(node);
                }
            }
        });


        final ArrayNode results = new ArrayNode(JsonNodeFactory.instance)
                .addAll(getPfxClient().postBatch(getApiPath(true), updateList, false))
                .addAll(getPfxClient().postBatch(createPath(ADD.getOperation(), USER.getTypeCode(), BATCH.getOperation()), addList, false));

        if (results == null) {
            return Lists.newArrayList();
        }

        return Lists.newArrayList(results.iterator());
    }


}