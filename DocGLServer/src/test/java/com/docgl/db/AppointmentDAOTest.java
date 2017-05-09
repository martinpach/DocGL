package com.docgl.db;

import com.docgl.entities.Appointment;
import com.docgl.enums.UserType;
import org.junit.Test;

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

    @Test
    public void getNumberOfAppointmentsTest() {
        long count = dao.getNumberOfAppointments();
        assertEquals(10, count);
    }

    @Test
    public void getAppointments() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.PATIENT);
        assertEquals(9, appointmentList.size());
    }

    @Test
    public void getAppointments2() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.DOCTOR);
        assertEquals(10, appointmentList.size());
    }
    /**
     * getAppointment test
     */
    @Test
    public void getAppointmentTest() {
        Appointment appointment = dao.getAppointment(1);
        assertEquals("test", appointment.getNote());
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


}
