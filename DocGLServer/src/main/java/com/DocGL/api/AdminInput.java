package com.DocGL.api;

/**
 * Created by Martin on 15.4.2017.
 */
public class AdminInput {
    private String username;
    private String password;
    private String email;

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public AdminInput() {
    }

    public AdminInput(String userName, String password, String email) {
        this.username = userName;
        this.password = password;
        this.email = email;
    }
}
