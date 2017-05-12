package com.docgl.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Martin on 10.5.2017.
 */
@Entity
@Table(name = "Free_Hours")
@NamedQueries({
        @NamedQuery(name = "getDoctorsFreeHours", query = "from FreeHours where doctor.id = :id")
})
public class FreeHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    @NotNull
    @Column(name="time_from")
    private Time from;

    @NotNull
    @Column(name="time_to")
    private Time to;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @NotNull
    @JsonIgnore
    private Doctor doctor;

    public FreeHours() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getFrom() {
        return from;
    }

    public void setFrom(Time from) {
        this.from = from;
    }

    public Time getTo() {
        return to;
    }

    public void setTo(Time to) {
        this.to = to;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
