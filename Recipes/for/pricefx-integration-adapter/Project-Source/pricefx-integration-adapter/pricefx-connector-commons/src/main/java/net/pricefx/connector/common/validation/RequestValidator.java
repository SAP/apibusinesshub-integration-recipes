package net.pricefx.connector.common.validation;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface RequestValidator {
    void validate(JsonNode input);
}
