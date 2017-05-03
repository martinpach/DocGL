package com.docgl.entities;

import com.docgl.enums.SpecializationsEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Rasťo on 15.4.2017.
 */
@Entity
@Table(name = "Doctors")
public class Doctor extends User{

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Appointment> appointments = new ArrayList<>(0);

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    @NotNull
    private Date registrationDate = new Date();

    @Column(columnDefinition = "int default 0")
    @NotNull
    private int likes;

    @NotNull
    private SpecializationsEnum specialization;

    @Column(name = "blocked", columnDefinition = "boolean default false")
    private boolean blocked;

    @Column(name = "approved", columnDefinition = "boolean default false")
    private boolean approved;

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public SpecializationsEnum getSpecialization() {
        return specialization;
    }

    public void setSpecialization(SpecializationsEnum specialization) {
        this.specialization = specialization;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Doctor(){
    }

    public Doctor(String firstName, String lastName, String email, String userName, String password) {
        super(firstName,lastName,email,userName,password);
    }

    public Doctor(String firstName, String lastName, String email, String userName, SpecializationsEnum specialization, String password, Date registrationDate) {
        super(firstName,lastName,email,userName,password);
        this.registrationDate = registrationDate;
        this.specialization = specialization;
    }

    public Doctor(String firstName, String lastName, String email, String userName, String password, Collection<Appointment> appointments, @NotNull Date registrationDate, @NotNull int likes, @NotNull SpecializationsEnum specialization, boolean blocked, boolean approved) {
        super(firstName, lastName, email, userName, password);
        this.appointments = appointments;
        this.registrationDate = registrationDate;
        this.likes = likes;
        this.specialization = specialization;
        this.blocked = blocked;
        this.approved = approved;
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
                ", blocked='"+this.isBlocked()+'\''+
                ", approved='"+this.isApproved()+'\''+
                '}';
    }
}
