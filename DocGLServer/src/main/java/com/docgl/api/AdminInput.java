package com.docgl.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Martin on 15.4.2017.
 */
public class AdminInput {

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    @JsonProperty
    private String email;

    public AdminInput() {
    }

    public AdminInput(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
