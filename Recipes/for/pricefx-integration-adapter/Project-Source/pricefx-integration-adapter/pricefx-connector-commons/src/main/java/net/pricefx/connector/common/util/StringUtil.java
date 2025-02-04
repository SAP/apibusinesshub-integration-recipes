package net.pricefx.connector.common.util;

import net.pricefx.connector.common.validation.RequestValidationException;
import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    private StringUtil() {
    }

    public static String getIdFromTypedId(String typedId) {
        if (StringUtils.isEmpty(typedId) || typedId.split("\\.") == null ||
                typedId.split("\\.").length < 2) {
            throw new RequestValidationException("TypedId is not valid");
        }

        return typedId.split("\\.")[0];
    }


}
