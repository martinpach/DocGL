package com.docgl.db;

import com.docgl.entities.Appointment;
import com.docgl.enums.UserType;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        List<Appointment> appointments = new ArrayList<>();
        if(userType.equals(UserType.DOCTOR)) {
            appointments = namedQuery("getDoctorsAppointment")
                    .setParameter("id", id)
                    .list();
        }
        else if(userType.equals(UserType.PATIENT)){
            appointments = namedQuery("getPatientsAppointment")
                    .setParameter("id", id)
                    .list();
        }
        return appointments;
    }

    /**
     * @param id user identificator
     * @param date appointments date
     * @return list of all appointments for unique doctor chosen date
     */
    public List<Appointment> getDoctorsAppointmentsByDate(int id, Date date){
        Criteria criteria = criteria();
            criteria.add(Restrictions.eq("doctorId", id));
            criteria.add(Restrictions.eq("date", date));
        return list(criteria);
    }

    /**
     * This function search an appointment by his id
     * @param id appointment id
     * @return appointment
     */
    public Appointment getAppointment(int id) {
        Session session = currentSession();
        return session.find(Appointment.class, id);
    }

    /**
     * This function cancel appointment, canceled appointment cannot be approved again
     * @param id appointment id
     */
    public void cancelAppointment(int id) {
        Session session = currentSession();
        Appointment appointment = session.find(Appointment.class, id);
        appointment.setCanceled(true);
    }
}
