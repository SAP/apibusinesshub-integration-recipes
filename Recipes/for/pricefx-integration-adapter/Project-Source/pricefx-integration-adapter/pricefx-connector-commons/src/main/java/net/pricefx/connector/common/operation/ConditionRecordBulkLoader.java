package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
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


    public ConditionRecordBulkLoader(PFXOperationClient pfxClient, IPFXExtensionType extensionType) {
        super(pfxClient, CONDITION_RECORD, extensionType, null);
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
    protected ObjectNode getRequestPayload(JsonNode request){
        return RequestFactory.buildBulkLoadRequest(CONDITION_RECORD_STAGING, (ObjectNode) request, getExtensionType());
    }

    @Override
    protected String getApiPath(){
        return RequestPathFactory.buildBulkLoadPath(getExtensionType(), CONDITION_RECORD_STAGING, null);
    }



}




