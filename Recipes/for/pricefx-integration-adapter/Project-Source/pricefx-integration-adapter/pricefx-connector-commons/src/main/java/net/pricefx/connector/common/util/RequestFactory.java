package net.pricefx.connector.common.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import net.pricefx.connector.common.validation.RequestValidationException;
import net.pricefx.pckg.client.okhttp.PfxCommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.pricefx.connector.common.util.OperatorId.AND;
import static net.pricefx.connector.common.util.OperatorId.EQUALS;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXLookupTableType.LOWERBOUND;
import static net.pricefx.connector.common.util.PFXLookupTableType.UPPERBOUND;
import static net.pricefx.connector.common.util.PFXTypeCode.LOOKUPTABLE;
import static net.pricefx.connector.common.util.PFXTypeCode.QUOTE;
import static net.pricefx.pckg.client.okhttp.PfxCommonService.buildSimpleCriterion;

public class RequestFactory {
    private static final String OPTIONS = "options";
    private static final String FILTER_CRITERIA = "filterCriteria";
    private static final int MAX_HEADER_LENGTH = 1020;
    private static final int MAX_KEYS = 4;

    private RequestFactory() {
    }

    public static List<ObjectNode> buildAssignRoleRequest(List<String> existingRoles, List<String> newRoles, String user) {
        List<String> originalExistingRoles = ImmutableList.copyOf(existingRoles);

        existingRoles.removeAll(newRoles);
        List<ObjectNode> requests = buildAssignRoleRequest(existingRoles, false, user);

        newRoles.removeAll(originalExistingRoles);
        requests.addAll(buildAssignRoleRequest(newRoles, true, user));

        return requests;
    }

    public static List<ObjectNode> buildAssignRoleRequest(List<String> roles, boolean assign, String user) {
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }

