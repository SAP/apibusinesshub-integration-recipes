package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface IPFXObjectUpsertRequestValidator {
    void validate(JsonNode input);
}
