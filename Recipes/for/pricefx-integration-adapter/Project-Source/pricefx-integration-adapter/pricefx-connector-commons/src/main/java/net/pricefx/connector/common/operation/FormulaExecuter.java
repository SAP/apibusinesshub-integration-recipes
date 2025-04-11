package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.ResponseUtil;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.RequestValidationException;
import net.pricefx.pckg.client.okhttp.PfxClientException;
import net.pricefx.pckg.client.okhttp.PfxCommonService;
import net.pricefx.pckg.processing.ProcessingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.OperatorId.AND;
import static net.pricefx.connector.common.util.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.EXECUTE_FORMULA;
import static net.pricefx.connector.common.util.PFXOperation.FETCH;
import static net.pricefx.connector.common.util.PFXTypeCode.FORMULA;
import static net.pricefx.connector.common.util.RequestUtil.createSimpleFetchRequest;

public class FormulaExecuter implements IPFXObjectExecuter {
    private final String formulaName;
    private final PFXOperationClient pfxClient;

    public FormulaExecuter(PFXOperationClient pfxClient, String formulaName) {
        this.pfxClient = pfxClient;
        this.formulaName = formulaName;
    }

    @Override
    public JsonNode execute(JsonNode request) {
        validateRequest(request);
        JsonNode result;
        try {
            result = pfxClient.doPost(createPath(EXECUTE_FORMULA.getOperation(), formulaName),
                    (ObjectNode) request);
        } catch (ProcessingException ex) {
            if (ex.getCause() instanceof PfxClientException &&
                    !CollectionUtils.isEmpty(((PfxClientException) ex.getCause()).getServerMessages())) {

                throw new ConnectorException("Formula execution failed: " + String.join(",",
                        ((PfxClientException) ex.getCause()).getServerMessages()));
            }

            throw new ConnectorException("Formula execution failed: " + ex.getMessage() + "/" +
                    ex.getCause().getMessage() + "/" +
                    ex.getCause().getClass().getSimpleName());
        }

        ArrayNode results = new ArrayNode(JsonNodeFactory.instance);

        if (result == null) {
            return new ObjectNode(JsonNodeFactory.instance);
        } else {
            result.iterator().forEachRemaining((JsonNode obj) -> {
                if (JsonUtil.isObjectNode(obj)) {
                    ObjectNode resultNode = ((ObjectNode) obj).retain("resultName", "result", "message");
                    ResponseUtil.formatFormulaResponse(resultNode);
                    results.add(resultNode);
                } else {
                    throw new ConnectorException("Unknown response returned.");
                }

            });
            return results;
        }
    }

    private void validateRequest(JsonNode request) {
        if (StringUtils.isEmpty(formulaName)) {
            throw new ConnectorException("Missing formula");
        }

        if (!JsonUtil.isObjectNode(request)) {
            throw new RequestValidationException("Request must be object");
        }

        if (StringUtils.isEmpty(getFormulaId())) {
            throw new NoSuchElementException(formulaName + " is inactive or deleted.");
        }
    }

    public String getFormulaId() {
        if (StringUtils.isEmpty(formulaName)) return null;

        ObjectNode request = createSimpleFetchRequest(AND.getValue(), Arrays.asList(
                PfxCommonService.buildSimpleCriterion(FIELD_UNIQUENAME, EQUALS.getValue(), formulaName),
                PfxCommonService.buildSimpleCriterion(STATUS, EQUALS.getValue(), EntityStatus.ACTIVE.name())
        ));

        JsonNode fetchResult = pfxClient.doPost(createPath(FETCH.getOperation(), FORMULA.getTypeCode()), request);

        String typedId = null;
        if (JsonUtil.isObjectNode(fetchResult)) {
            typedId = JsonUtil.getValueAsText(fetchResult.get(FIELD_TYPEDID));
        } else if (JsonUtil.isArrayNode(fetchResult)) {

            TreeMap<String, String> map = new TreeMap<>();
            fetchResult.iterator().forEachRemaining((JsonNode n) -> {
                if (JsonUtil.isObjectNode(n)) {
                    map.put(JsonUtil.getValueAsText(n.get(FIELD_VALIDAFTER)),
                            JsonUtil.getValueAsText(n.get(FIELD_TYPEDID)));
                }
            });

            if (map.isEmpty()) {
                return null;
            }

            typedId = map.lastEntry().getValue();

        }

        if (StringUtils.isEmpty(typedId)) return null;

        String[] splits = typedId.split("\\.");
        if (splits == null || splits.length == 0 || StringUtils.isEmpty(splits[0])) {
            return null;
        }

        return splits[0];
    }
}