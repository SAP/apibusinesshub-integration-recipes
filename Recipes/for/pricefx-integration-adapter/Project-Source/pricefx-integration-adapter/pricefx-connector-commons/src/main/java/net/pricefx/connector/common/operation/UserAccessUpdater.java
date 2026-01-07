package net.pricefx.connector.common.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.*;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.connector.common.validation.JsonValidationUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;
import static net.pricefx.connector.common.util.PFXConstants.*;
import static net.pricefx.connector.common.util.PFXOperation.*;
import static net.pricefx.connector.common.util.PFXTypeCode.*;



public class UserAccessUpdater implements IPFXObjectUpsertor {

    private final PFXOperationClient pfxClient;
    private final String uniqueId;
    private final PFXTypeCode typeCode;
    private final JsonNode schema;


    public UserAccessUpdater(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId, JsonNode schema) {
        if (typeCode != ROLE && typeCode != BUSINESSROLE && typeCode != USERGROUP) {
            throw new UnsupportedOperationException("Type code is not supported");
        }

        this.pfxClient = pfxClient;
        this.uniqueId = uniqueId;
        this.typeCode = typeCode;

        this.schema = (schema != null) ? schema :
                JsonSchemaUtil.loadSchema(PFXJsonSchema.USER_ACCESS_REQUEST, typeCode, null, null, false, false, false, true);

    }

    protected void validateRequest(JsonNode request) {
        if (StringUtils.isEmpty(uniqueId)) {
            throw new ConnectorException("Missing User's Login Name. Please set Unique Key Dynamic Property");
        }

        ObjectNode user = pfxClient.fetchFirstObject(createPath(FETCH.getOperation(), USER.getTypeCode()),
                FIELD_USER_LOGINNAME, uniqueId);
        if (user == null) {
            throw new ConnectorException("User does not exist");
        }

        final String id = JsonUtil.getValueAsText(user.get(FIELD_TYPEDID)).split("\\.")[0];
        if (StringUtils.isEmpty(id)) {
            throw new ConnectorException("User does not exist");
        }

        JsonValidationUtil.validatePayload(schema, request);
        JsonValidationUtil.validateExtraFields(schema, request);

        pfxClient.validateUserRoles(JsonUtil.getStringArray(request.get("add")), typeCode);
    }


    @Override
    public List<JsonNode> upsert(JsonNode request, boolean validate, boolean replaceNullKey, boolean convertValueToString, boolean isSimple, boolean showSystemFields, boolean rawPost) {

        if (validate) {
            validateRequest(request);
        }

        List<String> addList = JsonUtil.getStringArray(request.get("add"));
        List<String> removeList = JsonUtil.getStringArray(request.get("remove"));

        List<ObjectNode> requests = new ArrayList<>();
        requests.addAll(RequestFactory.buildAssignRoleRequest(addList, true, uniqueId));
        requests.addAll(RequestFactory.buildAssignRoleRequest(removeList, false, uniqueId));


        requests.forEach((ObjectNode req) ->
                pfxClient.doPost(getOperation(typeCode).getOperation(), req)
        );


        return ImmutableList.of(new ObjectNode(JsonNodeFactory.instance).put(FIELD_VALUE, Boolean.TRUE.toString()));
    }

    private PFXOperation getOperation(PFXTypeCode typeCode) {
        switch (typeCode) {
            case BUSINESSROLE:
                return ASSIGN_BUSINESS_ROLE;
            case ROLE:
                return ASSIGN_ROLE;
            case USERGROUP:
                return ASSIGN_GROUP;
            default:
                throw new UnsupportedOperationException("User Access Type not supported");
        }
    }

}