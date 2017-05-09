package com.docgl.api;


/**
 * Created by Martin on 15.4.2017.
 */
public class AdminInput{
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public AdminInput() {
    }

    /**
     * @param firstName input from json when using updateProfile resource
     * @param lastName input from json when using updateProfile resource
     * @param password input from json when using updateProfile resource
     * @param email input from json when using updateProfile resource
     */
    public AdminInput(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
