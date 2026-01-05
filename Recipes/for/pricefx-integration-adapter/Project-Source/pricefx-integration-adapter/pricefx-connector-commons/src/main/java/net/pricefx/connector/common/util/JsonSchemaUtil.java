package net.pricefx.connector.common.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import net.pricefx.connector.common.operation.IPFXObjectFetcher;
import net.pricefx.connector.common.validation.ConnectorException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXJsonSchema.POST_REQUEST;
import static net.pricefx.connector.common.util.PFXTypeCode.*;

public class JsonSchemaUtil {
    public static final String ITEMS = "items";
    public static final String SCHEMA_PROPERTIES = "properties";
    public static final String SCHEMA_TYPE = "type";



    private JsonSchemaUtil() {
    }

    public static Set<String> getFields(JsonNode schemaNode) {
        if (schemaNode == null) {
            return SetUtils.emptySet();
        }

        if (schemaNode.get(ITEMS) != null && schemaNode.get(ITEMS).get(SCHEMA_PROPERTIES) != null) {
            schemaNode = schemaNode.get(ITEMS);
        }

        if (schemaNode.get(SCHEMA_PROPERTIES) != null) {
            return ImmutableSet.copyOf(schemaNode.get(SCHEMA_PROPERTIES).fieldNames());
        } else {
            return SetUtils.emptySet();
        }
    }

    public static List<Pair<String, String>> createMetadataFields(Iterable<ObjectNode> metadata) {
        List<Pair<String, String>> list = new ArrayList<>();

        if (metadata == null) return list;

        metadata.forEach((ObjectNode m) -> {
            Pair<String, String> field = Pair.of(JsonUtil.getValueAsText(m.get(FIELD_FIELDNAME)), getMetadataLabel(m));
            if (field != null) {
                list.add(field);
            }

        });

        return list;
    }

    private static String getMetadataLabel(JsonNode metadata) {
        String label = JsonUtil.getLabelTranslations(JsonUtil.getValueAsText(metadata.get("labelTranslations")));

        if (StringUtils.isEmpty(label)) {
            return JsonUtil.getValueAsText(metadata.get(FIELD_LABEL));
        } else {
            return label;
        }
    }

    public static void updateSchemaWithMetadata(JsonNode schema, List<Map<String, Object>> metadata) {
        if (!CollectionUtils.isEmpty(metadata)) {
            for (Map<String, Object> field : metadata) {
                addSchemaAttribute(schema, (String) field.get(FIELD_NAME), true,
                        (boolean) field.get("numeric") ? NodeType.NUMBER.value() : NodeType.STRING.value());
            }
        }
    }

    public static JsonNode loadSchema(PFXJsonSchema schema, boolean useValidation) {

        try {
            return new ObjectMapper().readTree(IPFXObjectFetcher.class.getResourceAsStream(
                    useValidation ? schema.getValidationPath() : schema.getPath()));

        } catch (Exception ex) {
            throw new ConnectorException("Cannot read response schema");
        }
    }

    public static void addSchemaAttribute(JsonNode schemaNode, String attributeName, boolean withType, String type) {
        ObjectNode typeNode = new ObjectNode(JsonNodeFactory.instance);

        if (withType) typeNode.put(SCHEMA_TYPE, type);

        if (schemaNode.get(SCHEMA_PROPERTIES) == null) {
            ((ObjectNode) schemaNode).set(SCHEMA_PROPERTIES, new ObjectNode(JsonNodeFactory.instance));
        }

        ((ObjectNode) schemaNode.get(SCHEMA_PROPERTIES)).set(attributeName, typeNode);
    }


