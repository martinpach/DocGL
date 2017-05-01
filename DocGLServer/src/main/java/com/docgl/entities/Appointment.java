package com.docgl.entities;

import com.docgl.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Martin on 27.4.2017.
 */

@Entity
@Table(name = "Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.PublicView.class)
    private int id;

    private String note;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @NotNull
    @JsonView(Views.PatientView.class)
    private Doctor doctor;

    @Column(name = "doctor_id", insertable = false, updatable = false)
    @JsonIgnore
    private int doctorId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull
    @JsonView(Views.DoctorView.class)
    private Patient patient;

    @Column(name = "patient_id", insertable = false, updatable = false)
    @JsonIgnore
    private int patientId;

    @Temporal(TemporalType.TIME)
    @NotNull
    @JsonView(Views.PublicView.class)
    private Date time;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    @NotNull
    @JsonView(Views.PublicView.class)
    private Date date;

    @Column(name = "patient_first_name")
    @JsonView(Views.PublicView.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String patientFirstName;

    @Column(name = "patient_last_name")
    @JsonView(Views.PublicView.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String patientLastName;

    public Appointment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }
}
