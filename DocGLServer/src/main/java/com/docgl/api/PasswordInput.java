package com.docgl.api;

/**
 * Created by Martin on 23.4.2017.
 * Class is used for deserializing json input when using change password resource
 */
public class PasswordInput {
    private String password;

    public PasswordInput() {
    }

    public String getPassword() {
        return password;
    }
}
