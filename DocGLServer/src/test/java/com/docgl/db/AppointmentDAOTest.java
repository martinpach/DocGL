package com.docgl.db;

import com.docgl.api.NewAppointmentInput;
import com.docgl.DateParser;
import com.docgl.api.OfficeHours;
import com.docgl.entities.Appointment;
import com.docgl.enums.UserType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by Client on 5.5.2017.
 */
public class AppointmentDAOTest extends AbstractDAO {
    private final AppointmentDAO dao = new AppointmentDAO(sessionFactory);
    private final WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO(sessionFactory);
    private final DoctorDAO doctorDAO = new DoctorDAO(sessionFactory);
    private final PatientDAO patientDAO = new PatientDAO(sessionFactory);

    @Test
    public void getNumberOfAppointmentsTest() {
        long count = dao.getNumberOfAppointments();
        assertEquals(5, count);
    }

    @Test
    public void getAppointmentsTest() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.PATIENT);
        assertEquals(2, appointmentList.size());
        Appointment appointment = appointmentList.get(0);
        assertEquals(new LocalDate(2017,5,24), new LocalDate(appointment.getDate()));
        assertEquals(new LocalTime(7,30), new LocalTime(appointment.getTime()));
    }

    @Test
    public void getAppointments2Test() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.DOCTOR);
        assertEquals(5, appointmentList.size());
    }
    /**
     * getAppointment test
     */
    @Test
    public void getAppointmentTest() {
        Appointment appointment = dao.getAppointment(1);
        assertEquals("Headache", appointment.getNote());
    }
    @Test
    public void getAppointmentTest2() {
        Appointment appointment = dao.getAppointment(50);
        assertNull(appointment);
    }
    /**
     * cancelAppointment test
     */
    @Test
    public void cancelAppointmentTest() {
        Appointment appointment = dao.getAppointment(1);
        assertFalse(appointment.isCanceled());
        dao.cancelAppointment(1);
        assertTrue(appointment.isCanceled());
    }

    @Test
    public void markAppointmentAsDoneTest(){
        dao.markAppointmentAsDone(1);
        Appointment appointment = dao.getAppointment(1);
        assertTrue(appointment.isDone());
    }

    /**
     * createNewAppointment test
     */
    @Test
    public void createNewAppointment() {
        NewAppointmentInput newAppointmentInput = new NewAppointmentInput(stringToDate("2017-05-24"), stringToTime("10:50"), "Head ache.", "Chuck", "Norris", 1);
        dao.createNewAppointment(newAppointmentInput, 4);
        List<Appointment> appointmentList = dao.getAppointments(4, UserType.PATIENT);
        assertEquals(1, appointmentList.size());

        Appointment appointment = appointmentList.get(0);
        System.out.println(stringToDate("2017-05-24"));
        assertEquals(new LocalDate(2017,5,24), new LocalDate(appointment.getDate()));
        assertEquals(new LocalTime(10,50, 0), new LocalTime(appointment.getTime()));
        assertEquals("Head ache.", appointment.getNote());
        assertEquals("Chuck", appointment.getPatientFirstName());
        assertEquals("Norris", appointment.getPatientLastName());
        assertEquals(doctorDAO.getDoctor(1),appointment.getDoctor());
        assertEquals(patientDAO.getPatient(4),appointment.getPatient());
    }
    @Test
    public void createNewAppointment2() {
        NewAppointmentInput newAppointmentInput = new NewAppointmentInput(stringToDate("2017-05-24"), stringToTime("10:50"), "Chuck", "Norris", 1);
        dao.createNewAppointment(newAppointmentInput, 4);
        List<Appointment> appointmentList = dao.getAppointments(4, UserType.PATIENT);
        assertEquals(1, appointmentList.size());

        Appointment appointment = appointmentList.get(0);
        System.out.println(stringToDate("2017-05-24"));
        assertEquals(new LocalDate(2017,5,24), new LocalDate(appointment.getDate()));
        assertEquals(new LocalTime(10,50, 0), new LocalTime(appointment.getTime()));
        assertEquals(null, appointment.getNote());
        assertEquals("Chuck", appointment.getPatientFirstName());
        assertEquals("Norris", appointment.getPatientLastName());
        assertEquals(doctorDAO.getDoctor(1),appointment.getDoctor());
        assertEquals(patientDAO.getPatient(4),appointment.getPatient());
    }

    private Date stringToDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException ex){
        }
        return date;
    }
    private Time stringToTime(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Long ms = null;
        try{
            ms = sdf.parse(string).getTime();
        } catch (ParseException ex){
        }
        return new Time(ms);
    }

    @Test
    public void cancelDoctorsAppoitmentsByDateBetweenTimeIntervalTest(){
        dao.cancelDoctorsAppoitmentsByDateBetweenTimeInterval(1, DateParser.parseStringToUtilDate("2017-05-25"), DateParser.parseStringToTime("11:20:00"), DateParser.parseStringToTime("15:50:00"));
        Appointment appointment = dao.getAppointment(4);
        assertEquals(true, appointment.isCanceled());
    }

}
