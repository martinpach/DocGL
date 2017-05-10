package com.docgl.api;

import java.util.Date;

/**
 * Created by Rasťo on 10.5.2017.
 */
public class AvailableAppointmetTimesInput {
    private int id;
    private Date date;

    public AvailableAppointmetTimesInput() {
    }

    public AvailableAppointmetTimesInput(int id, Date date) {
        this.id = id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}
