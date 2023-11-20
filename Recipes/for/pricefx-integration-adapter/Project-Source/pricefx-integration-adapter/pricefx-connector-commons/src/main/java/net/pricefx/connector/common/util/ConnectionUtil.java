package net.pricefx.connector.common.util;

import net.pricefx.connector.common.validation.ConnectorException;
import net.pricefx.pckg.client.okhttp.FileUploadProvider;
import net.pricefx.pckg.client.okhttp.PfxClientBuilder;
import net.pricefx.pckg.processing.ProcessingException;
import okhttp3.MediaType;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.function.Function;

import static net.pricefx.connector.common.util.Constants.DEFAULT_TIMEOUT;
import static net.pricefx.connector.common.util.Constants.MAX_RECORDS;
import static net.pricefx.connector.common.validation.ConnectorException.ErrorType.CONNECTION_INVALID;

public class ConnectionUtil {
    private static final String BASIC = "BASIC";
    private ConnectionUtil() {
    }

    public static PfxClientBuilder getPFXClientBuilder(String partitionName, String url, String appKey) {

        return (PfxClientBuilder) new PfxClientBuilder(url, partitionName)
                .appKey(appKey)
                .debug(BASIC)
                .chunkSize(200)
                .maxRetry(3)
                .timeout(DEFAULT_TIMEOUT);
    }

    public static PfxClientBuilder getPFXClientBuilder(String partitionName, String url, String user, String password, String token) {

        PfxClientBuilder builder = (PfxClientBuilder) new PfxClientBuilder(url, partitionName)
                .credentials(user, password)
                .jwtCredentials(token)
                .debug(BASIC)
                .chunkSize(MAX_RECORDS)
                .maxRetry(3)
                .timeout(DEFAULT_TIMEOUT);

        builder.insecure();
        return builder;
    }

    public static PfxClientBuilder getPFXClientBuilder(String partitionName, String url, String user, String password) {

        PfxClientBuilder builder = (PfxClientBuilder) new PfxClientBuilder(url, partitionName)
                .credentials(user, password)
                .debug(BASIC)
                .chunkSize(MAX_RECORDS)
                .maxRetry(3)
                .timeout(DEFAULT_TIMEOUT);

        builder.insecure();
        return builder;
    }


    public static Function<Exception, RuntimeException> createExceptionMapper(String sourceObject) {
        return createExceptionMapper(sourceObject, "Unable to execute " + sourceObject);
    }

    public static Function<Exception, RuntimeException> createExceptionMapper(String sourceObject, String message) {
        return ((Exception e) -> new ProcessingException(sourceObject, message, e));
    }

    public static String getHost(String url) throws MalformedURLException {
        if (url == null) throw new ConnectorException(ConnectorException.ErrorType.HOST_MISSING);

        URL aURL = new URL(url);

        if ((StringUtils.isEmpty(aURL.getProtocol()) ||
                StringUtils.isEmpty(aURL.getHost())))
            throw new ConnectorException(ConnectorException.ErrorType.HOST_MISSING);

        return aURL.getProtocol() + "://" + aURL.getHost();
    }

    public static String createPath(String... paths) {
        return String.join("/", paths);
    }


    public static boolean haveMetaData(PFXTypeCode typeCode, IPFXExtensionType extensionType) {
        if (typeCode == null || StringUtils.isEmpty(typeCode.getMetadataTypeCode())) {
            return false;
        }

        if (!(extensionType instanceof PFXLookupTableType)) {
            return true;
        }

        return !((PFXLookupTableType) extensionType).getLookupTableType().isSimple() &&
                ((PFXLookupTableType) extensionType).getLookupTableType() != PFXLookupTableType.LookupTableType.RANGE;
    }

    public static FileUploadProvider createFileUploadProvider(MediaType mediaType, final String fieldName, final String filename, final byte[] data) {
        return new FileUploadProvider() {
            @Override
            public String getFieldName() {
                return fieldName;
            }

            @Override
            public String getFilename() {
                return filename;
            }

            @Override
            public byte[] getData() {
                return data;
            }

            @Override
            public String getMediaType() {
                return mediaType.toString();
            }
        };
    }

    public static class Connection {
        private final String partition;
        private final String username;
        private final String password;

        public Connection(String key) {

            try {
                String secret = new String(Base64.getDecoder().decode(key));

                partition = secret.split("/")[0];
                username = secret.split("/")[1].split(":")[0];
                password = secret.split("/")[1].split(":")[1];

                if ((StringUtils.isEmpty(partition) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password))) {
                    throw new ConnectorException(CONNECTION_INVALID);
                }
            } catch (Exception ex) {
                throw new ConnectorException(CONNECTION_INVALID);
            }

        }

        public String getPartition() {
            return partition;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }


}
