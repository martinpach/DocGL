package com.docgl.db;

import com.docgl.entities.Doctor;
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

    public List<Doctor> getAllDoctors(int limit, int start){
        if(limit > 0 && start > 0){
            return list(namedQuery("com.docgl.entities.Doctor.getFilteredDoctors")
                    .setParameter("start", start)
                    .setParameter("last", start + limit -1));
        }
        return list(namedQuery("com.DocGL.api.getAllDoctors"));
    }
}

