package com.docgl.enums;

/**
 * Created by Martin on 28.4.2017.
 * Enumeration for sortable columns of Doctor.
 */
public enum SortableDoctorColumns {
    ID("id"), LASTNAME("lastName"), LIKES("likes"), REGISTRATION_DATE("registrationDate"), SPECIALIZATION("specialization");

    private final String value;

    SortableDoctorColumns(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
