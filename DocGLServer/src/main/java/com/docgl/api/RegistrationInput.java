package com.docgl.api;

import com.docgl.UserType;

/**
 * Created by Martin on 26.4.2017.
 */
public class RegistrationInput extends Input{
    private String firstName;
    private String lastName;
    private String email;
    private UserType userType;

    public RegistrationInput() {
    }

    public RegistrationInput(String userName, String password, String firstName, String lastName, String email, UserType userType) {
        super(userName, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UserType getUserType() {
        return userType;
    }

}
