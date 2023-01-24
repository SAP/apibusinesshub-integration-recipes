package net.pricefx.connector.common.operation;

import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.FileUtil;
import net.pricefx.connector.common.util.PFXOperation;

import java.util.HashMap;

import static net.pricefx.connector.common.util.ConnectionUtil.createPath;


public class GenericFileUploader implements IFileUploader {

    private final String uniqueId;
    private final PFXOperationClient pfxClient;
    private final FileUtil.MediaType mediaType;
    private final String defaultFileName;
    private final String supported;
    private final okhttp3.MediaType okhttp3MediaType;
    private final PFXOperation operation;

    public GenericFileUploader(PFXOperationClient pfxClient, PFXOperation operation, FileUtil.MediaType mediaType, String defaultFileName,
                               okhttp3.MediaType okhttp3MediaType, String uniqueId, String supported) {
        this.pfxClient = pfxClient;
        this.mediaType = mediaType;
        this.defaultFileName = defaultFileName;
        this.okhttp3MediaType = okhttp3MediaType;
        this.operation = operation;
        this.uniqueId = uniqueId;
        this.supported = supported;
    }

    public PFXOperationClient getPfxClient() {
        return pfxClient;
    }

    @Override
    public String upload(byte[] data) {
        validate(mediaType, data, supported);
        String uploadSlot = pfxClient.getUploadSlot();
        pfxClient.upload(operation, createApiPath(uploadSlot), data, okhttp3MediaType, defaultFileName, new HashMap<>());
        return uploadSlot;
    }

    private String createApiPath(String uploadSlot) {
        return createPath(operation.getOperation(), uploadSlot, uniqueId);
    }

}