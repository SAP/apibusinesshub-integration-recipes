package net.pricefx.connector.common.util;

public class Constants {
    public static final String DEFAULT_URL = "http://docker:8010";

    public static final String SUCCESSFUL_STATUS_CODE = "200";

    public static final String URL_PROPERTY = "URL";

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String TOKEN = "TOKEN";
    public static final String PARTITION = "PARTITION";
    public static final String PAGE = "PAGE";
    public static final String PAGE_SIZE = "PAGE_SIZE";
    public static final String INCREMENTAL_LOAD_DATE = "INCREMENTAL_LOAD_DATE";
    public static final String UNIQUE_KEY = "UNIQUE_KEY";
    public static final String UPLOAD_SLOT = "UPLOAD_SLOT";
    public static final String MODULE_NAME = "MODULE_NAME";

    /*
     * page size
     */
    public static final int MAX_RECORDS = 500;

    public static final int MAX_UPSERT_RECORDS = 500;


    public static final int MAX_METADATA_RECORDS = 1000;

    /*
     * max bulk load records
     */
    public static final int MAX_BULKLOAD_RECORDS = 200000;

    /**
     * max line items in a quote supported now
     */
    public static final int MAX_CALC_LINEITEMS = 15;


    public static final int DEFAULT_TIMEOUT = 600;
    public static final String OAUTH_TOKEN_TYPE = "Bearer";


    public static final String DEFAULT_IMAGE_NAME = "upload.png";
    public static final String DEFAULT_UPLOAD_FIELDNAME = "file";

    public static final String DEFAULT_DATA_FILE_NAME = "upload.xlsx";

    public static final String EXTENSION_SEPARATOR = "_____";

    private Constants() {
    }

}
