package com.docgl.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rasťo on 11.5.2017.
 */
public class DayTimes {
    private String date;
    private List<String> times;

    public DayTimes(String date) {
        this.date = date;
        this.times = new ArrayList<String>();
    }

    public String getDate() {
        return date;
    }

    public List<String> getTimes() {
        return times;
    }
}
