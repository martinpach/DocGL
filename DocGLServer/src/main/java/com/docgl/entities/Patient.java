package com.docgl.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by D33 on 4/23/2017.
 */

@Entity
@Table(name="Patients")
public class Patient extends User {

    @OneToMany(mappedBy = "doctor")
    private Collection<Appointment> appointment = new ArrayList<>();

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date registrationDate;

    public Patient(){}

    public Patient (String firstName, String lastName, String email, String userName, String password, Date registrationDate){
        super(firstName,lastName,email,userName,password);
        this.registrationDate = registrationDate;
    }

    public Collection<Appointment> getAppointment() {
        return appointment;
    }

    public void setAppointment(Collection<Appointment> appointment) {
        this.appointment = appointment;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
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
