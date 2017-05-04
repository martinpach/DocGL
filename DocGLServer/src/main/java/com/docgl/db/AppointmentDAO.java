package com.docgl.db;

import com.docgl.entities.Appointment;
import com.docgl.enums.TimePeriod;
import com.docgl.enums.UserType;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 29.4.2017.
 */
public class AppointmentDAO extends AbstractDAO<Appointment> {
    public AppointmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * @return number of appointemnts for today
     */
    public long getNumberOfAppointments(){
        Criteria criteria = criteria();
        criteria.add(Restrictions.eq("date", new Date()));
        return (long) criteria().setProjection(Projections.rowCount()).uniqueResult();
    }

    /**
     * @param id user identificator
     * @param userType represents type: doctor, patient
     * @return list of all appointments for unique doctor or patient
     */
    public List<Appointment> getAppointments(int id, UserType userType){
        Criteria criteria = criteria();
        if(userType.getValue().equals("DOCTOR")){
            criteria.add(Restrictions.eq("doctorId", id));
        }
        if(userType.getValue().equals("PATIENT")){
            criteria.add(Restrictions.eq("patientId", id));
        }
        return list(criteria);
    }
}
