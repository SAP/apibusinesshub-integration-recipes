package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import net.pricefx.connector.common.validation.QuoteRequestValidator;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.Constants.MAX_CALC_LINEITEMS;
import static net.pricefx.connector.common.util.JsonSchemaUtil.createMetadataFields;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.ADD_LINEITEM_QUOTE;
import static net.pricefx.connector.common.util.PFXOperation.FETCH_QUOTE;
import static net.pricefx.connector.common.util.PFXTypeCode.QUOTE;

public class QuoteCreator implements IPFXObjectUpsertor, ICalculableObjectUpsertor {

    private final PFXOperationClient pfxClient;
    private final JsonNode schema;

    private static final String SKUS = "skus";

    public QuoteCreator(PFXOperationClient pfxClient, JsonNode schema) {
        this.pfxClient = pfxClient;

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(QUOTE, null, null);

        List<Pair<String, String>> attributes = createMetadataFields(metadata);
        this.schema = (schema != null) ? schema :
                JsonSchemaUtil.loadSchema(PFXJsonSchema.QUOTE_CREATE_REQUEST, QUOTE, null, attributes,
                        false, false, false, true);
    }


    @Override
    public List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields) {
        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(QUOTE, null, null);
        JsonValidationUtil.validatePayload(schema, request);
        validateExtraFields(schema, request);
        replaceAttributeExtension(request, metadata);

        new QuoteRequestValidator().validate(request);

        request = RequestFactory.buildCreateRequest(QUOTE, request);

        ArrayNode folderItems = ((ArrayNode) request.get(FIELD_QUOTE).get(FIELD_FOLDERS));
        Map<String, String> folderMap = createFolderMap(folderItems);

        ArrayNode lineItems = (ArrayNode) request.get(FIELD_QUOTE).get(FIELD_LINEITEMS);

        int noOfLineItems = 0;
        for (JsonNode lineItem : lineItems) {
            noOfLineItems += lineItem.get(SKUS).size();
        }

        if (noOfLineItems > MAX_CALC_LINEITEMS) {
            throw new RequestValidationException(RequestValidationException.ErrorType.LINEITEMS_EXCEEDS);
        }

        ((ObjectNode) request.get(FIELD_QUOTE)).set(FIELD_LINEITEMS, folderItems);

        List<JsonNode> resp = new GenericUpsertor(pfxClient, PFXOperation.SAVE_QUOTE.getOperation(), QUOTE, null, null).upsert(request, false, false, convertValueToString, false, false);

        if (CollectionUtils.isEmpty(resp)) return new ArrayList<>();

        JsonNode saveQuoteResponse = JsonUtil.getFirstDataNode(resp.get(0));
        String uniqueName = JsonUtil.getValueAsText(saveQuoteResponse.get(FIELD_UNIQUENAME));

        if (StringUtils.isEmpty(uniqueName)) return new ArrayList<>();

        JsonNode quote = pfxClient.action(createPath(FETCH_QUOTE.getOperation(), uniqueName));

        if (!JsonUtil.isObjectNode(quote)) {
            throw new ConnectorException("Quote creation failed. Unexpected Response returned");
        }

        if (lineItems == null || lineItems.size() == 0) {
            ResponseUtil.formatResponse(QUOTE, null, (ObjectNode) quote, convertValueToString);
            return ImmutableList.of(quote);
        }

        for (JsonNode lineItem : lineItems) {
            String parentName = JsonUtil.getValueAsText(lineItem.get("parent"));
            String parentLineId = folderMap.get(parentName);

            ObjectNode addItemRequest = JsonUtil.buildObjectNode(Pair.of(FIELD_QUOTE, quote),
                            Pair.of(SKUS, lineItem.get(SKUS)))
                    .put("parent", (StringUtils.isEmpty(parentLineId)) ? parentName : parentLineId);


            JsonNode addItemResponse = pfxClient.doPost(ADD_LINEITEM_QUOTE.getOperation(), addItemRequest);

            quote = JsonUtil.getFirstDataNode(addItemResponse);

        }

        ObjectNode addItemRequest = JsonUtil.buildObjectNode(Pair.of(FIELD_QUOTE, quote));

        new GenericUpsertor(pfxClient, PFXOperation.SAVE_QUOTE.getOperation(), QUOTE, null, null).upsert(JsonUtil.createArrayNode(addItemRequest), false, false, convertValueToString, false, false);

        quote = pfxClient.action(createPath(FETCH_QUOTE.getOperation(), uniqueName));

        ResponseUtil.formatResponse(QUOTE, null, (ObjectNode) quote, convertValueToString);
        return ImmutableList.of(quote);
    }

    private Map<String, String> createFolderMap(ArrayNode folderItems) {
        Map<String, String> folderMap = new HashMap<>();

        folderItems.forEach((JsonNode folder) -> {
            String key = JsonUtil.getValueAsText(folder.get(FIELD_LABEL));
            String value = JsonUtil.getValueAsText(folder.get(FIELD_LINEID));
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                folderMap.put(key, value);
            }
        });
        return folderMap;
    }
}