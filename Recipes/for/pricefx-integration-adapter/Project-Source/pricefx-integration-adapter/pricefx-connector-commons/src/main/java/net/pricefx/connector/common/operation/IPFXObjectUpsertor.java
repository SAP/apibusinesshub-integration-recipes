package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface IPFXObjectUpsertor {
    List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString,
                          boolean isSimple, boolean showSystemFields, boolean rawPost);

}
