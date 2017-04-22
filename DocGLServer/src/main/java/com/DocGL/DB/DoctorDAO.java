package com.DocGL.DB;

import com.DocGL.entities.Doctor;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Rasťo on 15.4.2017.
 */
public class DoctorDAO extends AbstractDAO<Doctor> {
    public DoctorDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Doctor> getAllDoctors(){
        return list(namedQuery("com.DocGL.api.getAllDoctors"));
    }
}

