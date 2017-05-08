package com.docgl.entities;

import com.docgl.enums.SpecializationsEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private Collection<WorkingHours> workingHours = new ArrayList<>(0);

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

    @Column(name = "phone")
    @NotNull
    private String phone;

    @NotNull
    private String city;

    @NotNull
    private String workplace;

    @Column(name = "appointments_duration", columnDefinition = "int default 0")
    private int appointmentsDuration;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public Collection<WorkingHours> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Collection<WorkingHours> workingHours) {
        this.workingHours = workingHours;
    }
    public int getAppointmentsDuration() {
        return appointmentsDuration;
    }

    public void setAppointmentsDuration(int appointmentsDuration) {
        this.appointmentsDuration = appointmentsDuration;
    }

    public Doctor(){
    }

    public Doctor(String firstName, String lastName, String email, String userName, String password) {
        super(firstName,lastName,email,userName,password);
    }

    public Doctor(String firstName, String lastName, String email, String userName, SpecializationsEnum specialization, String phone, String password, Date registrationDate, String city, String workplace) {
        super(firstName,lastName,email,userName,password);
        this.registrationDate = registrationDate;
        this.specialization = specialization;
        this.phone = phone;
        this.city = city;
        this.workplace = workplace;
    }

    public Doctor(String firstName, String lastName, String email, String userName, String password, Collection<Appointment> appointments, @NotNull Date registrationDate, @NotNull int likes, @NotNull SpecializationsEnum specialization, String phone, String city, String workplace,int appointmentsDuration, boolean blocked, boolean approved) {
        super(firstName, lastName, email, userName, password);
        this.appointments = appointments;
        this.registrationDate = registrationDate;
        this.likes = likes;
        this.specialization = specialization;
        this.phone = phone;
        this.blocked = blocked;
        this.approved = approved;
        this.city = city;
        this.workplace = workplace;
        this.appointmentsDuration = appointmentsDuration;
    }

    @Override
    public String toString(){

        return "Doctor{" +
                "id='" + this.getId() + '\'' +
                ", firstname='" + this.getFirstName() + '\'' +
                ", lastname='" + this.getLastName() +'\''+
                ", email='" + this.getEmail() +'\''+
                ", phone='" + this.getPhone() +'\''+
                ", city='" + this.getCity() +'\''+
                ", workplace='" + this.getWorkplace() +'\''+
                ", username='"+this.getUserName()+'\''+
                ", password='"+this.getPassword()+'\''+
                ", appointmentsDuration='"+this.getAppointmentsDuration()+'\''+
                ", blocked='"+this.isBlocked()+'\''+
                ", approved='"+this.isApproved()+'\''+
                '}';
    }

}
