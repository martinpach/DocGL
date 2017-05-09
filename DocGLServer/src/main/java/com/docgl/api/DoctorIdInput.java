package com.docgl.api;

/**
 * Created by Rasťo on 8.5.2017.
 */
public class DoctorIdInput {
    private Integer doctorId;


    public DoctorIdInput() {
    }

    public DoctorIdInput(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }
}
