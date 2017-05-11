package com.docgl.db;

import com.docgl.DateParser;
import com.docgl.api.OfficeHours;
import com.docgl.entities.Appointment;
import com.docgl.entities.Doctor;
import com.docgl.entities.WorkingHours;
import com.docgl.enums.UserType;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Test
    public void getNumberOfAppointmentsTest() {
        long count = dao.getNumberOfAppointments();
        assertEquals(5, count);
    }

    @Test
    public void getAppointmentsTest() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.PATIENT);
        assertEquals(2, appointmentList.size());
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

    @Test
    public void cancelDoctorsAppoitmentsByDateBetweenTimeIntervalTest(){
        dao.cancelDoctorsAppoitmentsByDateBetweenTimeInterval(1, DateParser.parseStringToUtilDate("2017-05-25"), DateParser.parseStringToTime("11:20:00"), DateParser.parseStringToTime("15:50:00"));
        Appointment appointment = dao.getAppointment(4);
        assertEquals(true, appointment.isCanceled());
    }

}
