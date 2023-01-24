package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;

public interface IPFXObjectDeleter {
    String delete(JsonNode request);
}
