package com.docgl.entities;

import com.docgl.enums.SpecializationsEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Martin on 28.4.2017.
 */

//@Entity
public class Specializations {
   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private SpecializationsEnum specialization_name;

    @OneToMany(mappedBy = "specialization")
    private Collection<Doctor> doctors = new ArrayList<>();

    public Specializations() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpecializationsEnum getSpecialization_name() {
        return specialization_name;
    }

    public void setSpecialization_name(SpecializationsEnum specialization_name) {
        this.specialization_name = specialization_name;
    }

    public Collection<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Collection<Doctor> doctors) {
        this.doctors = doctors;
    }*/
}
