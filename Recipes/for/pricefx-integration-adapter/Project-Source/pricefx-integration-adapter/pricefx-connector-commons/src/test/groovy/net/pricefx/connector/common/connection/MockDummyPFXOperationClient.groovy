package net.pricefx.connector.common.connection

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.PFXOperation
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.pckg.client.okhttp.FileUploadProvider

import java.util.function.Function

class MockDummyPFXOperationClient extends PFXOperationClient {

    MockDummyPFXOperationClient() {
        super(ConnectionUtil.getPFXClientBuilder("dummy", "http://dummy.com", "dummy"), true)
    }


    @Override
    ObjectNode post(String apiPath, Object body, Function<Exception, RuntimeException> exceptionFunction) {
        return new ObjectNode(JsonNodeFactory.instance)
    }

    @Override
    void logout() {

    }

    @Override
    void doUpload(FileUploadProvider upload, PFXOperation operation, String apiPath, Map<String, String> queryParams) {

    }

    @Override
    ArrayNode doPostBatch(String apiPath, ArrayNode request) {
        return new ArrayNode(JsonNodeFactory.instance)
    }

    @Override
    JsonNode doPost(String apiPath, ObjectNode request) {
        return new ObjectNode(JsonNodeFactory.instance)
    }

    @Override
    Iterable<ObjectNode> doAction(String apiPath) {
        return new ArrayList<ObjectNode>()
    }

    @Override
    Iterable<ObjectNode> doFetch(PFXTypeCode typeCode, String apiPath, ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize) {
        return new ArrayList<ObjectNode>()
    }

    @Override
    void testConnection() {

    }

    @Override
    String getUploadSlot() {
        return ""
    }
}
