package com.DocGL.entities;


import javax.persistence.*;
import java.security.Principal;
import java.util.Objects;


/**
 * Created by D33 on 4/8/2017.
 */

@Entity
@Table(name = "Admins")
@NamedQueries({
        @NamedQuery(name = "com.DocGL.api.getAllAdmins",
                query = "from Admin"),
        @NamedQuery(name="com.DocGL.api.getAdminInformation",
                    query="from Admin where userName = :username and password = :password"),
        @NamedQuery(name="com.DocGL.api.setPassword",
                query="update Admin set password = :password, passwordChanged = 'T' where idadmin = :id"),
        @NamedQuery(name="com.DocGL.api.setProfile",
                query="update Admin set userName = :username, password = :password, email = :email where idadmin = :id")
})
public class Admin implements Principal {

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

    @Column(name = "passwordChanged")
    private char passwordChanged;

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

    public char getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(char passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public Admin(String firstName, String lastName, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public Admin(String userName){
        this.userName=userName;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public String toString(){

        return "Admin{" +
                "id='" + idadmin + '\'' +
                ", firstname='" + firstName + '\'' +
                ", lastname='" + lastName +'\''+
                ", email='" + email +'\''+
                ", username='"+userName+'\''+
                ", password='"+password+'\''+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Admin admin = (Admin) o;
        return Objects.equals(idadmin, admin.idadmin) && Objects.equals(userName, admin.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idadmin, userName);
    }
}
