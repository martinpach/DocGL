package com.DocGL.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by D33 on 4/8/2017.
 */

@Entity
@Table(name = "admins")
@NamedNativeQueries({
        @NamedNativeQuery(name = "com.DocGL.api.getAllAdmins",
                query = "select * from Admins")
})
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idadmin;

    @Column(name = "firstname", insertable = false, updatable = false)
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String userName;

    @Column(name = "firstname")
    private String password;

    public Admin() {
    }

    public Admin(String firstName, String lastName, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}
