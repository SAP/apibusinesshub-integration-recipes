package net.pricefx.connector.common.validation;

import org.apache.commons.lang3.StringUtils;

import java.net.HttpURLConnection;

import static net.pricefx.connector.common.util.Constants.MAX_CALC_LINEITEMS;

public class RequestValidationException extends RuntimeException {

    private final int httpStatusCode;

    public RequestValidationException(String message) {
        this(HttpURLConnection.HTTP_BAD_REQUEST, message);
    }

    public RequestValidationException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public RequestValidationException(ErrorType errorType) {
        this(errorType, StringUtils.EMPTY);
    }

    public RequestValidationException(ErrorType errorType, String message) {
        super(StringUtils.isEmpty(message) ? errorType.getMessage() : (errorType.getMessage() + ": " + message));
        this.httpStatusCode = HttpURLConnection.HTTP_BAD_REQUEST;
    }

    public RequestValidationException(int line, ErrorType errorType, String message) {
        super("Error in Line " + line + " - " + (StringUtils.isEmpty(message) ? errorType.getMessage() : (errorType.getMessage() + ": " + message)));
        this.httpStatusCode = HttpURLConnection.HTTP_BAD_REQUEST;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public enum ErrorType {
        NON_JSON_INPUT("Input must be Json"),
        EMPTY_INPUT_ERROR("Input is null!"),
        MISSING_MANDATORY_ATTRIBUTES("Missing Mandatory attributes"),
        KEY_EXCEED_MAX("Total length of keys (including extension table name) should be < 255 "),
        SCHEMA_VALIDATION_ERROR("Schema Validation failed. Malformed json message."),
        LINEITEMS_EXCEEDS("Too many lineitems. Allowed no of line items: " + MAX_CALC_LINEITEMS),
        INVALID_DATE_FORMAT("Invalid date format"),
        INVALID_NUMBER_FORMAT("Invalid number format"),
        INVALID_FILE_TYPE("Invalid file type"),
        EXPIRY_DATE_EARLIER("Expiry Date must be later than target date");


        private final String message;

        ErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }


}
