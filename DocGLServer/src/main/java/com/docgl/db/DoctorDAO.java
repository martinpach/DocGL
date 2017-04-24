package com.docgl.db;

import com.docgl.entities.Doctor;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Rasťo on 15.4.2017.
 */
public class DoctorDAO extends AbstractDAO<Doctor> {
    public DoctorDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Doctor> getAllDoctors(int limit, int start){
        if(limit > 0 && start >= 0){
            Criteria criteria = criteria()
                    .setFirstResult(start)
                    .setMaxResults(limit);
            return list(criteria);
        }
        return list(namedQuery("getAllDoctors"));
    }
}

