package net.pricefx.connector.common.operation;

import net.pricefx.connector.common.connection.PFXOperationClient;
import net.pricefx.connector.common.util.FileUtil;
import net.pricefx.connector.common.util.PFXOperation;
import net.pricefx.connector.common.util.PFXTypeCode;

import static net.pricefx.connector.common.util.Constants.DEFAULT_DATA_FILE_NAME;


public class DataFileUploader extends GenericFileUploader {

    public DataFileUploader(PFXOperationClient pfxClient, PFXTypeCode typeCode, String uniqueId) {
        super(pfxClient, PFXOperation.DATA_FILE_IMPORT, FileUtil.MediaType.XLSX, DEFAULT_DATA_FILE_NAME,
                okhttp3.MediaType.parse(FileUtil.MediaType.XLSX.getType()), typeCode.getFullTargetName(uniqueId), ".xlsx");

    }

}