    public static void updateSchemaWithMetadata(JsonNode schema, PFXTypeCode pfxTypeCode,
                                                IPFXExtensionType extensionType, List<Pair<String, String>> attributes, boolean showAdditionalKeys,
                                                boolean showAdditionalAttributes, boolean withType) {
        if (pfxTypeCode == null) return;

        if (pfxTypeCode == QUOTE) {
            //create quote (object)
            if (!CollectionUtils.isEmpty(attributes)) {
                for (Pair<String, String> attribute : attributes) {
                    addSchemaAttribute(schema, attribute.getKey(), withType, attribute.getValue());
                }
            }
        } else {
            if (pfxTypeCode == LOOKUPTABLE || pfxTypeCode.isConditionRecordTypeCodes()) {
                int additionalKeys = getAdditionalKeys(pfxTypeCode, extensionType, showAdditionalKeys);
                addSchemaAttributes(schema, "key", additionalKeys, withType);
            }

            int additionalAttributes = getAdditionalAttributes(pfxTypeCode, extensionType, showAdditionalAttributes);
            addSchemaAttributes(schema, "attribute", additionalAttributes, withType);

        }
    }


    private static int getAdditionalKeys(PFXTypeCode pfxTypeCode, IPFXExtensionType extensionType, boolean showAdditionalKeys) {
        if (extensionType == null || !showAdditionalKeys) {
            return 0;
        }

        if (pfxTypeCode != null && (pfxTypeCode == LOOKUPTABLE || pfxTypeCode.isConditionRecordTypeCodes())) {
            return extensionType.getAdditionalKeys();
        }

        return 0;
    }

    private static void addSchemaAttributes(JsonNode schemaNode, String prefix, int attributes, boolean withType) {
        if (attributes == 0) return;

        if (JsonUtil.isObjectNode(schemaNode.get(SCHEMA_PROPERTIES))) {
            for (int i = 1; i <= attributes; i++) {
                addSchemaAttribute(schemaNode, prefix + i, withType, NodeType.STRING.value());
            }
        } else if (JsonUtil.isObjectNode(schemaNode.get(ITEMS)) &&
                JsonUtil.isObjectNode(schemaNode.get(ITEMS).get(SCHEMA_PROPERTIES))) {
            for (int i = 1; i <= attributes; i++) {
                addSchemaAttribute(schemaNode.get(ITEMS), prefix + i, withType, NodeType.STRING.value());
            }
        }
    }

    private static int getAdditionalAttributes(PFXTypeCode pfxTypeCode, IPFXExtensionType extensionType, boolean showAdditionalAttributes) {
        if (!showAdditionalAttributes) return 0;

        if (pfxTypeCode != null) {
            switch (pfxTypeCode) {
                case PAYOUT:
                    return MAX_PAYOUT_ATTRIBUTES;
                case CONDITION_RECORD_ALL:
                case CONDITION_RECORD_HISTORY:
                case CONDITION_RECORD:
                    return MAX_CONDITION_ATTRIBUTES;
                case PRODUCT:
                case CUSTOMER:
                case ACTION:
                case ACTION_PLAN:
                case PRICERECORD:
                    return MAX_ATTRIBUTES;
                case PRODUCTEXTENSION:
                case CUSTOMEREXTENSION:
                    if (extensionType != null) {
                        return extensionType.getAdditionalAttributes();
                    }
                    return MAX_EXT_ATTRIBUTES;
                case LOOKUPTABLE:
                    if (extensionType != null && ((PFXLookupTableType) extensionType).getLookupTableType().isMatrix()) {
                        return extensionType.getAdditionalAttributes();
                    }
                    break;
                default:
                    break;

            }
        }

        return 0;
    }

    public static JsonNode loadEmptySchema() {
        return loadSchema(POST_REQUEST, false);
    }

    /**
     * get schema for validation. additional attributes are added from metadata
     *
     * @param schema
     * @param typeCode
     * @param extensionType
     * @param attributes
     * @param showAdditionalKeys
     * @param showAdditionalAttributes
     * @param withType
     * @return
     */
    public static JsonNode loadSchema(PFXJsonSchema schema, PFXTypeCode typeCode,
                                      IPFXExtensionType extensionType, List<Pair<String, String>> attributes, boolean showAdditionalKeys,
                                      boolean showAdditionalAttributes, boolean withType, boolean useValidation) {

        JsonNode schemaNode = loadSchema(schema, useValidation);

        updateSchemaWithMetadata(schemaNode, typeCode, extensionType, attributes, showAdditionalKeys, showAdditionalAttributes, withType);

        return schemaNode;

    }
}
