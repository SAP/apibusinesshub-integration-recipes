package net.pricefx.adapter.sap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.GenericUpsertor;
import net.pricefx.connector.common.operation.UserUpsertor;
import net.pricefx.connector.common.util.IPFXExtensionType;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.util.RequestPathFactory;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class UpsertService extends AbstractJsonRequestService {


    private final PFXTypeCode typeCode;

    private final String path;
    private final IPFXExtensionType extensionType;
    private final boolean simpleResult;
    private final boolean replaceNullWithEmpty;

    private final boolean showSystemFields;

    public UpsertService(PFXOperationClient pfxClient, PFXTypeCode typeCode, IPFXExtensionType extensionType, boolean simpleResult,
                         boolean showSystemFields, boolean replaceNullWithEmpty) {
        super(pfxClient);
        this.replaceNullWithEmpty = replaceNullWithEmpty;
        this.typeCode = typeCode;
        this.extensionType = extensionType;

        this.path = RequestPathFactory.buildUpsertPath(extensionType, typeCode);
        this.simpleResult = simpleResult;
        this.showSystemFields = showSystemFields;
    }

    @Override
    protected JsonNode execute(JsonNode request) {

        List<JsonNode> results;
        switch (typeCode) {
            case USER:
                results = new UserUpsertor(getPfxClient()).upsert(request, true, false, false, false, false);
                break;
            default:
                results = new GenericUpsertor(getPfxClient(), path, typeCode, extensionType, null).upsert(request, true, replaceNullWithEmpty, false, simpleResult, showSystemFields);
        }


        if (CollectionUtils.isEmpty(results)) {
            return new ObjectNode(JsonNodeFactory.instance);

        } else {
            if (simpleResult) {
                return results.get(0);
            }

            if (results.size() == 1) {
                return results.get(0);
            } else {
                ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
                results.forEach(arrayNode::add);
                return arrayNode;
            }
        }
    }
}
