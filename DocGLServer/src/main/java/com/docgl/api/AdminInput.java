package com.docgl.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Martin on 15.4.2017.
 */
public class AdminInput {

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private String email;

    public AdminInput() {
    }

    public AdminInput(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
