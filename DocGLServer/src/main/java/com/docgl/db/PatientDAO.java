package com.docgl.db;

import com.docgl.entities.Patient;
import com.docgl.entities.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Rasťo on 22.4.2017.
 */
public class PatientDAO extends AbstractDAO<Patient> {
    public PatientDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Patient> getAllPatients(){
        return list(namedQuery("getAllPatients"));
    }
}
