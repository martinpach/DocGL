package com.docgl.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rasťo on 15.4.2017.
 */
@Entity
@Table(name = "Doctors")
@NamedQueries({
        @NamedQuery(name = "com.DocGL.api.getAllDoctors",
                query = "from Doctor"),
        @NamedQuery(name = "com.docgl.entities.Doctor.getFilteredDoctors",
                query = "from Doctor where iddoctor between :start and :last")
})
public class Doctor implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iddoctor;

    @Column(name = "firstname", insertable = false, updatable = false)
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    public Doctor(){
    }

    public int getIddoctor() {
        return iddoctor;
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

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Doctor(String firstName, String lastName, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}
