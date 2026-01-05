package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.RequestUtil;
import net.pricefx.connector.common.validation.ConnectorException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.MAX_RECORDS;
import static net.pricefx.connector.common.util.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.ADD;
import static net.pricefx.connector.common.util.PFXTypeCode.ADVANCED_CONFIG;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;


public class AdvancedConfigurationUpsertor extends GenericSingleUpsertor {
    public AdvancedConfigurationUpsertor(PFXOperationClient pfxClient) {
        super(pfxClient, ADVANCED_CONFIG, null, null);
        setUpdateOnly(true);
    }


    @Override
    public String getApiPath(boolean batch) {
        if (isUpdateOnly()) {
            return super.getApiPath(batch);
        } else {
            return createPath(ADD.getOperation(), ADVANCED_CONFIG.getTypeCode());
        }
    }

    @Override
    protected ArrayNode buildUpsertRequest(JsonNode request) {
        if (super.buildUpsertRequest(request) == null) {
            return null;
        }

        String uniqueName = JsonUtil.getValueAsText(request.get(FIELD_UNIQUENAME));

        ObjectNode criterion = RequestUtil.createSimpleFetchRequest(buildSimpleCriterion(
                FIELD_UNIQUENAME, EQUALS.getValue(), uniqueName));

        List<ObjectNode> advConfigs =
                new GenericFetcher(getPfxClient(), ADVANCED_CONFIG, null, uniqueName, false).
                        fetch(criterion, ImmutableList.of(FIELD_UNIQUENAME), Collections.emptyList(),
                                0L, MAX_RECORDS, true, false);

        String value = JsonUtil.getValueAsText(request.get(FIELD_VALUE));
        ObjectNode updateRequest;
        if (CollectionUtils.isEmpty(advConfigs)) {
            //add
            updateRequest = new ObjectNode(JsonNodeFactory.instance)
                    .put(FIELD_UNIQUENAME, uniqueName)
                    .put(FIELD_VALUE, value);
            setUpdateOnly(false);
        } else {
            //update
            ObjectNode advConfig = advConfigs.get(0);
            String typedId = JsonUtil.getValueAsText(advConfig.get(FIELD_TYPEDID));
            Number number = JsonUtil.getNumericValue(advConfig.get(FIELD_VERSION));

            if (StringUtils.isEmpty(typedId) || number == null) {
                throw new ConnectorException("TypedID or version number not found in advanced config");
            }

            updateRequest = new ObjectNode(JsonNodeFactory.instance)
                    .put(FIELD_VERSION, number.intValue()).put(FIELD_TYPEDID, typedId)
                    .put(FIELD_VALUE, value);
        }
        return JsonUtil.createArrayNode(updateRequest);
    }

}