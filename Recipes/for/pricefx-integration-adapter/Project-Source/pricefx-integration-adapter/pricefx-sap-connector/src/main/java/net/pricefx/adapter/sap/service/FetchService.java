package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.*;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.RequestValidationException;
import net.pricefx.pckg.client.okhttp.PfxCommonService;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import static net.pricefx.adapter.sap.util.RequestUtil.convertRequestToJson;
import static net.pricefx.connector.common.util.PFXConstants.FIELD_UNIQUENAME;
import static net.pricefx.connector.common.util.PFXTypeCode.QUOTE;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;

public class FetchService {


    private final PFXTypeCode typeCode;
    private final PFXOperationClient pfxClient;
    private final IPFXExtensionType extensionType;

    private final String uniqueKey;

    public FetchService(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, String uniqueKey) {
        this.extensionType = extensionType;
        this.typeCode = typeCode;
        this.pfxClient = pfxClient;
        this.uniqueKey = uniqueKey;
    }

    public JsonNode fetch(long startRow, int pageSize, boolean validate, boolean formatted, Object input) {
        JsonNode request = convertRequestToJson(input);
        if (!JsonUtil.isObjectNode(request)) {
            throw new RequestValidationException("Input message must be a Json Object");
        }

        Iterable<ObjectNode> results;
        switch (typeCode) {
            case MANUALPRICELIST:
                results = new ManualPriceListFetcher(pfxClient, false).
                        fetch((ObjectNode) request, startRow, pageSize, validate, formatted);
                break;
            default:
                results = new GenericFetcher(pfxClient, typeCode, extensionType, uniqueKey, false).
                        fetch((ObjectNode) request, startRow, pageSize, validate, formatted);
                break;
        }
        return convertFetchResults(results);
    }

    public JsonNode fetchCount(Object input) {
        JsonNode request = convertRequestToJson(input);
        if (!JsonUtil.isObjectNode(request)) {
            throw new RequestValidationException("Input message must be a Json Object");
        }

        int result = new GenericFetcher(pfxClient, typeCode, extensionType, uniqueKey, false).fetchCount((ObjectNode) request);
        return new IntNode(result);

    }

    private JsonNode convertGetResults(Iterable<ObjectNode> results) {
        if (Iterables.isEmpty(results)) {
            return new ObjectNode(JsonNodeFactory.instance);
        } else if (Iterables.size(results) == 1) {
            return Iterables.get(results, 0);
        } else {
            ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
            results.forEach(arrayNode::add);
            return arrayNode;
        }
    }

    private JsonNode convertFetchResults(Iterable<ObjectNode> results) {
        if (Iterables.isEmpty(results)) {
            return new ObjectNode(JsonNodeFactory.instance);
        } else {
            ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
            results.forEach(arrayNode::add);
            return arrayNode;
        }
    }

    public JsonNode get(String key, String value, boolean fullResult, boolean formatted) {
        return get(key, value, fullResult, formatted, 0L, Constants.MAX_RECORDS);
    }

    public JsonNode get(String key, String value, boolean fullResult, boolean formatted, long startRow, int pageSize) {
        Iterable<ObjectNode> results;

        PFXTypeCode pfxTypeCode = this.typeCode;
        ObjectNode criterion;
        if (pfxTypeCode == PFXTypeCode.TYPEDID) {

            GenericFetcher fetcher = new GenericFetcher(pfxClient, value, uniqueKey, false);

            pfxTypeCode = fetcher.getTypeCode();
            ObjectNode result = fetcher.getById();

            if (!PFXTypeCode.isDataCollectionTypeCodes(typeCode) && pfxTypeCode != QUOTE) {
                return result;
            } else {
                String newValue = JsonUtil.getValueAsText(result.get(pfxTypeCode.getIdentifierFieldNames()[0]));
                newValue = StringUtils.isEmpty(newValue) ? value : newValue;
                criterion = RequestUtil.createSimpleFetchRequest(
                        PfxCommonService.buildSimpleCriterion(pfxTypeCode.getIdentifierFieldNames()[0], OperatorId.EQUALS.getValue(),
                                newValue));
            }
        } else {
            criterion = RequestUtil.createSimpleFetchRequest(buildSimpleCriterion(key, OperatorId.EQUALS.getValue(), value));
        }


        switch (pfxTypeCode) {
            case CONTRACT:
                results = new ContractFetcher(pfxClient, false).withFullResult(fullResult).
                        fetch(criterion, ImmutableList.of(FIELD_UNIQUENAME), false, formatted);
                break;
            case REBATEAGREEMENT:
                results = new RebateAgreementFetcher(pfxClient, false).withFullResult(fullResult).
                        fetch(criterion, ImmutableList.of(FIELD_UNIQUENAME), false, formatted);
                break;
            case DATAFEED:
            case DATASOURCE:
            case DATAMART:
                results = new GenericFetcher(pfxClient, pfxTypeCode, extensionType, null, false).
                        fetch(criterion, ImmutableList.of(pfxTypeCode.getIdentifierFieldNames()[0]), Collections.emptyList(),
                                startRow, pageSize, true, formatted);
                break;
            case QUOTE:
                results = new QuoteFetcher(pfxClient, false).withFullResult(fullResult).
                        fetch(criterion, ImmutableList.of(FIELD_UNIQUENAME), false, formatted);
                break;
            default:
                results = new GenericFetcher(pfxClient, pfxTypeCode, extensionType, value, false).
                        fetch(criterion, ImmutableList.of(pfxTypeCode.getIdentifierFieldNames()[0]), Collections.emptyList(),
                                startRow, pageSize, true, formatted);
                break;
        }

        return convertGetResults(results);

    }

    public JsonNode fetchMetadata(long startRow, int pageSize) {
        List<ObjectNode> results = new GenericMetadataFetcher(pfxClient, typeCode, extensionType).
                fetch(startRow, pageSize, uniqueKey);
        return convertFetchResults(results);
    }

}
