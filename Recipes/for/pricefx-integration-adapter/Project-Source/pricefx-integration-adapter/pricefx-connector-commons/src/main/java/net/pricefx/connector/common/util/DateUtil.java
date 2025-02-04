package net.pricefx.connector.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DEFAULT_TIMEZONE = "GMT";

    private DateUtil() {
    }

    public static String getFormattedDateTime(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(DATETIME_FORMAT).format(date);
    }

    public static String getFormattedDate(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static String getTrimmedDate(String dateTime) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(DATE_FORMAT).format(getDateTime(dateTime));
    }

    public static boolean isAfter(String date1, String date2) throws ParseException {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);

            return d1.after(d2);
        } catch (Exception ex) {
            throw new ParseException("Invalid date format", 0);
        }
    }

    public static boolean isAfterTimestamp(String date1, String date2) throws ParseException {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);

            return d1.after(d2);
        } catch (Exception ex) {
            throw new ParseException("Invalid date format", 0);
        }
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        return sdf.format(new Date());
    }

    public static Date getDateTime(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
            format.setLenient(false);

            return format.parse(dateStr);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Date getDate(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);

            return format.parse(dateStr);
        } catch (Exception ex) {
            return null;
        }
    }

}
