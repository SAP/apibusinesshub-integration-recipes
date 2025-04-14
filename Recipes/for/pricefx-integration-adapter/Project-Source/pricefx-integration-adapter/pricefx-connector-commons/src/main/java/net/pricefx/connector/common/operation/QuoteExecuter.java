package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.CustomOperation;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXConstants;
import net.pricefx.connector.common.util.RequestUtil;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.client.okhttp.PfxCommonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Map;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.UNIQUE_KEY;
import static net.pricefx.connector.common.util.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXConstants.WorkflowStatus.SUBMITTED;
import static net.pricefx.connector.common.util.PFXConstants.WorkflowStatus.WITHDRAWN;
import static net.pricefx.connector.common.util.PFXOperation.*;
import static net.pricefx.connector.common.util.PFXTypeCode.QUOTE;

public class QuoteExecuter extends AbstractGenericExecuter {
    private final CustomOperation customOperation;

    public QuoteExecuter(PFXOperationClient pfxClient, Map<String, String> parameters, CustomOperation customOperation) {
        super(pfxClient, parameters);
        this.customOperation = customOperation;
    }

    @Override
    protected void validateRequest(JsonNode request) {
        String objectId = getParameters().get(UNIQUE_KEY);
        if (StringUtils.isEmpty(objectId)) {
            throw new ConnectorException("Unique ID is not provided. Cannot execute Quote Action");
        }
    }

    @Override
    protected String buildPath(JsonNode request) {
        String uniqueName = getParameters().get(UNIQUE_KEY);

        switch (customOperation) {
            case CONVERT_DEAL:
                return createPath(CONVERT_QUOTE.getOperation(), uniqueName);
            case QUOTE_COPY:
                return createPath(COPY_QUOTE.getOperation(), uniqueName);
            case QUOTE_REVISION:
                return createPath(REVISE_QUOTE.getOperation(), uniqueName);
            case QUOTE_SUBMIT:
                return SUBMIT_QUOTE.getOperation();
            case QUOTE_WITHDRAW:
                JsonNode quote = getQuote(uniqueName);
                if (!JsonUtil.isObjectNode(quote)) {
                    return null;
                } else {
                    return getWithdrawQuoteUrl((ObjectNode) quote);
                }
            default:
                return null;
        }
    }

    @Override
    protected ObjectNode buildRequest(JsonNode request) {
        String uniqueName = getParameters().get(UNIQUE_KEY);

        switch (customOperation) {
            case QUOTE_SUBMIT:
                JsonNode quote = getPfxClient().action(createPath(FETCH_QUOTE.getOperation(), uniqueName));

                if (!JsonUtil.isObjectNode(quote)) {
                    return null;
                }
                return createQuoteSubmitRequest((ObjectNode) quote);
            case QUOTE_COPY:
            case QUOTE_REVISION:
            case QUOTE_WITHDRAW:
            case CONVERT_DEAL:
                return new ObjectNode(JsonNodeFactory.instance);
            default:
                return null;
        }
    }

    private ObjectNode createQuoteSubmitRequest(ObjectNode quoteNode) {
        return JsonUtil.buildObjectNode(Pair.of(FIELD_QUOTE, quoteNode));
    }

    @Override
    protected boolean isSuccess(JsonNode firstResult) {
        try {
            switch (customOperation) {
                case QUOTE_WITHDRAW:
                    return (WITHDRAWN == PFXConstants.WorkflowStatus.valueOf(JsonUtil.getValueAsText(firstResult.get("workflow").get(FIELD_WORKFLOWSTATUS))));
                case QUOTE_COPY:
                case QUOTE_REVISION:
                case CONVERT_DEAL:
                    return !StringUtils.isEmpty(JsonUtil.getValueAsText(firstResult.get(FIELD_UNIQUENAME)));
                case QUOTE_SUBMIT:
                    return (SUBMITTED == PFXConstants.WorkflowStatus.valueOf(JsonUtil.getValueAsText(firstResult.get(FIELD_WORKFLOWSTATUS))));
                default:
                    return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    protected String getSuccessResponse(JsonNode firstResult) {
        switch (customOperation) {
            case QUOTE_COPY:
            case QUOTE_REVISION:
                return JsonUtil.getValueAsText(firstResult.get(FIELD_UNIQUENAME));
            default:
                return Boolean.TRUE.toString();
        }
    }

    @Override
    protected String getFailedResponse(JsonNode firstResult) {
        return null;
    }

    private ObjectNode getQuote(String uniqueName) {
        if (StringUtils.isEmpty(uniqueName)) return null;

        ObjectNode request = RequestUtil.createSimpleFetchRequest(PfxCommonService.buildSimpleCriterion(FIELD_UNIQUENAME, EQUALS.getValue(), uniqueName));

        Iterable<ObjectNode> quotes = new GenericFetcher(getPfxClient(),
                createPath(FETCH.getOperation(), QUOTE.getTypeCode()),
                QUOTE, null, null, false).fetch(request, new ArrayList<>(),
                new ArrayList<>(), 0L, 1, false, false);

        return Iterables.get(quotes, 0);

    }

    private String getWithdrawQuoteUrl(ObjectNode quote) {
        if (quote == null) {
            return null;
        }
        String typedId = JsonUtil.getValueAsText(quote.get(FIELD_TYPEDID));
        typedId = getWorkflowCurrentStepId(typedId);

        if (StringUtils.isEmpty(typedId)) {
            throw new ConnectorException("Withdrawal not allowed for this quote. Please check quote status in Pricefx partition.");
        }

        return createPath(WITHDRAW_WORKFLOW.getOperation(), typedId);

    }

    private String getWorkflowCurrentStepId(String typedId) {
        if (StringUtils.isEmpty(typedId)) return StringUtils.EMPTY;

        JsonNode node = Iterables.get(
                getPfxClient().doAction(createPath(FETCH_WORKFLOW_DETAILS.getOperation(), typedId)), 0);
        return JsonUtil.getValueAsText(node.get("workflow").get("currentStepId"));
    }
}