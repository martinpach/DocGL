package com.docgl;

import org.joda.time.LocalDate;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Martin on 11.5.2017.
 * Class used for parsing Date and Time formats.
 */
public class DateParser {
    public static Date parseStringToUtilDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException ex){
            System.err.println("ParseStringToUtilDate"+ex.getMessage());
        }
        return date;
    }

    public static Time parseStringToTime(String string) {
//        SimpleDateFormat sdf = new SimpleTimeFormat("HH:mm");
        return java.sql.Time.valueOf(string);
    }

    public static Date addDaysToDate(int numOfDays, Date date){
        LocalDate localDate = new LocalDate(date);
        return localDate.plusDays(numOfDays).toDate();
    }
}
