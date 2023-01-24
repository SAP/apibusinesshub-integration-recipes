package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;

public interface IPFXObjectActioner {
    JsonNode action();

    String getSuccessResponse();

    String getFailedResponse();
}
