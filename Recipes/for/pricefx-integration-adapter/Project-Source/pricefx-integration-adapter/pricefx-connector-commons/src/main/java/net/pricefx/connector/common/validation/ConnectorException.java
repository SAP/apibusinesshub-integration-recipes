package net.pricefx.connector.common.validation;

import org.apache.commons.lang3.StringUtils;

import java.net.HttpURLConnection;

public class ConnectorException extends RuntimeException {
    private final ErrorType errorType;

    public ConnectorException(String message) {
        super(message);
        this.errorType = ErrorType.HTTP_INTERNAL_ERROR;
    }

    public ConnectorException(String message, Exception e) {
        super(message, e);
        this.errorType = ErrorType.HTTP_INTERNAL_ERROR;
    }

    public ConnectorException(ErrorType errorType) {
        this(errorType, StringUtils.EMPTY);
    }

    public ConnectorException(ErrorType errorType, String message) {
        super(StringUtils.isEmpty(message) ? errorType.getMessage() : (errorType.getMessage() + ": " + message));
        this.errorType = errorType;
    }

    public ConnectorException(int line, ErrorType errorType, String message) {
        super("Error in Line " + line + " - " + (StringUtils.isEmpty(message) ? errorType.getMessage() : (errorType.getMessage() + ": " + message)));
        this.errorType = errorType;
    }

    public int getStatusCode() {
        return errorType.getStatusCode();
    }

    public enum ErrorType {
        UNSUPPORTED_OPERATION("Unsupported Operation", HttpURLConnection.HTTP_UNSUPPORTED_TYPE),
        HTTP_INTERNAL_ERROR("Unexpected error", HttpURLConnection.HTTP_INTERNAL_ERROR),
        CONNECTION_INVALID("Cannot create PFX Partition from Auth Key", HttpURLConnection.HTTP_UNAUTHORIZED),
        HOST_MISSING("Host not found", HttpURLConnection.HTTP_INTERNAL_ERROR),
        CONNECTION_ERROR("Failed to connect to PFX Partition", HttpURLConnection.HTTP_INTERNAL_ERROR),
        TABLE_NOT_FOUND("Table not found or inactive", HttpURLConnection.HTTP_INTERNAL_ERROR);

        private final String message;
        private final int statusCode;

        ErrorType(String message, int statusCode) {
            this.message = message;
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return message;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }


}
