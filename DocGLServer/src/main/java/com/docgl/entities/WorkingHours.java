package com.docgl.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Martin on 7.5.2017.
 */

@Entity
@Table(name="Working_Hours")
public class WorkingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @ManyToOne
    @JoinColumn(name="doctor_id")
    @JsonIgnore
    private Doctor doctor;

    @Column(name = "doctor_id", insertable = false, updatable = false)
    @JsonIgnore
    private int doctorId;

    @Column(name="monday_from")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mondayFrom;

    @Column(name="monday_to")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mondayTo;

    @Column(name="tuesday_from")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tuesdayFrom;

    @Column(name="tuesday_to")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tuesdayTo;

    @Column(name="wednesday_from")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wednesdayFrom;

    @Column(name="wednesday_to")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wednesdayTo;

    @Column(name="thursday_from")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String thursdayFrom;

    @Column(name="thursday_to")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String thursdayTo;

    @Column(name="friday_from")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fridayFrom;

    @Column(name="friday_to")
//    @Temporal(TemporalType.TIME)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fridayTo;

    public WorkingHours() {
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

    public String getMondayFrom() {
        return mondayFrom;
    }

    public void setMondayFrom(String mondayFrom) {
        this.mondayFrom = mondayFrom;
    }

    public String getMondayTo() {
        return mondayTo;
    }

    public void setMondayTo(String mondayTo) {
        this.mondayTo = mondayTo;
    }

    public String getTuesdayFrom() {
        return tuesdayFrom;
    }

    public void setTuesdayFrom(String tuesdayFrom) {
        this.tuesdayFrom = tuesdayFrom;
    }

    public String getTuesdayTo() {
        return tuesdayTo;
    }

    public void setTuesdayTo(String tuesdayTo) {
        this.tuesdayTo = tuesdayTo;
    }

    public String getWednesdayFrom() {
        return wednesdayFrom;
    }

    public void setWednesdayFrom(String wednesdayFrom) {
        this.wednesdayFrom = wednesdayFrom;
    }

    public String getWednesdayTo() {
        return wednesdayTo;
    }

    public void setWednesdayTo(String wednesdayTo) {
        this.wednesdayTo = wednesdayTo;
    }

    public String getThursdayFrom() {
        return thursdayFrom;
    }

    public void setThursdayFrom(String thursdayFrom) {
        this.thursdayFrom = thursdayFrom;
    }

    public String getThursdayTo() {
        return thursdayTo;
    }

    public void setThursdayTo(String thursdayTo) {
        this.thursdayTo = thursdayTo;
    }

    public String getFridayFrom() {
        return fridayFrom;
    }

    public void setFridayFrom(String fridayFrom) {
        this.fridayFrom = fridayFrom;
    }

    public String getFridayTo() {
        return fridayTo;
    }

    public void setFridayTo(String fridayTo) {
        this.fridayTo = fridayTo;
    }
}
