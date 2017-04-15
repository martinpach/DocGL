package com.DocGL.api;

/**
 * Created by Martin on 15.4.2017.
 */
public class AdminInput {
    private String username;
    private String password;
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
