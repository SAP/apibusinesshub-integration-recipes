package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;


public class GenericDeleter implements IPFXObjectDeleter, IPFXObjectFilterRequester {

    private final PFXOperationClient pfxClient;
    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;


    public GenericDeleter(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
        this.extensionType = extensionType;
    }

    @Override
    public String delete(JsonNode request) {
        this.validateCriteria(request, true, PFXJsonSchema.DELETE_REQUEST);
        RequestUtil.addAdvancedCriteria((ObjectNode) request);
        ObjectNode rootNode = new ObjectNode(JsonNodeFactory.instance);
        rootNode.set("filterCriteria", request);
        rootNode = RequestFactory.buildDeleteRequest(typeCode, rootNode, extensionType);
        return JsonUtil.getValueAsText(JsonUtil.getFirstDataNode(
                pfxClient.doPost(RequestPathFactory.buildDeletePath(extensionType, typeCode), rootNode)));

    }

}