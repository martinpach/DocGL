package com.docgl.api;


/**
 * Created by Martin on 15.4.2017.
 */
public class AdminInput extends UserInput{

    public AdminInput() {
    }


    /**
     * @param firstName input from json when using updateProfile resource
     * @param lastName input from json when using updateProfile resource
     * @param password input from json when using updateProfile resource
     * @param email input from json when using updateProfile resource
     */
    public AdminInput(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

}
