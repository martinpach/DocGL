package com.docgl.api;

import java.util.Date;

/**
 * Created by Rasťo on 10.5.2017.
 */
public class NewAppointmentInput {
    private Date date;
    private Date time;
    private String note;
    private String firstName;
    private String lastName;
    private int doctorId;

    public NewAppointmentInput() {
    }

    public NewAppointmentInput(Date date, Date time, String note, String firstName, String lastName, int doctorId) {
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

    public Date getTime() {
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

    public int getDoctorId() {
        return doctorId;
    }
}
