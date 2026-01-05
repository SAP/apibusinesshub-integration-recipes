package net.pricefx.connector.common.util;


public enum NodeType {
    ARRAY("array"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    NUMBER("number"),
    NULL("null"),
    OBJECT("object"),
    STRING("string");

    private final String _value;

    private NodeType(String value) {
        this._value = value;
    }

    public String value() {
        return this._value;
    }
}