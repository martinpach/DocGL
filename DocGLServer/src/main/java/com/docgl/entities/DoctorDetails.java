package com.docgl.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Martin on 27.4.2017.
 */

@Entity
@Table(name = "Doctor_details")
public class DoctorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    @NotNull
    private Doctor doctor;

    @Column(columnDefinition = "int default 0")
    @NotNull
    private int likes;

    @NotNull
    private String specialization;

    public DoctorDetails() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}
