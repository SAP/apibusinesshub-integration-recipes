package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;

public interface IPFXObjectBulkLoader {
    JsonNode bulkLoad(JsonNode request, boolean validate);
}
