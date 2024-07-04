package net.pricefx.connector.common.connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.util.ConnectionUtil;
import net.pricefx.connector.common.util.PFXOperation;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.client.okhttp.AuthV2EnabledPfxClient;
import net.pricefx.pckg.client.okhttp.FileUploadProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static net.pricefx.connector.common.util.JsonUtil.getData;
import static net.pricefx.connector.common.util.PFXOperation.TOKEN;

public class PFXOperationClient extends AuthV2EnabledPfxClient implements IPFXConnection {
    private final boolean keepConnectionAlive;
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";

    public PFXOperationClient(Builder builder, boolean keepConnectionAlive) {
        super(builder);
        this.keepConnectionAlive = keepConnectionAlive;
    }

    @Override
    public void logout(boolean forcedLogout, boolean jwt, boolean oauth) {
        if (forcedLogout || (!jwt && !oauth)) {
            if (oauth) {
                try {
                    super.deleteAccessToken(TOKEN.getOperation());
                } catch (Exception e) {
                    throw new ConnectorException("Token is already invalidated. Logout is not required.");
                }
            }
            doLogout();
        }
    }

    @Override
    public void testConnection() {
        testConnection(getPartition(), keepConnectionAlive);
    }

    @Override
    public Iterable<ObjectNode> doAction(String apiPath) {
        return getCommonService().get(apiPath, ConnectionUtil.createExceptionMapper(apiPath));
    }

    @Override
    public JsonNode doPostRaw(String apiPath, Object request) {
        try {
            return super.postRetry(apiPath, request);
        } catch (IOException e) {
            throw new ConnectorException("Unable to execute " + apiPath, e);
        }
    }

    @Override
    public JsonNode doPost(String apiPath, ObjectNode request) {
        JsonNode result = getCommonService().post(apiPath, request, ConnectionUtil.createExceptionMapper(apiPath));
        return getData(result);
    }

    @Override
    public Iterable<ObjectNode> doFetch(PFXTypeCode typeCode, String apiPath, ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize) {
        return getCommonService().fetch(apiPath, advancedCriteria,
                ConnectionUtil.createExceptionMapper(typeCode.getLabel(), "Unable to fetch " + typeCode.getLabel())
        ).sortBy(sortBy).valueFields(valueFields).limit(pageSize + startRow).start(startRow);
    }

    @Override
    public ArrayNode doPostBatch(String apiPath, ArrayNode request) {
        return getCommonService().postBatch(apiPath, request, ConnectionUtil.createExceptionMapper(apiPath));
    }

    @Override
    public void doUpload(FileUploadProvider upload, PFXOperation operation, String apiPath, Map<String, String> queryParams) {
        getCommonService().upload(apiPath, queryParams, upload,
                ConnectionUtil.createExceptionMapper(operation.name(), "Unable to upload file"));
    }

    @Override
    public void closeConnection() {
        close();
    }

    @Override
    public ObjectNode getAccessToken(String apiPath, Object body) throws IOException {
        ObjectNode results = super.getAccessToken(apiPath, body);
        return new ObjectNode(JsonNodeFactory.instance)
                .put(ACCESS_TOKEN, results.get(ACCESS_TOKEN).textValue())
                .put(REFRESH_TOKEN, results.get(REFRESH_TOKEN).textValue());

    }


}
