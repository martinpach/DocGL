package com.docgl.entities;

import com.docgl.enums.SpecializationsEnum;
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

    @OneToMany(mappedBy = "doctor")
    private Collection<Appointment> appointments = new ArrayList<>();

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    @NotNull
    private Date registrationDate = new Date();

    @Column(columnDefinition = "int default 0")
    @NotNull
    private int likes;

    //@ManyToOne
    //@JoinColumn(name = "specialization_id")
    @NotNull
    //@JsonIgnore
    private SpecializationsEnum specialization;

    /*@Column(name = "specialization_id", insertable = false, updatable = false)
    private SpecializationsEnum specializationId;*/

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

    /*public SpecializationsEnum getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(SpecializationsEnum specializationId) {
        this.specializationId = specializationId;
    }*/

    public Doctor(){
    }
    public Doctor(String firstName, String lastName, String email, String userName, String password) {
        super(firstName,lastName,email,userName,password);
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
                '}';
    }
}
