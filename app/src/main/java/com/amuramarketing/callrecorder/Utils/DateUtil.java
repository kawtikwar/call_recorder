package com.amuramarketing.callrecorder.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
        Date now = new Date();
        return sdf.format(now);
    }

    public static String getMonthString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date now = new Date();
        return sdf.format(now);
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        return sdf.format(now);
    }

    public String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        Date now = new Date();
        return sdf.format(now);
    }

    public static String getDateString(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            Date resultdate = new Date(time);
            return sdf.format(resultdate);

        } catch (Exception e) {
            return "";
        }
    }
}