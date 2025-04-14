package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Map;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;
import static net.pricefx.connector.common.util.OperatorId.AND;
import static net.pricefx.connector.common.util.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXConstants.JobStatus.PENDING;
import static net.pricefx.connector.common.util.PFXOperation.FETCH;
import static net.pricefx.connector.common.util.PFXOperation.RUN_DATALOAD;
import static net.pricefx.connector.common.util.PFXTypeCode.DATALOAD;
import static net.pricefx.connector.common.util.RequestUtil.createSimpleFetchRequest;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;

public class DataloadRunner extends AbstractGenericExecuter implements IPFXObjectFilterRequester {

    private final PFXTypeCode typeCode;
    private final DataloadType type;

    public DataloadRunner(PFXOperationClient pfxClient, PFXTypeCode typeCode, DataloadType type, Map<String, String> parameters) {
        super(pfxClient, parameters);
        this.typeCode = typeCode;
        this.type = type;
    }

    @Override
    protected void validateRequest(JsonNode request) {
        if (StringUtils.isEmpty(getParameters().get(UNIQUE_KEY))) {
            throw new RequestValidationException("Unique ID is not provided. Cannot execute " + type.name());
        }

        if (JsonUtil.isObjectNode(request) && (!request.isEmpty())) {
            validateCriteria(request, false, PFXJsonSchema.FILTER_REQUEST);

        }
    }

    private String getTargetName() {
        return typeCode.getFullTargetName(getParameters().get(UNIQUE_KEY));
    }

    @Override
    protected String buildPath(JsonNode request) {
        return RUN_DATALOAD.getOperation();
    }

    @Override
    protected ObjectNode buildRequest(JsonNode request) {

        // check if dataload exist for this  DM/ DS /DF
        ObjectNode req = createSimpleFetchRequest(AND.getValue(),
                ImmutableList.of(buildSimpleCriterion(FIELD_TYPE, EQUALS.getValue(), type.name()),
                        buildSimpleCriterion(type.getTargetField(), EQUALS.getValue(), getTargetName())));

        Iterable<ObjectNode> objectNodes = new GenericFetcher(getPfxClient(),
                createPath(FETCH.getOperation(), DATALOAD.getTypeCode()),
                DATALOAD, null, null, false).fetch(req, new ArrayList<>(),
                new ArrayList<>(), 0L, 1, false, false);


        if (IterableUtils.isEmpty(objectNodes)) {
            throw new ConnectorException("Dataload not found");
        }

        ObjectNode result = objectNodes.iterator().next();

        ObjectNode dataNode = new ObjectNode(JsonNodeFactory.instance);
        if (result != null && !StringUtils.isEmpty(JsonUtil.getValueAsText(result.get(FIELD_TYPEDID)))) {
            dataNode.put(FIELD_TYPEDID, JsonUtil.getValueAsText(result.get(FIELD_TYPEDID)));
            if (!StringUtils.isEmpty(JsonUtil.getValueAsText(result.get(FIELD_SOURCENAME)))) {
                dataNode.put(FIELD_SOURCENAME, JsonUtil.getValueAsText(result.get(FIELD_SOURCENAME)));
            }
            if (!StringUtils.isEmpty(JsonUtil.getValueAsText(result.get(FIELD_TARGETNAME)))) {
                dataNode.put(FIELD_TARGETNAME, JsonUtil.getValueAsText(result.get(FIELD_TARGETNAME)));
            }
            dataNode.put(FIELD_TYPE, JsonUtil.getValueAsText(result.get(FIELD_TYPE)));
        } else {
            throw new ConnectorException("Dataload not found");
        }

        if (JsonUtil.isObjectNode(request)) {
            dataNode.set("dtoFilter", request);
            RequestUtil.addAdvancedCriteria((ObjectNode) dataNode.get("dtoFilter"));
        }

        return dataNode;
    }


    @Override
    protected boolean isSuccess(JsonNode firstResult) {
        try {
            return (PFXConstants.JobStatus.valueOf(JsonUtil.getValueAsText(firstResult.get(STATUS))) == PENDING);
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    protected String getSuccessResponse(JsonNode firstResult) {
        return JsonUtil.getValueAsText(firstResult.get(FIELD_TYPEDID));
    }

    @Override
    protected String getFailedResponse(JsonNode firstResult) {
        return Boolean.FALSE.toString();
    }

    public enum DataloadType {

        TRUNCATE(FIELD_TARGETNAME), DS_INTERNAL_COPY(FIELD_TARGETNAME), DS_FLUSH(FIELD_TARGETNAME),
        CALCULATION(FIELD_TARGETNAME), DMM_CALCULATION(FIELD_TARGETNAME),
        DS_CUSTOMERS(FIELD_TARGETNAME), DS_PRODUCTS(FIELD_TARGETNAME);

        private final String targetField;

        DataloadType(String targetField) {
            this.targetField = targetField;
        }

        public String getTargetField() {
            return targetField;
        }
    }


}