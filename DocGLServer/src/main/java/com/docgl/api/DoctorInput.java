package com.docgl.api;

/**
 * Created by Martin on 9.5.2017.
 */
public class DoctorInput extends UserInput{
    private String phone;

    public DoctorInput() {
    }

    public DoctorInput(String firstName, String lastName, String email, String password, String phone) {
        super(firstName, lastName, email, password);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
