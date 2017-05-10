package com.docgl.api;

/**
 * Created by Martin on 9.5.2017.
 */
public class UserInput {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserInput() {
    }

    public UserInput(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
