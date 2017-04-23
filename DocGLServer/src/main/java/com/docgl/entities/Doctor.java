package com.docgl.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rasťo on 15.4.2017.
 */
@Entity
@Table(name = "Doctors")
@NamedQueries({
        @NamedQuery(name = "getAllDoctors",
                query = "from Doctor"),
        @NamedQuery(name = "getFilteredDoctors",
                query = "from Doctor where iddoctor between :start and :last")
})
public class Doctor extends User implements Serializable{

    public Doctor(){
    }
    public Doctor(String firstName, String lastName, String email, String userName, String password) {
        super(firstName,lastName,email,userName,password);
    }

    @Override
    public String toString(){

        return "Doctor{" +
                "id='" + this.getId() + '\'' +
                ", firstname='" + this.getFirstName() + '\'' +
                ", lastname='" + this.getLastName() +'\''+
                ", email='" + this.getEmail() +'\''+
                ", username='"+this.getUserName()+'\''+
                ", password='"+this.getPassword()+'\''+
                '}';
    }
}
