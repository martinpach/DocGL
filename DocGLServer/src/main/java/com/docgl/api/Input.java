package com.docgl.api;

/**
 * Created by Martin on 26.4.2017.
 */
public abstract class Input {
    private String userName;
    private String password;

    public Input() {
    }

    public Input(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}