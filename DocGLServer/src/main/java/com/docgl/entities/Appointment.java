package com.docgl.entities;

import com.docgl.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Martin on 27.4.2017.
 */

@Entity
@Table(name = "Appointments")
@NamedQueries({
        @NamedQuery(name="getDoctorsAppointment", query="from Appointment where doctor.id = :id"),
        @NamedQuery(name="getPatientsAppointment", query="from Appointment where patient.id = :id"),
        @NamedQuery(name="getDoctorsAppointmentsByDate", query="from Appointment where doctor.id = :id and date = :date"),
        @NamedQuery(name="cancelDoctorsAppointmentsByDateBetweenTimeInterval",
                query="update Appointment set canceled = true where id = :id and doctor.id = :idDoctor and date = :date and time >= :timeFrom and time <= :timeTo")
})
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

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull
    @JsonView(Views.DoctorView.class)
    private Patient patient;

    @NotNull
    @JsonView(Views.PublicView.class)
    private Time time;

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

    @Column(name = "canceled", columnDefinition = "boolean default false")
    @JsonView(Views.PublicView.class)
    private boolean canceled;

    @Column(name = "done", columnDefinition = "boolean default false")
    @JsonView(Views.PublicView.class)
    private boolean done;

    public Appointment() {
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
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

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
