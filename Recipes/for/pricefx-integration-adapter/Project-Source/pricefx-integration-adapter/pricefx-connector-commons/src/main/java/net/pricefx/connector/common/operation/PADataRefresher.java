package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXConstants;
import net.pricefx.connector.common.validation.ConnectorException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.INCREMENTAL_LOAD_DATE;
import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_TYPEDID;
import static net.pricefx.connector.common.util.PFXConstants.JobStatus.PENDING;
import static net.pricefx.connector.common.util.PFXConstants.STATUS;
import static net.pricefx.connector.common.util.PFXOperation.REFRESH_DATAMART;
import static net.pricefx.connector.common.util.PFXTypeCode.DATAMART;

public class PADataRefresher extends AbstractGenericExecuter {

    public PADataRefresher(PFXOperationClient pfxClient, Map<String, String> parameters) {
        super(pfxClient, parameters);
    }

    @Override
    protected void validateRequest(JsonNode request) {
        String objectId = getParameters().get(UNIQUE_KEY);
        if (StringUtils.isEmpty(objectId)) {
            throw new ConnectorException("Unique ID is not provided. Cannot execute Refresh");
        }
    }

    @Override
    protected String buildPath(JsonNode request) {
        String objectId = getParameters().get(UNIQUE_KEY);
        return createPath(REFRESH_DATAMART.getOperation(), DATAMART.getFullTargetName(objectId));
    }

    @Override
    protected ObjectNode buildRequest(JsonNode request) {
        String incLoadDate = getParameters().get(INCREMENTAL_LOAD_DATE);

        ObjectNode dataNode = new ObjectNode(JsonNodeFactory.instance);

        if (!StringUtils.isEmpty(incLoadDate)) {
            dataNode.put("incremental", true);
            dataNode.put("incLoadDate", incLoadDate);
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


}