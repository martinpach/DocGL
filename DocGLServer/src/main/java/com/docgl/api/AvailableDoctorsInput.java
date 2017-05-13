package com.docgl.api;

import com.docgl.enums.SpecializationsEnum;

import java.util.Date;

/**
 * Created by Rasťo on 13.5.2017.
 */
public class AvailableDoctorsInput {
    private SpecializationsEnum specialization;
    private String city;
    private Date dateFrom;
    private Date dateTo;

    public AvailableDoctorsInput() {
    }

    public AvailableDoctorsInput(SpecializationsEnum specialization, String city, Date dateFrom, Date dateTo) {
        this.specialization = specialization;
        this.city = city;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public SpecializationsEnum getSpecialization() {
        return specialization;
    }

    public String getCity() {
        return city;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }
}
