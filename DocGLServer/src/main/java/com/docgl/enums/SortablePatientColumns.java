package com.docgl.enums;

/**
 * Created by Martin on 28.4.2017.
 * Enumeration for sortable columns of Patient.
 */
public enum SortablePatientColumns {
    ID("id"), LASTNAME("lastName"), REGISTRATION_DATE("registrationDate");

    private final String value;

    SortablePatientColumns(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
