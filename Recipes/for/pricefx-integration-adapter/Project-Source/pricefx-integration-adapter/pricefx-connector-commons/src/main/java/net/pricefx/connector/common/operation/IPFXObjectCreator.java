package net.pricefx.connector.common.operation;


import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IPFXObjectCreator {
    ObjectNode create(ObjectNode request);
}
