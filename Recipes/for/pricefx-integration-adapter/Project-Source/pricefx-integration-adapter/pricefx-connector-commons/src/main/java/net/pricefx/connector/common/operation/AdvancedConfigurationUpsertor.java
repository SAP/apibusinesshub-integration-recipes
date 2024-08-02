package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.smartgwt.client.types.OperatorId;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.RequestUtil;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.MAX_RECORDS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.ADD;
import static net.pricefx.connector.common.util.PFXOperation.UPDATE;
import static net.pricefx.connector.common.util.PFXTypeCode.ADVANCED_CONFIG;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;


public class AdvancedConfigurationUpsertor implements IPFXObjectUpsertor {

    private final PFXOperationClient pfxClient;
    public AdvancedConfigurationUpsertor(PFXOperationClient pfxClient) {
        this.pfxClient = pfxClient;
    }


    @Override
    public List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields) {
        if (!JsonUtil.isObjectNode(request)){
            throw new RequestValidationException("Invalid upsert Advanced Config request");
        }

        String uniqueName = JsonUtil.getValueAsText(request.get(FIELD_UNIQUENAME));
        if (StringUtils.isEmpty(uniqueName)){
            throw new RequestValidationException("Invalid upsert Advanced Config request. Missing uniqueName!");
        }

        ObjectNode criterion = RequestUtil.createSimpleFetchRequest(buildSimpleCriterion(
                FIELD_UNIQUENAME, OperatorId.EQUALS.getValue(), uniqueName));

        List<ObjectNode> advConfigs =
                new GenericFetcher(pfxClient, ADVANCED_CONFIG, null, uniqueName, false).
                        fetch(criterion, ImmutableList.of(FIELD_UNIQUENAME), Collections.emptyList(),
                                0L, MAX_RECORDS, true, false);

        String value = JsonUtil.getValueAsText(request.get(FIELD_VALUE));

        if (CollectionUtils.isEmpty(advConfigs)){
            ObjectNode updateRequest = new ObjectNode(JsonNodeFactory.instance)
                    .put(FIELD_UNIQUENAME, uniqueName)
                    .put(FIELD_VALUE,value);
            JsonNode result = pfxClient.doPost(createPath(ADD.getOperation(), ADVANCED_CONFIG.getTypeCode()), updateRequest);

            List<JsonNode> list = new ArrayList<>();
            if (result == null) {
                return list;
            } else if (result.isArray()) {
                list.add(result.get(0));
            } else {
                list.add(result);
            }
            return list;

        } else {
            //update
            ObjectNode advConfig = advConfigs.get(0);
            String typedId = JsonUtil.getValueAsText(advConfig.get(FIELD_TYPEDID));
            Number number = JsonUtil.getNumericValue(advConfig.get(FIELD_VERSION));

            if (StringUtils.isEmpty(typedId) || number == null){
                throw new ConnectorException("TypedID or version number not found in advanced config");
            }

            ObjectNode updateRequest = new ObjectNode(JsonNodeFactory.instance)
                                            .put(FIELD_VERSION, number.intValue()).put(FIELD_TYPEDID, typedId)
                                            .put(FIELD_VALUE,value);
            JsonNode result = pfxClient.doPost(createPath(UPDATE.getOperation(), ADVANCED_CONFIG.getTypeCode()), updateRequest);

            List<JsonNode> list = new ArrayList<>();
            if (result == null) {
                return list;
            } else if (result.isArray()) {
                list.add(result.get(0));
            } else {
                list.add(result);
            }
            return list;

        }


    }

}