package com.docgl.entities;

/**
 * Created by D33 on 4/23/2017.
 */
public class Patient extends User {

    public Patient(){}

    public Patient (String firstName, String lastName, String email, String userName, String password){
        super(firstName,lastName,email,userName,password);
    }

    @Override
    public String toString(){

        return "Patient{" +
                "id='" + this.getId() + '\'' +
                ", firstname='" + this.getFirstName() + '\'' +
                ", lastname='" + this.getLastName() +'\''+
                ", email='" + this.getEmail() +'\''+
                ", username='"+this.getUserName()+'\''+
                ", password='"+this.getPassword()+'\''+
                '}';
    }
}
