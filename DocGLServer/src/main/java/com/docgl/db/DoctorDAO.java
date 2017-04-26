package com.docgl.db;

import com.docgl.ValidationException;
import com.docgl.entities.Doctor;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Objects;

/**
 * Created by Rasťo on 15.4.2017.
 */
public class DoctorDAO extends AbstractDAO<Doctor> {
    public DoctorDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Doctor> getAllDoctors(int limit, int start, String sortBy, String way){
        if(limit > 0 && start >= 0){
            Criteria criteria = criteria()
                    .setFirstResult(start)
                    .setMaxResults(limit);
            if(!sortBy.equals("lastName")){
                throw new ValidationException("Query param '"+ sortBy +"' is not valid");
            }
            if(way.equals("asc")){
                criteria.addOrder(Order.asc(sortBy));
            }
            else if(way.equals("desc")){
                criteria.addOrder(Order.desc(sortBy));
            }
            else{
                throw new ValidationException("Query param '"+ way +"' is not valid. Use instead : 'asc' or 'desc'");
            }

            return list(criteria);
        }
        return list(namedQuery("getAllDoctors"));
    }
}

