package com.docgl.api;

import com.docgl.entities.Doctor;

/**
 * Created by Rasťo on 3.5.2017.
 */
public class DoctorRepresentation {
    private Doctor doctor;
    private String token;

    /**
     * This class represents response after successful login
     * @param doctor represents logged doctor entity object
     * @param token represents generated token
     */
    public DoctorRepresentation(Doctor doctor, String token) {
        this.doctor = doctor;
        this.token = token;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getToken() {
        return token;
    }
}
