package net.pricefx.connector.common.util;

import org.apache.commons.lang3.EnumUtils;

public enum FileUploadStatus {
    NOT_FOUND, //connector only status
    STARTED, DONE, INPROGRESS, ERROR, CANCELLED, POSTPROCESSING_DONE; // status from core

    public static FileUploadStatus validValueOf(String typeCode) {
        if (EnumUtils.isValidEnum(FileUploadStatus.class, typeCode)) {
            return FileUploadStatus.valueOf(typeCode);
        }
        return NOT_FOUND;
    }
}
