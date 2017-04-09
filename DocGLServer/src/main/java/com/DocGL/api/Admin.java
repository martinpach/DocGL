package com.DocGL.api;

import javax.persistence.*;

/**
 * Created by D33 on 4/8/2017.
 */

@Entity
@Table(name = "admins")
@NamedNativeQueries(value = {
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

}
