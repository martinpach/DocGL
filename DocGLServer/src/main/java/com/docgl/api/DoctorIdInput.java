package com.docgl.api;

/**
 * Created by Rasťo on 8.5.2017.
 */
public class DoctorIdInput {
    private int doctorId;


    public DoctorIdInput() {
    }

    public DoctorIdInput(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getDoctorId() {
        return doctorId;
    }
}
