package net.pricefx.adapter.sap.service;

import com.apple.foundationdb.tuple.ByteArrayUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.ByteStreams;
import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.operation.DataFileUploader;
import net.pricefx.connector.common.operation.ProductImageUploader;
import net.pricefx.connector.common.util.PFXTypeCode;
import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.collections4.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

import static net.pricefx.connector.common.util.PFXConstants.FIELD_VALUE;

public class FileUploadService extends AbstractService {

    private final PFXTypeCode typeCode;
    private final String uniqueId;
    private final String extensionName;

    public FileUploadService(PFXOperationClient pfxClient, PFXTypeCode typeCode, String extensionName, String uniqueId) {

        super(pfxClient);
        this.typeCode = typeCode;
        this.uniqueId = uniqueId;
        this.extensionName = extensionName;
    }

    @Override
    public JsonNode execute(Object input) {
        byte[] data;
        try {
            byte[] bytes = ByteStreams.toByteArray((ByteArrayInputStream) input);
            data = getFileBytes(bytes);
            if (data == null || data.length == 0) {
                data = bytes;
            }
        } catch (Exception ex) {
            throw new RequestValidationException("File not found or invalid");
        }

        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        String slot;
        switch (typeCode) {
            case DATAFEED:
            case DATASOURCE:
                slot = new DataFileUploader(getPfxClient(), typeCode, extensionName).upload(data);
                break;
            case PRODUCTIMAGE:
                slot = new ProductImageUploader(getPfxClient(), uniqueId).upload(data);
                break;
            default:
                throw new UnsupportedOperationException("operation not supported");
        }
        node.put(FIELD_VALUE, slot);

        return node;
    }

    private byte[] getFileBytes(byte[] bytes) {
        List<byte[]> bytesSplits = ByteArrayUtil.split(bytes, new byte[]{13, 10, 13, 10});
        if (bytesSplits != null && bytesSplits.size() > 1) {
            bytesSplits = ByteArrayUtil.split(bytesSplits.get(1), new byte[]{13, 10, 45, 45});

            if (!CollectionUtils.isEmpty(bytesSplits)) {
                return bytesSplits.get(0);
            }
        }

        return new byte[0];
    }
}
