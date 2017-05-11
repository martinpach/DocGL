package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.api.AvailableAppointmetTimesInput;
import com.docgl.api.CountRepresentation;
import com.docgl.api.LoggedUser;
import com.docgl.api.OfficeHours;
import com.docgl.db.AppointmentDAO;
import com.docgl.db.DoctorDAO;
import com.docgl.db.PublicHolidaysDAO;
import com.docgl.db.WorkingHoursDAO;
import com.docgl.entities.Appointment;
import com.docgl.entities.Doctor;
import com.docgl.entities.WorkingHours;
import com.docgl.enums.TimePeriod;
import com.docgl.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Martin on 29.4.2017.
 */

@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentsResource {

    private AppointmentDAO appointmentDAO;
    private WorkingHoursDAO workingHoursDAO;
    private PublicHolidaysDAO publicHolidaysDAO;
    private DoctorDAO doctorDAO;
    private Authorizer authorizer;

    public AppointmentsResource(AppointmentDAO appointmentDAO, WorkingHoursDAO workingHoursDAO, DoctorDAO doctorDAO, PublicHolidaysDAO publicHolidaysDAO) {
        this.appointmentDAO = appointmentDAO;
        this.workingHoursDAO = workingHoursDAO;
        this.publicHolidaysDAO = publicHolidaysDAO;
        this.doctorDAO = doctorDAO;
        this.authorizer = new Authorizer();
    }

    /**
     * Resource for getting number of appointments for today
     * @param loggedUser is user that is sending request
     * @return number of appointments in json representation
     */
    @GET
    @Path("count")
    @UnitOfWork
    public CountRepresentation getNumberOfAppointments(@Auth LoggedUser loggedUser){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        return new CountRepresentation(appointmentDAO.getNumberOfAppointments());
    }

    /**
     * Resource for getting appointment by ID
     * @param id appointment ID
     * @return appointment in json representation
     */
    @GET
    @Path("{id}")
    @UnitOfWork
    public Appointment getAppointment(@Auth LoggedUser loggedUser, @PathParam("id") int id) {
        Appointment appointment = appointmentDAO.getAppointment(id);
        if(loggedUser.getUserType().equals(UserType.DOCTOR)){
            authorizer.checkAuthentication(appointment.getDoctor().getId(),loggedUser.getId());
        }
        else if(loggedUser.getUserType().equals(UserType.PATIENT)){
            authorizer.checkAuthentication(appointment.getPatient().getId(), loggedUser.getId());
        }
        if (appointment == null)
            throw new BadRequestException("Appointment with id like that does not exist!");
        return appointmentDAO.getAppointment(id);
    }
    /**
     * Resource for cancelling appointment by his ID
     * @param id appointment id
     */
    @DELETE
    @Path("{id}")
    @UnitOfWork
    public void cancelAppointment(@Auth LoggedUser loggedUser, @PathParam("id") int id) {
        Appointment appointment = appointmentDAO.getAppointment(id);
        int patientId = appointment.getPatient().getId();
        int doctorId = appointment.getDoctor().getId();
        UserType[] roles = {UserType.DOCTOR, UserType.PATIENT};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        if(loggedUser.getUserType().equals(UserType.DOCTOR)){
            authorizer.checkAuthentication(doctorId, loggedUser.getId());
        }
        else{
            authorizer.checkAuthentication(patientId, loggedUser.getId());
        }
        if (appointment == null)
            throw new BadRequestException("Appointment with id like that does not exist!");
        if (appointment.isCanceled()) {
            throw new BadRequestException("Appointment is already canceled!");
        }
        appointmentDAO.cancelAppointment(id);
    }

    /**
     * Resource for marking appointment as done
     */
    @PUT
    @Path("{id}/done")
    @UnitOfWork
    public void markAppointmentAsDone(@Auth LoggedUser loggedUser, @PathParam("id") int id){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        Appointment appointment = appointmentDAO.getAppointment(id);
        authorizer.checkAuthentication(loggedUser.getId(), appointment.getDoctor().getId());
        appointmentDAO.markAppointmentAsDone(id);
    }


    /**
     * This is a resource for getting all the available times for an appointment.
     * Function: Checks if the doctor has set his working hours, if is approved, Date of Validity is set,
     * checks if that day isn't a public holiday in case the doctor has set his working hours that day.
     * After this Function, doctor's working hours are checked and all possible appointment times for that
     * day are returned.
     *
     * @param input input with Date and Id of Doctor
     * @return List of times presented in String format
     */
    @POST
    @Path("day/times")
    @UnitOfWork
    public List<String> getAvailableAppointmentTimesOFDay(AvailableAppointmetTimesInput input) {

        Date date = input.getDate();
        int docID = input.getId();

        if (date.compareTo(new Date()) == -1)
            throw new BadRequestException("Appointment date must be set in the future!");
        Doctor doctor = doctorDAO.getDoctor(docID);
        if (!doctor.isWorkingHoursSet() || !doctor.isApproved() || doctor.getDateOfValidity()==null)
            throw new BadRequestException("Its not possible to make appointment at selected Doctor!");

        Date dateOfValidity = doctor.getDateOfValidity();
        int appDuration = doctor.getAppointmentsDuration();

        if (date.compareTo(dateOfValidity) == -1)
            throw new BadRequestException("Date is earlier than Doctors Date of Validity!");
        if (publicHolidaysDAO.isDatePublicHoliday(date))
            throw new BadRequestException("Selected date is an Public Holiday!");

        List<Appointment> appointments = appointmentDAO.getDoctorsAppointmentsByDate(docID, date);
        List<WorkingHours> workingHoursList = workingHoursDAO.getDoctorsWorkingHours(docID);

        int dayOfWeek = new LocalDate(date).getDayOfWeek();

        OfficeHours officeHours = new OfficeHours();
        officeHours.setOfficeHours(dayOfWeek, workingHoursList);

        if (officeHours.getFrom() == null && officeHours.getFrom2() == null)
            throw new BadRequestException("Doctor does not work that day!");

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

        //Morning hours together with Afternoon ones
        availableTimes = availableTimesMorning;
        if (availableTimesAfternoon != null) {
            for (LocalTime loc:availableTimesAfternoon) {
                availableTimes.add(loc);
            }
        }
        if (availableTimes == null)
            throw new BadRequestException("That day are all appointment times already taken!");

        //joda.LocalTime to Strings
        List<String> availableTimesString = new ArrayList<String>();
        for (LocalTime m:availableTimes) {
            availableTimesString.add(format.print(m));
        }
        return availableTimesString;
    }

    /**
     *  This function fill List of LocalTimes with available times for an Appointment.
     *  @param officeHoursFrom start of time when doctor take new Appointments
     *  @param officeHoursTo end of time when doctor take new Appointments
     *  @param appointments List of already assigned Appointments
     *  @param appDuration Duration of an Appointment
     *  @return List of Times in joda.LocalTime
     */
    private List<LocalTime> setListOfAvailableTimes(LocalTime officeHoursFrom, LocalTime officeHoursTo, List<Appointment> appointments, int appDuration) {
        List<LocalTime> availableTimes = new ArrayList<LocalTime>();
        LocalTime appointmentTime = officeHoursFrom;
        LocalTime takenTime;

        while (appointmentTime.plusMinutes(appDuration).compareTo(officeHoursTo) != 1) {
            availableTimes.add(appointmentTime);
            for (Appointment ap:appointments) {
                takenTime = new LocalTime(ap.getTime());
                if (takenTime.compareTo(appointmentTime)==0 && !ap.isCanceled()) {
                    availableTimes.remove(availableTimes.size()-1);
                }
            }
            appointmentTime=appointmentTime.plusMinutes(appDuration);
        }
        return availableTimes;
    }

}
