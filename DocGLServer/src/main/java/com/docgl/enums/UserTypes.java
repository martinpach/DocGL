package com.docgl.enums;

/**
 * Created by Rasťo on 28.5.2017.
 */
public enum UserTypes {
    DOCTORS("DOCTORS"), PATIENTS("PATIENTS");

    private final String value;

    UserTypes(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
