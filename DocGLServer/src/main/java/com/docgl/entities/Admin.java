package com.docgl.entities;


import javax.persistence.*;


/**
 * Created by D33 on 4/8/2017.
 */

@Entity
@Table(name = "Admins")
public class Admin extends User{

    @Column(name = "password_changed", columnDefinition = "boolean default true")
    private boolean passwordChanged;

    public boolean getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public Admin() {
    }

    public Admin(String firstName, String lastName, String email, String userName, String password, boolean passwordChanged) {
        super(firstName,lastName,email,userName,password);
        this.passwordChanged=passwordChanged;
    }

    public Admin(String userName) {
       super(userName);
    }

    @Override
    public String toString(){

        return "Admin{" +
                "id='" + this.getId() + '\'' +
                ", firstname='" + this.getFirstName() + '\'' +
                ", lastname='" + this.getLastName() +'\''+
                ", email='" + this.getEmail() +'\''+
                ", username='"+this.getUserName()+'\''+
                ", password='"+this.getPassword()+'\''+
                ", passwordChanged='"+passwordChanged+'\''+
                '}';
    }
}

