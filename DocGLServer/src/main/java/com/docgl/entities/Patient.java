package com.docgl.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by D33 on 4/23/2017.
 */

@Entity
@Table(name="Patients")
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
