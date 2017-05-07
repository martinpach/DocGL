package com.docgl.api;

import com.docgl.enums.SpecializationsEnum;
import com.docgl.enums.UserType;

/**
 * Created by Martin on 26.4.2017.
 */
public class RegistrationInput extends Input{
    private String firstName;
    private String lastName;
    private String email;
    private UserType userType;
    private SpecializationsEnum specialization;
    private String phone;
    private String city;
    private String workplace;


    public RegistrationInput() {
    }

    /**
     * This class represent deserialized json input when using registration resource
     * @param userName entered userName
     * @param password entered password
     * @param firstName entered firstName
     * @param lastName entered lastName
     * @param email entered email
     * @param userType entered userType
     * @param specialization entered specialization
     * @param phone entered phone
     * @param city entered city
     * @param workplace entered workplace
     */
    public RegistrationInput(String userName, String password, String firstName, String lastName, String email, UserType userType, SpecializationsEnum specialization, String phone, String city, String workplace) {
        super(userName, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType;
        this.specialization = specialization;
        this.phone = phone;
        this.city = city;
        this.workplace = workplace;
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

    public SpecializationsEnum getSpecialization() {
        return specialization;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getWorkplace() {
        return workplace;
    }
}
