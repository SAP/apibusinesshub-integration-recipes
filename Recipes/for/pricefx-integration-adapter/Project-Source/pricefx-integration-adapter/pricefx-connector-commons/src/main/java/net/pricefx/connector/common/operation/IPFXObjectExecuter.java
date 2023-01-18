package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;

public interface IPFXObjectExecuter {
    JsonNode execute(JsonNode request);
}
