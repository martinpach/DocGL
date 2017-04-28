package com.docgl.enums;

/**
 * Created by Martin on 28.4.2017.
 */
public enum SortingWays {
    ASC("asc"), DESC("desc");

    private final String value;

    SortingWays(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
