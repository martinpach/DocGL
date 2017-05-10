package com.docgl.db;

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
        assertEquals(4, count);
    }

    @Test
    public void getAppointmentsTest() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.PATIENT);
        assertEquals(1, appointmentList.size());
    }

    @Test
    public void getAppointments2Test() {
        List<Appointment> appointmentList = dao.getAppointments(1, UserType.DOCTOR);
        assertEquals(4, appointmentList.size());
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
     *
     */
    private List<LocalTime> setListOfAvailableTimes(LocalTime officeHoursFrom, LocalTime officeHoursTo, List<Appointment> appointments, int appDuration) {
        List<LocalTime> availableTimes = new ArrayList<LocalTime>();
        LocalTime appointmentTime = officeHoursFrom;

        while (appointmentTime.plusMinutes(appDuration).compareTo(officeHoursTo) != 1) {
            availableTimes.add(appointmentTime);
            for (Appointment ap:appointments) {
                LocalTime takenTime = new LocalTime(ap.getTime());
                if (takenTime.compareTo(appointmentTime)==0) {
                    availableTimes.remove(availableTimes.size()-1);
                }
            }
            appointmentTime=appointmentTime.plusMinutes(appDuration);
        }
        return availableTimes;
    }

    @Test
    public void getDoctorsDays() {
        Doctor doctor = doctorDAO.getDoctor(1);
        if (doctor.isWorkingHoursSet()) {
            List<String> workingDays = new ArrayList<String>();
            List<WorkingHours> workingHoursList = workingHoursDAO.getDoctorsWorkingHours(1);
            WorkingHours morningHours = null;
            WorkingHours afternoonHours = null;
            morningHours = workingHoursList.get(0);
            afternoonHours = workingHoursList.get(1);
            if (morningHours.getMondayFrom() != null || afternoonHours.getMondayFrom() != null)
                workingDays.add("Monday");
            if (morningHours.getThursdayFrom() != null || afternoonHours.getThursdayFrom() != null)
                workingDays.add("Tuesday");
            if (morningHours.getWednesdayFrom() != null || afternoonHours.getWednesdayFrom() != null)
                workingDays.add("Wednesday");
            if (morningHours.getThursdayFrom() != null || afternoonHours.getThursdayFrom() != null)
                workingDays.add("Thursday");
            if (morningHours.getFridayFrom() != null || afternoonHours.getFridayFrom() != null)
                workingDays.add("Friday");
            //return workingDays;
            for (String s:workingDays)
                System.out.println(s);
        }
        //return null;

        assertEquals(1, 1);
    }


    @Test
    public void getAvailableAppointmentTimesOFDay() {

        Doctor doctor = doctorDAO.getDoctor(1);
        Date startingDoctorDate = doctor.getDateOfValidity();
        int appDuration = doctor.getAppointmentsDuration();
        System.out.println("DateOfValidity: "+startingDoctorDate);

        //od kedy plati jeho rozvrh ordinuje podmienku
//        LocalTime
        //Date selectedDate = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String stringdate = "2017-05-24";
        Date date = null;
        try {
            date = sdf.parse(stringdate);
        } catch (ParseException ex){

        }
        //System.out.println(DateUtils.truncate(date, java.util.Calendar.DAY_OF_MONTH));

        //ci us vtedy ordinuje ak nie vrati false
        /*
        if (date.compareTo(startingDoctorDate) == -1)
            System.out.println("JE TO MENSIE");
        */
        //musim osetrit aby bralo len appoitmenty daneho dna - done
        List<Appointment> appointments = dao.getDoctorsAppointmentsByDate(1, new Date());
        List<WorkingHours> workingHoursList = workingHoursDAO.getDoctorsWorkingHours(1);


        LocalDate appDate = new LocalDate(appointments.get(0).getDate());
        System.out.println("DATE: "+appDate);
        System.out.println("DATE: "+appDate.getDayOfWeek());


        int dayOfWeek = appDate.getDayOfWeek();

        OfficeHours officeHours = new OfficeHours();
        officeHours.setOfficeHours(dayOfWeek, workingHoursList);


        System.out.println("DAY FROM: "+officeHours.getFrom()+" DAY TO: "+officeHours.getTo()+"    "+ workingHoursList.get(0).getThursdayFrom());

        List<LocalTime> availableTimesMorning = new ArrayList<LocalTime>();
        List<LocalTime> availableTimesAfternoon = new ArrayList<LocalTime>();
        List<LocalTime> availableTimes = new ArrayList<LocalTime>();

        //String times into joda.LocalTime
        DateTimeFormatter format= DateTimeFormat.forPattern("HH:mm");
        LocalTime officeHoursFrom = null;
        LocalTime officeHoursTo = null;
        //Morning hours
        if (officeHours.getFrom() != null) {
            officeHoursFrom = format.parseLocalTime(officeHours.getFrom());
            officeHoursTo = format.parseLocalTime(officeHours.getTo());
            availableTimesMorning = setListOfAvailableTimes(officeHoursFrom, officeHoursTo, appointments, appDuration);
        }
        //Afternoon hours
        if (officeHours.getFrom2() != null) {
            officeHoursFrom = format.parseLocalTime(officeHours.getFrom2());
            officeHoursTo = format.parseLocalTime(officeHours.getTo2());
            availableTimesAfternoon = setListOfAvailableTimes(officeHoursFrom, officeHoursTo, appointments, appDuration);
        }

        availableTimes = availableTimesMorning;
        if (availableTimesAfternoon != null) {
            for (LocalTime loc:availableTimesAfternoon) {
                availableTimes.add(loc);
            }
        }

        for (LocalTime m:availableTimes) {
            System.out.println("at: "+m);
        }

        assertEquals(1, 1);
    }


}
