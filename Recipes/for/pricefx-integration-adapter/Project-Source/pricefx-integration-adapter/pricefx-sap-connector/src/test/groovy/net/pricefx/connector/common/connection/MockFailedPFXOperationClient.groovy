package net.pricefx.connector.common.connection

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.util.PFXOperation
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.pckg.client.okhttp.FileUploadProvider

import java.util.function.Function

class MockFailedPFXOperationClient extends PFXOperationClient {


    MockFailedPFXOperationClient(Builder builder) {
        super(builder, true)
    }


    @Override
    ObjectNode post(String apiPath, Object body, Function<Exception, RuntimeException> exceptionFunction) {
        throw new RuntimeException()
    }

    @Override
    void logout() {
        close()
    }

    @Override
    void doUpload(FileUploadProvider upload, PFXOperation operation, String apiPath, Map<String, String> queryParams) {
        throw new RuntimeException()
    }

    @Override
    ArrayNode doPostBatch(String apiPath, ArrayNode request) {
        throw new RuntimeException()
    }

    @Override
    JsonNode doPost(String apiPath, ObjectNode request) {
        throw new RuntimeException()
    }

    @Override
    Iterable<ObjectNode> doAction(String apiPath) {
        throw new RuntimeException()
    }

    @Override
    Iterable<ObjectNode> doFetch(PFXTypeCode typeCode, String apiPath, ObjectNode advancedCriteria, List<String> sortBy, List<String> valueFields, Long startRow, int pageSize) {
        throw new RuntimeException()
    }

    @Override
    void testConnection() {
        throw new RuntimeException()
    }

    @Override
    String getUploadSlot() {
        throw new RuntimeException()
    }
}
