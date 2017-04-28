package com.docgl.enums;

/**
 * Created by Martin on 28.4.2017.
 */
public enum SpecializationsEnum {
    DENTIST("DENTIST"), CARDIOLOGIST("CARDIOLOGIST"), ORTHOPEDIST("ORTHOPEDIST");

    private final String value;

    SpecializationsEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
