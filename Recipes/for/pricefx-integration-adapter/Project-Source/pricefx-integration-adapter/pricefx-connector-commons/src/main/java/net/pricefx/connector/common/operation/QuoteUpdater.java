package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.JsonSchemaUtil.createMetadataFields;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXConstants.WorkflowStatus.DRAFT;
import static net.pricefx.connector.common.util.PFXOperation.FETCH_QUOTE;
import static net.pricefx.connector.common.util.PFXTypeCode.QUOTE;

public class QuoteUpdater implements IPFXObjectUpsertor, ICalculableObjectUpsertor {

    private final PFXOperationClient pfxClient;
    private final JsonNode initialSchema;


    public QuoteUpdater(PFXOperationClient pfxClient, JsonNode schema) {
        this.pfxClient = pfxClient;
        this.initialSchema = schema;
    }

    public List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields) {
        String uniqueName = JsonUtil.getValueAsText(request.get(FIELD_UNIQUENAME));

        JsonNode quote = pfxClient.action(createPath(FETCH_QUOTE.getOperation(), uniqueName));

        boolean save = false;
        PFXJsonSchema pfxJsonSchema = PFXJsonSchema.QUOTE_UPDATE_REQUEST;
        try {
            save = (DRAFT == PFXConstants.WorkflowStatus.valueOf(quote.get(FIELD_WORKFLOWSTATUS).textValue()));
            if (save) {
                pfxJsonSchema = PFXJsonSchema.QUOTE_UPSERT_REQUEST;
            }
        } catch (Exception ex) {
            //ignore
        }

        Iterable<ObjectNode> metadata = pfxClient.doFetchMetadata(QUOTE, null, null);
        replaceAttributeExtension(request, metadata);
        List<Pair<String, String>> attributes = createMetadataFields(metadata);

        JsonNode schema = (initialSchema != null) ? initialSchema :
                JsonSchemaUtil.loadSchema(pfxJsonSchema, QUOTE, null, attributes, false, false, false, true);
        JsonValidationUtil.validatePayload(schema, request);
        validateExtraFields(schema, request);

        //SAVE_QUOTE
        if (save) {
            interpolateQuote(quote, request);
            request = new ObjectNode(JsonNodeFactory.instance).set("quote", quote);
        } else { //UPDATE QUOTE
            ((ObjectNode) request).put(FIELD_TYPEDID, quote.get(FIELD_TYPEDID).textValue())
                    .put(FIELD_VERSION, quote.get(FIELD_VERSION).longValue());
        }

        doUpdate(request, save);
        quote = pfxClient.action(createPath(FETCH_QUOTE.getOperation(), uniqueName));
        ResponseUtil.formatResponse(QUOTE, null, (ObjectNode) quote, convertValueToString);
        return ImmutableList.of(quote);

    }

    public static void interpolateQuote(JsonNode quote, JsonNode request) {
        if (!JsonUtil.isObjectNode(quote) || StringUtils.isEmpty(JsonUtil.getValueAsText(quote.get(FIELD_VERSION))) ||
                StringUtils.isEmpty(JsonUtil.getValueAsText(quote.get(FIELD_TYPEDID)))) {
            throw new ConnectorException("Quote to be updated does not exist");
        }

        request.fieldNames().forEachRemaining((String fieldName) -> {
            if (FIELD_INPUTS.equals(fieldName)) {
                JsonUtil.updateInputs(quote.get(FIELD_INPUTS), request.get(FIELD_INPUTS));
            } else if (FIELD_LINEITEMS.equals(fieldName)) {
                request.get(FIELD_LINEITEMS).forEach((JsonNode lineItem) -> {
                    String lineId = JsonUtil.getValueAsText(lineItem.get(FIELD_LINEID));
                    quote.get(FIELD_LINEITEMS).forEach((JsonNode quoteLineItem) -> {
                        if (lineId.equals(JsonUtil.getValueAsText(quoteLineItem.get(FIELD_LINEID)))) {
                            JsonUtil.updateInputs(quoteLineItem.get(FIELD_INPUTS), lineItem.get(FIELD_INPUTS));
                        }
                    });
                });
            } else {
                ((ObjectNode) quote).set(fieldName, request.get(fieldName));
            }
        });

    }

    private JsonNode doUpdate(JsonNode request, boolean save) {
        if (JsonUtil.isObjectNode(request)) {
            if (save) {
                return pfxClient.doPost(PFXOperation.SAVE_QUOTE.getOperation(), (ObjectNode) request);
            } else {
                return pfxClient.doPost(createPath(PFXOperation.UPDATE.getOperation(), QUOTE.getTypeCode()), (ObjectNode) request);
            }
        }

        throw new UnsupportedOperationException("Request is not a valid object");
    }
}