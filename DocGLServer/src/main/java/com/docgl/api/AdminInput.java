package com.docgl.api;


/**
 * Created by Martin on 15.4.2017.
 */
public class AdminInput extends Input{
    private String email;

    public AdminInput() {
    }

    /**
     * @param userName input from json when using updateProfile resource
     * @param password input from json when using updateProfile resource
     * @param email input from json when using updateProfile resource
     */
    public AdminInput(String userName, String password, String email) {
        super(userName, password);
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}
