package com.docgl.api;

import com.docgl.enums.UserType;

/**
 * Created by Solanid on 11.4.2017.
 */
public class LoginInput extends Input{
    private UserType userType;

    public LoginInput() {
    }

    /**
     * @param userName represents input from json when using login resource
     * @param password represents input from json when using login resource
     * @param userType represents input from json and it is used to distinct what type of user is trying to log in
     */
    public LoginInput(String userName, String password, UserType userType) {
        super(userName, password);
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }
}
