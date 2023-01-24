package net.pricefx.connector.common.connection

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import net.pricefx.connector.common.util.ConnectionUtil
import net.pricefx.connector.common.util.JsonUtil
import net.pricefx.connector.common.util.PFXOperation
import net.pricefx.connector.common.util.PFXTypeCode
import net.pricefx.pckg.client.okhttp.FileUploadProvider
import net.pricefx.pckg.processing.ProcessingException

import java.util.function.Function

import static net.pricefx.connector.common.util.PFXConstants.*

class MockFailedPFXOperationClient extends PFXOperationClient {


    MockFailedPFXOperationClient() {
        super(ConnectionUtil.getPFXClientBuilder("dummy", "http://dummy.com", "dummy"), true)
    }


    @Override
    ObjectNode post(String apiPath, Object body, Function<Exception, RuntimeException> exceptionFunction) {
        throw new RuntimeException()
    }

    @Override
    void logout() {

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
        if ("fetch/F".equals(apiPath)) {
            if (request.get("criteria").get(0).get("value").textValue() == "DUMMY") {
                return new ArrayNode(JsonNodeFactory.instance)
            }
            return JsonUtil.createArrayNode(new ObjectNode(JsonNodeFactory.instance).
                    put(FIELD_UNIQUENAME, "test").put(FIELD_VALIDAFTER, "2020-01-01")
                    .put(FIELD_TYPEDID, "1.F"))
        }

        throw new ProcessingException(request, "failed", new RuntimeException())
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
