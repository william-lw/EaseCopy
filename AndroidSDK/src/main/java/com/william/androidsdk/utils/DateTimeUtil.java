package com.william.androidsdk.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static final SimpleDateFormat DATE_FORMAT_YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_YYYYMMDD_HHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    /**
     * format the time according to the current locale and the user's 12-/24-hour clock preference
     *
     * @param context
     * @return
     */
    public static String getCurrentTimeString(Context context) {
        return DateFormat.getTimeFormat(context).format(new Date());
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT_YYYYMMDD_HHMMSS.format(date);
    }

    public static String formatDate(Date date, SimpleDateFormat simpleDateFormat) {
        if (date == null) {
            return "";
        }
        return simpleDateFormat.format(date);
    }

    public static String formatTime(Context context, long allSecond) {
        long hour_value = allSecond / 3600;
        long minute_value = allSecond % 3600 / 60;
        long second_value = allSecond % 3600 % 60;
        String whitespace_symbol = " ";

        String hour_symbol = "h";
        String minute_symbol = "m";
        String second_symbol = "s";

        if (hour_value > 0) {
            return hour_value + hour_symbol + whitespace_symbol + minute_value + minute_symbol;
        } else if (minute_value > 0) {
            return minute_value + minute_symbol + whitespace_symbol + second_value + second_symbol;
        } else {
            return second_value + second_symbol;
        }
    }
}