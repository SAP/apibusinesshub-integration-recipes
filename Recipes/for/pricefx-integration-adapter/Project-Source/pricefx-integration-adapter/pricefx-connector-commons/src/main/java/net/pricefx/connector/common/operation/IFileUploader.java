package net.pricefx.connector.common.operation;

import net.pricefx.connector.common.util.FileUtil;
import net.pricefx.connector.common.validation.RequestValidationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface IFileUploader {
    String upload(byte[] data) throws IOException;

    default void validate(FileUtil.MediaType mediaType, final byte[] data, String supported) {

        if (mediaType != null) {
            try {
                FileUtil.checkSupportedFileType(new ByteArrayInputStream(data), mediaType);
            } catch (IOException e) {
                throw new RequestValidationException(RequestValidationException.ErrorType.INVALID_FILE_TYPE,
                        "Only " + supported + " files are supported");
            }
        }
    }
}
