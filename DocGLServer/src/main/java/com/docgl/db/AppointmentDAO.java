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
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
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
        return (long) criteria()
                .add(Restrictions.eq("canceled", false))
                .add(Restrictions.eq("done", false))
                .add(Restrictions.eq("date", new Date()))
                .setProjection(Projections.rowCount()).uniqueResult();
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
        markAsDonePastAppointments(appointments);
        return appointments;
    }

    /**
     * @param id user identificator
     * @param date appointments date
     * @return list of all appointments for unique doctor chosen date
     */
    public List<Appointment> getDoctorsAppointmentsByDate(int id, Date date){
        List<Appointment> appointments = namedQuery("getDoctorsAppointmentsByDate")
                .setParameter("id", id)
                .setParameter("date", date)
                .list();
        markAsDonePastAppointments(appointments);
        return appointments;
    }

    /**
     * This function search an appointment by his id
     * @param id appointment id
     * @return appointment
     */
    public Appointment getAppointment(int id) {
        Session session = currentSession();
        Appointment appointment = session.find(Appointment.class, id);
        markAsDonePastAppointment(appointment);
        return appointment;
    }

    /**
     * This function gets last doctor appointment from database. It is for doctor changing working hours. (He cannot change working hours while he has some interview)
     * @param idDoctor chosen doctor
     * @return last appointment
     */
    public Appointment getDoctorsLastAppointment(int idDoctor){
        Appointment appointment = (Appointment) namedQuery("getDoctorsLastAppointment")
                .setParameter("id", idDoctor)
                .setMaxResults(1)
                .getSingleResult();
        markAsDonePastAppointment(appointment);
        return appointment;
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
                   // new Time((long)(input.getTime().getTime() + 3.6e+6)),
                    input.getTime(),
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
                   // new Time((long)(input.getTime().getTime() + 3.6e+6)),
                    input.getTime(),
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
                //appointmentToUpdate.setTime(new Time((long)(appointment.getTime().getTime() + 3.6e+6)));
                appointmentToUpdate.setTime(appointment.getTime());
            }
        }

    }

    /**
     * Function that set all appointments that are older as actual date and actual time plus doctors working hours as DONE
     * @param appointments list of Appointments
     */
    private void markAsDonePastAppointments(List<Appointment> appointments) {
        if  (appointments != null) {
            Doctor doctor = appointments.get(0).getDoctor();
            LocalDate date;
            LocalTime time;
            LocalTime currTimeWithAppDuration = new LocalTime().plusMinutes(doctor.getAppointmentsDuration());
            for (Appointment a : appointments) {
                date = new LocalDate(a.getDate());
                time = new LocalTime(a.getTime());
                if  ((date.compareTo(new LocalDate()) == 0 && time.compareTo(currTimeWithAppDuration) == -1) || date.compareTo(new LocalDate()) == -1)
                    a.setDone(true);
            }
        }
    }

    /**
     * Function that set appointment if is older than actual date and actual time plus doctors working hours as DONE
     * @param appointment list of Appointment
     */
    private void markAsDonePastAppointment(Appointment appointment) {
        if  (appointment != null) {
            Doctor doctor = appointment.getDoctor();
            LocalDate date;
            LocalTime time;
            LocalTime currTimeWithAppDuration = new LocalTime().plusMinutes(doctor.getAppointmentsDuration());
            date = new LocalDate(appointment.getDate());
            time = new LocalTime(appointment.getTime());
            if  ((date.compareTo(new LocalDate()) == 0 && time.compareTo(currTimeWithAppDuration) == -1) || date.compareTo(new LocalDate()) == -1)
                appointment.setDone(true);
        }
    }

}
