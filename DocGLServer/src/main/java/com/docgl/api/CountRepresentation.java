package com.docgl.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Martin on 4.5.2017.
 */
public class CountRepresentation {
    @JsonProperty
    private long count;

    public CountRepresentation(long count) {
        this.count = count;
    }
}
