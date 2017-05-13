package com.docgl.api;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Rasťo on 10.5.2017.
 */
public class NewAppointmentInput {
    private Date date;
    private Time time;
    private String note;
    private String firstName;
    private String lastName;
    private Integer doctorId;

    public NewAppointmentInput() {
    }

    public NewAppointmentInput(Date date, Time time, String note, String firstName, String lastName, Integer doctorId) {
        this.date = date;
        this.time = time;
        this.note = note;
        this.firstName = firstName;
        this.lastName = lastName;
        this.doctorId = doctorId;
    }
    public NewAppointmentInput(Date date, Time time, String firstName, String lastName, Integer doctorId) {
        this.date = date;
        this.time = time;
        this.note = note;
        this.firstName = firstName;
        this.lastName = lastName;
        this.doctorId = doctorId;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public String getNote() {
        return note;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }
}
