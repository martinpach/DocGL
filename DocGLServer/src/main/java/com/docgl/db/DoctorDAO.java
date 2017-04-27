package com.docgl.db;

import com.docgl.entities.Doctor;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Rasťo on 15.4.2017.
 */
public class DoctorDAO extends AbstractDAO<Doctor> {
    public DoctorDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Doctor> getAllDoctors(int limit, int start, String sortBy, String way){
        Criteria criteria = criteria();
        criteria = new DatabaseCommonMethods().setPaginationAndSorting(criteria,limit,start,sortBy,way);
        return list(criteria);
    }
}

