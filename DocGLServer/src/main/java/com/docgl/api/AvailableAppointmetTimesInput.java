package com.docgl.api;

import java.util.Date;

/**
 * Created by Rasťo on 10.5.2017.
 */
public class AvailableAppointmetTimesInput {
    private Integer id;
    private Date date;
    private Date dateFrom;
    private Date dateTo;

    public AvailableAppointmetTimesInput() {
    }

    /**
     * This class represent json input when using get available appointment times in date interval resource.
     * @param id Doctors ID
     * @param date Date when user want to make an appointment
     */
    public AvailableAppointmetTimesInput(Integer id, Date date) {
        this.id = id;
        this.date = date;
    }

    /**
     * This class represent json input when using get available appointment times in date interval resource.
     * @param id Doctors ID
     * @param dateFrom Date from when user want to make an appointment
     * @param dateTo Date to
     */
    public AvailableAppointmetTimesInput(Integer id, Date dateFrom, Date dateTo) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }
}
