package com.docgl.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Martin on 13.5.2017.
 */
public class WeeklyAppointmentsRepresentation {
    @JsonProperty
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="UTC")
    private Date date;
    @JsonProperty
    private long appointments;

    public WeeklyAppointmentsRepresentation(Date date, long appointments) {
        this.date = date;
        this.appointments = appointments;
    }
}
