package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.JsonSchemaUtil;
import net.pricefx.connector.common.util.JsonUtil;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import net.pricefx.pckg.processing.ProcessingException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.JsonSchemaUtil.SCHEMA_PROPERTIES;
import static net.pricefx.connector.common.util.JsonSchemaUtil.STRING;
import static net.pricefx.connector.common.util.PFXOperation.DELETE;


public class GenericKeyDeleter implements IPFXObjectDeleter {

    private final PFXOperationClient pfxClient;
    private final PFXTypeCode typeCode;
    private final IPFXExtensionType extensionType;
    private final JsonNode schemaNode;


    public GenericKeyDeleter(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType) {
        this.pfxClient = pfxClient;
        this.typeCode = typeCode;
        this.extensionType = extensionType;

        schemaNode = JsonSchemaUtil.loadEmptySchema();
        if (!ArrayUtils.isEmpty(typeCode.getIdentifierFieldNames())) {
            for (String k : typeCode.getIdentifierFieldNames()) {
                JsonSchemaUtil.addSchemaAttribute(schemaNode, k, true, STRING);
            }
        }
    }

    @Override
    public String delete(JsonNode request) {
        validate(request);

        try {
            JsonNode result = pfxClient.post((ObjectNode) request, createPath(DELETE.getOperation(), typeCode.getTypeCode()), typeCode, extensionType);
            result = JsonUtil.getFirstDataNode(result);
            if (!StringUtils.isEmpty(JsonUtil.getValueAsText(result.get(typeCode.getIdentifierFieldNames()[0])))) {
                return "1";
            }
        } catch (ProcessingException ex) {
            return "0";
        }

        return "0";
    }

    private void validate(JsonNode request) {
        if (schemaNode != null && schemaNode.get(SCHEMA_PROPERTIES) != null) {
            JsonValidationUtil.validateExtraFields(schemaNode.get(SCHEMA_PROPERTIES), request);
        }

        Set<String> mandatory = ImmutableSet.copyOf(typeCode.getIdentifierFieldNames());
        JsonValidationUtil.validateMissingMandatoryAttributes((ObjectNode) request, mandatory, -1);
    }

}