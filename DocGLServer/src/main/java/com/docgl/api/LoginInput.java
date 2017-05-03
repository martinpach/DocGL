package com.docgl.api;

import com.docgl.enums.UserType;

/**
 * Created by Solanid on 11.4.2017.
 */
public class LoginInput extends Input{
    private UserType userType;

    public LoginInput() {
    }

    public LoginInput(String userName, String password, UserType userType) {
        super(userName, password);
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }
}