        return roles.stream().map((String role) -> ((ObjectNode) new ObjectNode(JsonNodeFactory.instance)
                .put("assign", assign).put(FIELD_UNIQUENAME, role)
                .set("users", new ArrayNode(JsonNodeFactory.instance).add(user)))).collect(Collectors.toList());
    }

    public static ObjectNode buildDeleteRequest(PFXTypeCode typeCode, ObjectNode filter, IPFXExtensionType extensionType) {
        if (typeCode.isExtension()) {
            filter.get(FILTER_CRITERIA).get("criteria").forEach(
                    (JsonNode criteria) -> {
                        if (criteria.get(FIELD_VALUE) != null && criteria.get(FIELD_VALUE).isNumber()) {
                            ((ObjectNode) criteria).put(FIELD_VALUE, JsonUtil.getValueAsText(criteria.get(FIELD_VALUE)));
                        }
                    }
            );

            ObjectNode criterion = PfxCommonService.buildSimpleCriterion(FIELD_NAME, OperatorId.EQUALS.getValue(), extensionType.getTable());

            ObjectNode rootNode = new ObjectNode(JsonNodeFactory.instance);
            rootNode.set(FILTER_CRITERIA, RequestUtil.createSimpleFetchRequest(AND.getValue(), ImmutableList.of(
                    (ObjectNode) filter.get(FILTER_CRITERIA),
                    RequestUtil.createSimpleFetchRequest(AND.getValue(), ImmutableList.of(criterion)))));

            return rootNode;
        } else {
            return filter;
        }
    }

    public static void validateBulkLoadRequest(PFXTypeCode typeCode, IPFXExtensionType extensionType) {
        if (typeCode != null) {

            switch (typeCode) {
                case CONDITION_RECORD_STAGING:
                case CUSTOMEREXTENSION:
                case PRODUCTEXTENSION:
                case LOOKUPTABLE:
                    if (extensionType == null || StringUtils.isEmpty(extensionType.getTable()) ||
                            (typeCode == LOOKUPTABLE && (((PFXLookupTableType) extensionType).getLookupTableType()) == null)) {
                        throw new RequestValidationException("Table is not identified");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public static ObjectNode buildBulkLoadRequest(PFXTypeCode typeCode, ObjectNode request, IPFXExtensionType extensionType) {
        validateBulkLoadRequest(typeCode, extensionType);

        Set<String> idFields = new HashSet<>();

        if (typeCode != null) {
            idFields = Sets.newHashSet(typeCode.getIdentifierFieldNames());
            switch (typeCode) {
                case CONDITION_RECORD_STAGING:
                    ((ArrayNode) request.get(HEADER)).add("conditionRecordSetName");
                    request.get(FIELD_DATA).forEach((JsonNode row) -> ((ArrayNode) row).add(extensionType.getTable()));
                    return request;
                case CUSTOMEREXTENSION:
                case PRODUCTEXTENSION:
                    idFields = extensionType.getBusinessKeys();
                    ((ArrayNode) request.get(HEADER)).add(FIELD_NAME);
                    request.get(FIELD_DATA).forEach((JsonNode row) -> ((ArrayNode) row).add(extensionType.getTable()));

                    ObjectNode options = addJoinFieldsOptions(idFields, true);
                    addJoinFieldsLengthOptions(options, extensionType);
                    request.set(OPTIONS, options);
                    return request;
                case DATASOURCE:
                    request.set(OPTIONS, new ObjectNode(JsonNodeFactory.instance).put("direct2ds", true));
                    return request;
                case LOOKUPTABLE:
                    idFields = extensionType.getBusinessKeys();

                    //add lookup table id
                    ((ArrayNode) request.get(HEADER)).add("lookupTable");
                    request.get(FIELD_DATA).forEach((JsonNode row) -> ((ArrayNode) row).add(extensionType.getTable()));

                    switch (((PFXLookupTableType) extensionType).getLookupTableType()) {
                        case RANGE:
                            if (JsonUtil.getStringArray(request.get(HEADER)).contains(FIELD_NAME)) {
                                throw new RequestValidationException("field NAME should not exist in input message.");
                            }

                            List<JsonNode> headers = ImmutableList.copyOf(request.get(HEADER).iterator());
                            int lowerPos = Iterables.indexOf(headers, u -> LOWERBOUND.equals(u.textValue()));
                            int upperPos = Iterables.indexOf(headers, u -> UPPERBOUND.equals(u.textValue()));

                            ((ArrayNode) request.get(HEADER)).add(FIELD_NAME);
                            request.get(FIELD_DATA).forEach((JsonNode node) -> ((ArrayNode) node).add(JsonUtil.getValueAsText(node.get(lowerPos)) + "-" +
                                    JsonUtil.getValueAsText(node.get(upperPos))));
                            break;
                        case MATRIX:
                        case MATRIX2:
                        case MATRIX3:
                        case MATRIX4:
                        case MATRIX5:
                        case MATRIX6:
                            request.set(OPTIONS, addJoinFieldsOptions(idFields, false));
                            break;
                        default:
                            break;
                    }
                    return request;
                default:
                    break;

            }
        }

        request.set(OPTIONS, addJoinFieldsOptions(idFields, false));
        return request;
    }

    private static void addJoinFieldsLengthOptions(ObjectNode options, IPFXExtensionType extensionType) {
        if (extensionType == null || options == null || options.isNull()) {
            return;
        }

        if (extensionType.getBusinessKeys() != null && extensionType.getBusinessKeys().size() >= MAX_KEYS) {
            int maxLength = MAX_HEADER_LENGTH / (extensionType.getBusinessKeys().size() + 1);

            options.putArray("maxJoinFieldsLengths").add(
                    new ObjectNode(JsonNodeFactory.instance).put("joinField", FIELD_NAME)
                            .put("maxLength", maxLength));

            extensionType.getBusinessKeys().forEach(header ->
                    ((ArrayNode) options.get("maxJoinFieldsLengths")).add(
                            new ObjectNode(JsonNodeFactory.instance).put("joinField", header).put("maxLength", maxLength))
            );
        }
    }

    private static ObjectNode addJoinFieldsOptions(Set<String> idFields, boolean addName) {
        ObjectNode options = new ObjectNode(JsonNodeFactory.instance);

        if (!CollectionUtils.isEmpty(idFields)) {
            ArrayNode businessKeys = JsonUtil.createArrayNodeFromStrings(idFields);
            if (addName) businessKeys.add(FIELD_NAME);
            options.set("joinFields", businessKeys);
        }
        return options;

    }


    public static ObjectNode buildCreateRequest(PFXTypeCode typeCode, JsonNode request) {
        if (typeCode == QUOTE) {
            request = buildCreateQuoteRequest(request);
        }

        return (ObjectNode) request;
    }

    private static ObjectNode buildCreateQuoteRequest(JsonNode request) {
        if (!JsonUtil.isObjectNode(request)) return null;

        ArrayNode folders = (ArrayNode) request.get(FIELD_FOLDERS);
        ((ObjectNode) request).set(FIELD_FOLDERS, new ArrayNode(JsonNodeFactory.instance));
        request = new ObjectNode(JsonNodeFactory.instance).set(FIELD_QUOTE, request);

        if (folders != null) {
            for (JsonNode folder : folders) {
                if (folder.isTextual()) {
                    String lineId = JsonUtil.getRandomId(folder.textValue());
                    ObjectNode folderNode = new ObjectNode(JsonNodeFactory.instance).put(FIELD_LABEL, folder.textValue())
                            .put("folder", true)
                            .put(FIELD_LINEID, lineId);
                    ((ArrayNode) request.get(FIELD_QUOTE).get(FIELD_FOLDERS)).add(folderNode);
                }
            }
        }

        moveValueObject((ArrayNode) request.get(FIELD_QUOTE).get(FIELD_INPUTS));

        ArrayNode lineItems = (ArrayNode) request.get(FIELD_QUOTE).get(FIELD_LINEITEMS);
        if (lineItems != null) {
            lineItems.forEach((JsonNode lineItem) -> {
                if (JsonUtil.isObjectNode(lineItem)) {
                    moveValueObject((ArrayNode) lineItem.get(FIELD_INPUTS));
                }
            });
        }

        return (ObjectNode) request;
    }

    private static void moveValueObject(ArrayNode inputs) {
        if (inputs != null) {
            inputs.forEach((JsonNode input) -> {

                if (JsonUtil.isObjectNode(input) &&
                        JsonUtil.isObjectNode(input.get(FIELD_VALUEOBJECT)) && input.get(FIELD_VALUEOBJECT).size() > 0) {
                    if (!StringUtils.isEmpty(JsonUtil.getValueAsText(input.get(FIELD_VALUE)))) {
                        throw new RequestValidationException("Either value or valueObject is allowed");
                    }

                    ((ObjectNode) input).set(FIELD_VALUE, input.get(FIELD_VALUEOBJECT));
                    ((ObjectNode) input).remove(FIELD_VALUEOBJECT);
                }

            });
        }
    }

    public static ObjectNode buildFetchDataRequest(ObjectNode dataRequest, PFXTypeCode typeCode, IPFXExtensionType extensionType) {

        if (typeCode != null && typeCode.isConditionRecordTypeCodes()) {
            ObjectNode criterion = buildSimpleCriterion(FIELD_CONDITIONRECRODSETID, EQUALS.getValue(),
                    ((PFXConditionRecordType) extensionType).getTableId() + "");
            ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
            arrayNode.add(criterion);
            arrayNode.add(dataRequest);

            ObjectNode newDataRequest = new ObjectNode(JsonNodeFactory.instance);
            newDataRequest.set(FIELD_CRITERIA, arrayNode);
            newDataRequest.put("operator", AND.getValue());

            return newDataRequest;
        } else {
            return dataRequest;
        }

    }

    public static ObjectNode buildFetchMetadataRequest(PFXTypeCode typeCode, IPFXExtensionType extensionType, String uniqueKey) {

        ObjectNode criterion = null;


        if (typeCode == PFXTypeCode.PRICELISTITEM || typeCode == PFXTypeCode.MANUALPRICELISTITEM) {
            criterion = buildSimpleCriterion(FIELD_PLI_PRICELISTID, EQUALS.getValue(), uniqueKey);
        } else if (typeCode == PFXTypeCode.PRICEGRIDITEM) {
            criterion = buildSimpleCriterion(FIELD_PGI_PRICEGRIDID, EQUALS.getValue(), uniqueKey);
        } else if (!StringUtils.isEmpty(uniqueKey) && typeCode == PFXTypeCode.ROLE) {
            criterion = buildSimpleCriterion("module", EQUALS.getValue(), uniqueKey);
        } else if (extensionType != null && typeCode != null && typeCode.isConditionRecordTypeCodes()) {
            criterion = buildSimpleCriterion(FIELD_CONDITIONRECRODSETID, EQUALS.getValue(),
                    ((PFXConditionRecordType) extensionType).getTableId() + "");
        } else if (extensionType != null && extensionType.getTypeCode() != null && !StringUtils.isEmpty(extensionType.getTable())) {
            if (typeCode == LOOKUPTABLE) {
                criterion = buildSimpleCriterion("lookupTableId", EQUALS.getValue(), extensionType.getTable());
            } else {
                criterion = buildSimpleCriterion(FIELD_NAME, EQUALS.getValue(), extensionType.getTable());
            }
        }

        return criterion == null ? null : RequestUtil.createSimpleFetchRequest(criterion);

    }

    public static ArrayNode buildUpsertRequest(PFXTypeCode typeCode, IPFXExtensionType extensionType, JsonNode request) {
        if (request == null || (!request.isObject() && !request.isArray()) ||
                (JsonUtil.isArrayNode(request) && request.size() == 0)) {
            return null;
        }

        if (JsonUtil.isObjectNode(request)) {
            request = JsonUtil.createArrayNode(request);
        }

        if (typeCode.isExtension() && extensionType != null) {
            request.forEach((JsonNode node) -> addExtensionName(node, extensionType));
        }

        return (ArrayNode) request;
    }


    private static void addExtensionName(JsonNode node, IPFXExtensionType extensionType) {
        if (JsonUtil.isObjectNode(node) && !StringUtils.isEmpty(extensionType.getTable())) {
            ((ObjectNode) node).put(FIELD_NAME, extensionType.getTable());
        }
    }
}