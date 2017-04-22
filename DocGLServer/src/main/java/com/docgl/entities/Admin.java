package com.docgl.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


/**
 * Created by D33 on 4/8/2017.
 */

@Entity
@Table(name = "Admins")

@NamedQueries({
        @NamedQuery(name="getAdminInformationById",
                    query = "from Admin where id = :id"),
        @NamedQuery(name="getAdminInformation",
                    query="from Admin where userName = :username and password = AES_ENCRYPT(:password, 'sovy2017')"),
        @NamedQuery(name="setPassword",
                query="update Admin set password = AES_ENCRYPT(:password, 'sovy2017'), passwordChanged = 1 where id = :id"),
        @NamedQuery(name="setProfile",
                query="update Admin set userName = :username, password = AES_ENCRYPT(:password, 'sovy2017'), email = :email where id = :id")
})
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", insertable = false, updatable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "user_name")
    private String userName;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "password_changed")
    private int passwordChanged;

    public Admin() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(char passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public Admin(String firstName, String lastName, String email, String userName, String password, int passwordChanged) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.passwordChanged = passwordChanged;
    }

    public Admin(String userName){
        this.userName=userName;
    }



    @Override
    public String toString(){

        return "Admin{" +
                "id='" + id + '\'' +
                ", firstname='" + firstName + '\'' +
                ", lastname='" + lastName +'\''+
                ", email='" + email +'\''+
                ", username='"+userName+'\''+
                ", password='"+password+'\''+
                ", passwordChanged='"+passwordChanged+'\''+
                '}';
    }
}

