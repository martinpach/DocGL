package com.docgl.enums;

/**
 * Created by Martin on 29.4.2017.
 * Enumeration for Time periods.
 */
public enum TimePeriod {
    TODAY("TODAY"), WEEK("WEEK");
    private final String value;

    TimePeriod(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
