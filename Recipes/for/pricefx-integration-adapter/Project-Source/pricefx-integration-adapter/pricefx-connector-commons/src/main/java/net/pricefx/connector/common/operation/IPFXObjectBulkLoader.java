package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;

public interface IPFXObjectBulkLoader {
    String bulkLoad(JsonNode request, boolean validate);
}
