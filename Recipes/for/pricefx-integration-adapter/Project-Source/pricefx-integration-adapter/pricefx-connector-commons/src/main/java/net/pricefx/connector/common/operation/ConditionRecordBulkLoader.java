package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.RequestFactory;
import net.pricefx.connector.common.util.RequestPathFactory;
import net.pricefx.connector.common.validation.RequestValidationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.pricefx.connector.common.util.PFXConstants.HEADER;
import static net.pricefx.connector.common.util.PFXTypeCode.CONDITION_RECORD;
import static net.pricefx.connector.common.util.PFXTypeCode.CONDITION_RECORD_STAGING;
import static net.pricefx.connector.common.validation.RequestValidationException.ErrorType.MISSING_MANDATORY_ATTRIBUTES;


public class ConditionRecordBulkLoader extends GenericBulkLoader {
    private final boolean superseded;


    public ConditionRecordBulkLoader(PFXOperationClient pfxClient, IPFXExtensionType extensionType, boolean superseded) {
        super(pfxClient, CONDITION_RECORD, extensionType, null);
        this.superseded = superseded;
    }

    @Override
    protected void validateData(JsonNode inputNode) {
        validateKeyFields(inputNode.get(HEADER));
        super.validateData(inputNode);
    }

    private void validateKeyFields(JsonNode headerNode) {
        Set<String> keyFields = new HashSet<>();
        Collections.addAll(keyFields, CONDITION_RECORD_STAGING.getIdentifierFieldNames());
        if (getExtensionType() != null) keyFields.addAll(getExtensionType().getBusinessKeys());

        if (headerNode != null && headerNode.isArray()) {
            List<String> headers = JsonUtil.getStringArray(headerNode);
            keyFields.removeAll(headers);
        }

        if (!keyFields.isEmpty()) {
            throw new RequestValidationException(MISSING_MANDATORY_ATTRIBUTES, keyFields.toString());
        }
    }

    @Override
    protected ObjectNode getRequestPayload(JsonNode request) {
        ObjectNode req = RequestFactory.buildBulkLoadRequest(CONDITION_RECORD_STAGING, (ObjectNode) request, getExtensionType());

        if (superseded) {
            ObjectNode splicing = new ObjectNode(JsonNodeFactory.instance);
            splicing.put("supersedeRecords", true);
            ObjectNode conditionRecords = new ObjectNode(JsonNodeFactory.instance);
            conditionRecords.set("splicing", splicing);
            ObjectNode options = new ObjectNode(JsonNodeFactory.instance);
            options.set("conditionRecords", conditionRecords);
            req.set("options", options);
        }

        return req;
    }

    @Override
    protected String getApiPath() {
        return RequestPathFactory.buildBulkLoadPath(getExtensionType(), CONDITION_RECORD_STAGING, null);
    }


}




