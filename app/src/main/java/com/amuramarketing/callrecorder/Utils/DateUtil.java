package com.amuramarketing.callrecorder.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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



    public static String getDayDateString(String msecs) {

        try {
            long yourmilliseconds = Long.parseLong(msecs);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
            Date resultdate = new Date(yourmilliseconds);

            SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMMM dd, yyyy");
            return dateFormat.format(resultdate);
        }catch (Exception e){

            return "";
        }
    }


    public static String getTimeString(String msecs) {

        long yourmilliseconds=0;
        try {

            yourmilliseconds = Long.parseLong(msecs);
        }catch (Exception e){
            yourmilliseconds=0;
        }

        GregorianCalendar cal = new GregorianCalendar();
        StringBuffer sBuf = new StringBuffer(8);

        cal.setTime(new Date(yourmilliseconds));

        int hour = cal.get(Calendar.HOUR);

        if (hour == 0)
            hour = 12;

        if (hour < 10)
            sBuf.append("");

        sBuf.append(Integer.toString(hour));
        sBuf.append(":");

        int minute = cal.get(Calendar.MINUTE);

        if (minute < 10)
            sBuf.append("0");

        sBuf.append(Integer.toString(minute));
        sBuf.append(" ");
        sBuf.append(cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");

        return (sBuf.toString());
    }


    public static String getDurationString(String msecs) {

        try {

            int sec = Integer.parseInt(msecs);
            Date d = new Date(sec * 1000L);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            String time = df.format(d);
            return time;

        }catch (Exception e){

            return "";
        }

    }


}