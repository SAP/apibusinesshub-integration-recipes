package net.pricefx.adapter.sap.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
    public static final String PROPERTY_PREFIX = "${property.";
    public static final String EXCHANGE_PROPERTY_PREFIX = "${exchangeProperty.";
    public static final String HEADER_PREFIX = "${header.";


    private StringUtil() {
    }

    public static String getPropertyNameFromExpression(String expression) {
        String result = getNameFromExpression(expression, PROPERTY_PREFIX);
        if (StringUtils.isEmpty(result)){
            return getNameFromExpression(expression, EXCHANGE_PROPERTY_PREFIX);
        } else {
            return result;
        }

    }

    public static String getHeaderNameFromExpression(String expression) {
        return getNameFromExpression(expression, HEADER_PREFIX);
    }

    private static String getNameFromExpression(String expression, String prefix) {
        if (!StringUtils.isEmpty(expression) && StringUtils.startsWith(expression,
                prefix)) {
            return StringUtils.removeEndIgnoreCase(StringUtils.removeStartIgnoreCase(expression, prefix), "}");
        }

        return StringUtils.EMPTY;
    }

}
