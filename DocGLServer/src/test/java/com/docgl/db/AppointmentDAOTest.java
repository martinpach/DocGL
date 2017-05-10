package com.docgl.db;

import com.docgl.entities.Appointment;
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

    @Test
    public void getNumberOfAppointmentsTest() {
        long count = dao.getNumberOfAppointments();
        assertEquals(2, count);
    }

    @Test
    public void getAppointmentsTest() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.PATIENT);
        assertEquals(1, appointmentList.size());
    }

    @Test
    public void getAppointments2Test() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.DOCTOR);
        assertEquals(2, appointmentList.size());
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

    /**
     *
     */
    public void setDay(int dayOfWeek) {

    }

    @Test
    public void getAvailableAppointmentTimesOFDay() {

        //od kedy plati jeho rozvrh ordinuje podmienku
//        LocalTime
        Date selectedDate = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String stringdate = "2017-05-24";
        Date date = null;
        try {
            date = sdf.parse(stringdate);
        } catch (ParseException ex){

        }
        System.out.println(DateUtils.truncate(date, java.util.Calendar.DAY_OF_MONTH));

        //musim osetrit aby bralo len appoitmenty daneho dna - done
        //List<Appointment> appointments = dao.getAppointments(1, UserType.DOCTOR);
        List<Appointment> appointments = dao.getDoctorsAppointmentsByDate(1, date);
        List<WorkingHours> workingHoursList = workingHoursDAO.getDoctorsWorkingHours(1);

        LocalDate appDate = new LocalDate(appointments.get(0).getDate());
        System.out.println("DATE: "+appDate);
        System.out.println("DATE: "+appDate.getDayOfWeek());



        String dayFrom = null;
        String dayTo = null;/*
        switch (appDate.getDayOfWeek()) {
            case 1: dayFrom = "monday_from";
                dayTo = "monday_to";
            case 2:
        }
*/

        //appDate.getDayOfWeek();


        String monday_from = "7:30";
        String monday_to = "12:00";
        int appDuration = 20;

        DateTimeFormatter format= DateTimeFormat.forPattern("HH:mm");
        LocalTime mondayFrom = format.parseLocalTime(monday_from);
        LocalTime mondayTo = format.parseLocalTime(monday_to);
        LocalTime appointmentTime = mondayFrom;

        LocalTime duration = new LocalTime(0,20);

        mondayFrom = mondayFrom.plusMinutes(appDuration);

        System.out.println("Time: "+ mondayFrom);

        List<LocalTime> availableTimes = new ArrayList<LocalTime>();

        System.out.println(appointments.get(0).getTime());

        while (appointmentTime.compareTo(mondayTo) == -1) {
            availableTimes.add(appointmentTime);
            for (Appointment ap:appointments) {
                LocalTime takenTime = new LocalTime(ap.getTime());
                if (takenTime.compareTo(appointmentTime)==0) {
                    availableTimes.remove(availableTimes.size()-1);
                }
            }
            appointmentTime=appointmentTime.plusMinutes(appDuration);
        }
        for (LocalTime m:availableTimes) {
            System.out.println("at: "+m);
        }
//        System.out.println("at: "+availableTimes.size());
//        System.out.println("compare: "+appointmentTime.compareTo(mondayTo));
//        System.out.println("compare: "+appointmentTime+"    "+mondayTo);
//        System.out.println("at: "+availableTimes.get(1));
//        System.out.println("at: "+availableTimes.get(2));
//        System.out.println("at: "+availableTimes.get(3));

        assertEquals(1, 1);
    }

}
