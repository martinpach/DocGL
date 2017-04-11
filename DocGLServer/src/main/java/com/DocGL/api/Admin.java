package com.DocGL.api;


import jersey.repackaged.com.google.common.base.Throwables;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.persistence.*;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonMap;


/**
 * Created by D33 on 4/8/2017.
 */

@Entity
@Table(name = "Admins")
@NamedQueries({
        @NamedQuery(name = "com.DocGL.api.getAllAdmins",
                query = "from Admin"),
        @NamedQuery(name="com.DocGL.api.getAdminInformation",
                    query="from Admin where userName = :username and password = :password")
})
public class Admin/* implements Principal */{

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

    @Column(name = "password")
    private String password;

    public Admin() {
    }

    public int getIdadmin() {
        return idadmin;
    }

    public void setIdadmin(int idadmin) {
        this.idadmin = idadmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin(String firstName, String lastName, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

}
