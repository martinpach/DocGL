package com.docgl;

import org.joda.time.LocalTime;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Martin on 11.5.2017.
 */
public class DateParser {
    public static Date parseStringToUtilDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException ex){
        }
        return date;
    }

    public static Time parseStringToTime(String string) {
//        SimpleDateFormat sdf = new SimpleTimeFormat("HH:mm");
        Time time = java.sql.Time.valueOf(string);
        return time;
    }
}
