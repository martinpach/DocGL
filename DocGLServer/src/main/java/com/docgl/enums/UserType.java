package com.docgl.enums;

/**
 * Created by Martin on 24.4.2017.
 */
public enum UserType {
    ADMIN("ADMIN"), DOCTOR("DOCTOR"), PATIENT("PATIENT");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
