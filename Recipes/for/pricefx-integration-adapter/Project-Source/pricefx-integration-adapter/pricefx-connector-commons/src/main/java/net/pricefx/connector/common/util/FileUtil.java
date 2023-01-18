package net.pricefx.connector.common.util;

import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    private FileUtil() {
    }

    public static void checkSupportedFileType(InputStream is, MediaType mediaType) throws IOException {
        String type = null;
        switch (mediaType) {
            case IMAGE:
                String contentType = new Tika().detect(is);
                org.apache.tika.mime.MediaType tikaMediaType = org.apache.tika.mime.MediaType.parse(contentType);
                if (tikaMediaType != null) {
                    type = tikaMediaType.getType();
                }
                break;
            case XLSX:
                XlsxDetector detector = new XlsxDetector();
                tikaMediaType = detector.detect(is, new Metadata());
                if (tikaMediaType != null) {
                    type = tikaMediaType.getType() + "/" + tikaMediaType.getSubtype();
                }
                break;
            default:
                throw new RequestValidationException(RequestValidationException.ErrorType.INVALID_FILE_TYPE,
                        "Only " + mediaType + " files are supported");
        }

        if (type == null || !mediaType.getType().equalsIgnoreCase(type)) {
            throw new RequestValidationException(RequestValidationException.ErrorType.INVALID_FILE_TYPE,
                    "Only " + mediaType + " files are supported");
        }
    }

    public enum MediaType {

        IMAGE("image"), XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        private final String type;

        MediaType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }


}
