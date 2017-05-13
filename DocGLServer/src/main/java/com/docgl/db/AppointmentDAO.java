package com.docgl.db;

import com.docgl.api.NewAppointmentInput;
import com.docgl.entities.Appointment;
import com.docgl.entities.Doctor;
import com.docgl.entities.Patient;
import com.docgl.enums.UserType;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.sql.Time;
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
        return namedQuery("getDoctorsAppointmentsByDate")
                    .setParameter("id", id)
                    .setParameter("date", date)
                    .list();
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

    /**
     * This function mark appointment as done
     * @param id chosen appointment
     */
    public void markAppointmentAsDone(int id){
        Session session = currentSession();
        Appointment appointment = session.find(Appointment.class, id);
        appointment.setDone(true);
    }

    /**
     * Save new Appointment to the database.
     * @param input class NewAppointmentInput with date, time, firstName, lastName, note, doctorId
     * @param patientId patientId
     */
    public void createNewAppointment(NewAppointmentInput input, int patientId) {
        if (StringUtils.isBlank(input.getNote())) {
            currentSession().save(new Appointment(
                    currentSession().find(Doctor.class, input.getDoctorId()),
                    currentSession().find(Patient.class, patientId),
                    new Time((long)(input.getTime().getTime() + 3.6e+6)),
                    input.getDate(),
                    input.getFirstName(),
                    input.getLastName()
            ));
        }
        else {
            currentSession().save(new Appointment(
                    input.getNote(),
                    currentSession().find(Doctor.class, input.getDoctorId()),
                    currentSession().find(Patient.class, patientId),
                    new Time((long)(input.getTime().getTime() + 3.6e+6)),
                    input.getDate(),
                    input.getFirstName(),
                    input.getLastName()
            ));
        }
    }

     /**
     * This function cancels appointments in time interval
     * @param idDoctor doctors' appointment
     * @param date date of appointment
     * @param from start of time interval
     * @param to end of time interval
     */
    public void cancelDoctorsAppoitmentsByDateBetweenTimeInterval(int idDoctor, Date date, Time from, Time to){
        from = new java.sql.Time((long) (from.getTime() - 1.14e+6));
        List<Appointment> appointments = getDoctorsAppointmentsByDate(idDoctor, date);
        for(Appointment appointment : appointments) {
            if(appointment.getDate().equals(date) && appointment.getTime().after(from) && appointment.getTime().before(to)
                    && !appointment.isCanceled()){

                Appointment appointmentToUpdate = currentSession().find(Appointment.class, appointment.getId());
                appointmentToUpdate.setCanceled(true);
                appointmentToUpdate.setTime(new Time((long)(appointment.getTime().getTime() + 3.6e+6)));
            }
        }

    }
}
