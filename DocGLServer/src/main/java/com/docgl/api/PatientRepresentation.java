package com.docgl.api;

import com.docgl.entities.Patient;

/**
 * Created by Martin on 24.4.2017.
 */
public class PatientRepresentation {
    private Patient patient;
    private String token;

    public PatientRepresentation(Patient patient, String token) {
        this.patient = patient;
        this.token = token;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getToken() {
        return token;
    }
}
