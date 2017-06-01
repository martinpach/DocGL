package com.docgl.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.print.Doc;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by D33 on 4/23/2017.
 * Patient Entity
 */

@Entity
@Table(name = "Patients")
public class Patient extends User {

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Appointment> appointments = new ArrayList<>();

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    @NotNull
    private Date registrationDate;

    @Column(name = "blocked", columnDefinition = "boolean default false")
    private boolean blocked;

    // favourite doctors
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Favourite_doctors")
    @JsonIgnore
    private Collection<Doctor> doctors = new ArrayList<>();

    @Column(name = "fcm_registration_token")
    @Lob
    private String FCMRegistrationToken;

    public Patient() {
    }

    public Patient(String firstName, String lastName, String email, String userName, String password, Date registrationDate) {
        super(firstName, lastName, email, userName, password);
        this.registrationDate = registrationDate;

    }

    public String getFCMRegistrationToken() {
        return FCMRegistrationToken;
    }

    public void setFCMRegistrationToken(String FCMRegistrationToken) {
        this.FCMRegistrationToken = FCMRegistrationToken;
    }

    public Collection<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Collection<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Collection<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Collection<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {

        return "Patient{" +
                "id='" + this.getId() + '\'' +
                ", firstname='" + this.getFirstName() + '\'' +
                ", lastname='" + this.getLastName() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", username='" + this.getUserName() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                ", blocked='" + this.isBlocked() + '\'' +
                '}';
    }
}
