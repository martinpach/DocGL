package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.api.*;
import com.docgl.db.*;
import com.docgl.entities.Appointment;
import com.docgl.entities.Doctor;
import com.docgl.entities.FreeHours;
import com.docgl.entities.WorkingHours;
import com.docgl.enums.UserType;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    private FreeHoursDAO freeHoursDAO;
    private DoctorDAO doctorDAO;
    private Authorizer authorizer;

    public AppointmentsResource(AppointmentDAO appointmentDAO, WorkingHoursDAO workingHoursDAO, DoctorDAO doctorDAO, PublicHolidaysDAO publicHolidaysDAO, FreeHoursDAO freeHoursDAO) {
        this.appointmentDAO = appointmentDAO;
        this.workingHoursDAO = workingHoursDAO;
        this.publicHolidaysDAO = publicHolidaysDAO;
        this.freeHoursDAO = freeHoursDAO;
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
     * @return List of DayTime object that contains date and List of times in Stings
     */
    @POST
    @Path("interval/times")
    @UnitOfWork
    public List<DayTimes> getAvailableAppointmentTimesOfDateInterval(AvailableAppointmetTimesInput input) {

        if  (input.getId() == null)
            throw new BadRequestException("Property 'id' is missing or not presented!");
        if  (input.getDateFrom() == null)
            throw new BadRequestException("Property 'dateFrom' is missing or not presented!");
        if  (input.getDateTo() == null)
            throw new BadRequestException("Property 'dateTo' or 'date' is missing or not presented!");

        int docID = input.getId();
        LocalDate dateFrom = new LocalDate(input.getDateFrom());
        LocalDate dateTo = new LocalDate(input.getDateTo());
        LocalDate date = dateFrom;
        Date dateDate = input.getDateFrom();

        Doctor doctor = doctorDAO.getDoctor(docID);
        if (!doctor.isWorkingHoursSet() || !doctor.isApproved() || doctor.getDateOfValidity()==null)
            throw new BadRequestException("Its not possible to make appointment at selected Doctor!");

        LocalDate dateOfValidity = new LocalDate(doctor.getDateOfValidity());
        int appDuration = doctor.getAppointmentsDuration();

        List<DayTimes> dayTimesList = new ArrayList<>();
        List<WorkingHours> workingHoursList = workingHoursDAO.getDoctorsWorkingHours(docID);
        List<FreeHours> freeHoursList = freeHoursDAO.getDoctorsFreeHours(docID);

        while (date.compareTo(dateTo) != 1) {
            // Continue, date is not in the future. //
            if (date.compareTo(new LocalDate()) == -1) {
                date = date.plusDays(1);
                dateDate = new Date(dateDate.getTime()+(24*60*60*1000));
                continue;
            }
            // Continue, date is earlier than Doctors Date of Validity. //
            if (date.compareTo(dateOfValidity) == -1) {
                date = date.plusDays(1);
                dateDate = new Date(dateDate.getTime()+(24*60*60*1000));
                continue;
            }
            // Continue, date is an Public Holiday. //
            if (publicHolidaysDAO.isDatePublicHoliday(dateDate)) {
                date = date.plusDays(1);
                dateDate = new Date(dateDate.getTime()+(24*60*60*1000));
                continue;
            }

            List<Appointment> appointments = appointmentDAO.getDoctorsAppointmentsByDate(docID, dateDate);

            int dayOfWeek = date.getDayOfWeek();
            OfficeHours officeHours = new OfficeHours();
            officeHours.setOfficeHours(dayOfWeek, workingHoursList);
            // Continue, that date doctor does not work. //
            if (officeHours.getFrom() == null && officeHours.getFrom2() == null) {
                date = date.plusDays(1);
                dateDate = new Date(dateDate.getTime()+(24*60*60*1000));
                continue;
            }
            List<LocalTime> availableTimesMorning = new ArrayList<>();
            List<LocalTime> availableTimesAfternoon = new ArrayList<>();

            //String times into joda.LocalTime
            DateTimeFormatter format= DateTimeFormat.forPattern("HH:mm");
            LocalTime officeHoursFrom;
            LocalTime officeHoursTo;

            //Morning hours
            if (officeHours.getFrom() != null) {
                officeHoursFrom = format.parseLocalTime(officeHours.getFrom());
                officeHoursTo = format.parseLocalTime(officeHours.getTo());
                availableTimesMorning = setListOfAvailableTimes(officeHoursFrom, officeHoursTo, appointments, appDuration, freeHoursList, date);
            }
            //Afternoon hours
            if (officeHours.getFrom2() != null) {
                officeHoursFrom = format.parseLocalTime(officeHours.getFrom2());
                officeHoursTo = format.parseLocalTime(officeHours.getTo2());
                availableTimesAfternoon = setListOfAvailableTimes(officeHoursFrom, officeHoursTo, appointments, appDuration, freeHoursList, date);
            }
            List<LocalTime> availableTimes;
            availableTimes = availableTimesMorning;
            if (availableTimesAfternoon != null) {
                for (LocalTime loc:availableTimesAfternoon) {
                    availableTimes.add(loc);
                }
            }
            // That day are all appointment times already taken. //
            if (availableTimes == null){
                date = date.plusDays(1);
                dateDate = new Date(dateDate.getTime()+(24*60*60*1000));
                continue;
            }

            DateTimeFormatter dateformat= DateTimeFormat.forPattern("yyyy-MM-dd");
            DayTimes dayTimes = new DayTimes(date.toString(dateformat));
            for (LocalTime m:availableTimes) {
                dayTimes.getTimes().add(format.print(m));
            }
            dayTimesList.add(dayTimes);
            date = date.plusDays(1);
            dateDate = new Date(dateDate.getTime()+(24*60*60*1000));
        }

        return dayTimesList;
    }


    /**
     *  This function fill List of LocalTimes with available times for an Appointment.
     *  @param appointmentTime start of time when doctor take new Appointments
     *  @param officeHoursTo end of time when doctor take new Appointments
     *  @param appointments List of already assigned Appointments
     *  @param appDuration Duration of an Appointment
     *  @return List of Times in joda.LocalTime
     */
    private List<LocalTime> setListOfAvailableTimes(LocalTime appointmentTime, LocalTime officeHoursTo, List<Appointment> appointments, int appDuration, List<FreeHours> freeHoursList, LocalDate inputDate) {
        List<LocalTime> availableTimes = new ArrayList<>();
        LocalTime takenTime;

        while (appointmentTime.plusMinutes(appDuration).compareTo(officeHoursTo) != 1) {
            availableTimes.add(appointmentTime);
            for (Appointment ap:appointments) {
                takenTime = new LocalTime(ap.getTime());
                if ( takenTime.compareTo(appointmentTime)==0 && !ap.isCanceled() ) {
                    if (availableTimes.size() != 0)
                        availableTimes.remove(availableTimes.size() - 1);
                }
            }
            if (freeHoursList.size() != 0 && isTimeAtFreeHours(freeHoursList, appointmentTime, inputDate)) {
                availableTimes.remove(availableTimes.size() - 1);
            }
            appointmentTime=appointmentTime.plusMinutes(appDuration);
        }
        return availableTimes;
    }


    /**
     * This function checks if inputTime is in doctors working hours and is'nt already taken by another patient.
     * @param appointmentTime start of time when doctor take new Appointments
     * @param officeHoursTo end of time when doctor take new Appointments
     * @param appointments List of already assigned Appointments
     * @param appDuration List of already assigned Appointments
     * @param inputTime new Time of appointment which is going to be checked
     * @return true if time is valid, false if is not
     */
    private boolean isTimeOfAnAppointmentValid(LocalTime appointmentTime, LocalTime officeHoursTo, List<Appointment> appointments, int appDuration, LocalTime inputTime) {
        LocalTime takenTime;
        while (appointmentTime.plusMinutes(appDuration).compareTo(officeHoursTo) != 1) {
            if (inputTime.compareTo(appointmentTime) == 0) {
                for (Appointment ap:appointments) {
                    takenTime = new LocalTime(ap.getTime());
                    if (takenTime.compareTo(inputTime)==0 && !ap.isCanceled())
                        return false;
                }
                return true;
            }
            appointmentTime=appointmentTime.plusMinutes(appDuration);
        }
        return false;
    }

    /**
     * This function checks if input time is'nt during the Doctors free hours
     * @param freeHoursList list of doctors free hours
     * @param inputTime new Time of appointment which is going to be checked
     * @return true if Time is during Doctors free hours, false if is'nt
     */
    private boolean isTimeAtFreeHours(List<FreeHours> freeHoursList, LocalTime inputTime, LocalDate inputDate) {
        if (freeHoursList.size() != 0) {
            LocalTime freeHoursFrom;
            LocalTime freeHoursTo;
            for (FreeHours f:freeHoursList) {
                if (new LocalDate(f.getDate()).compareTo(inputDate) == 0) {
                    freeHoursFrom = new LocalTime(f.getFrom());
                    freeHoursTo = new LocalTime(f.getTo());
                    if (inputTime.compareTo(freeHoursFrom.minusMinutes(19)) == 1 && inputTime.compareTo(freeHoursTo) == -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //TODO save firstname lastneme to patient history.
    /**
     * Resource for creating new appointment, resource check if inputs are valid and time is'nt already taken.
     * @param loggedUser user that is sending request
     * @param patientId id of Patient in Path parameters
     * @param input input withh date, time, note, firstName, lastName, doctorId
     */
    @PUT
    @Path("{id}/new")
    @UnitOfWork
    public void createNewAppointment(@Auth LoggedUser loggedUser, @PathParam("id") int patientId, NewAppointmentInput input) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.PATIENT);
        authorizer.checkAuthentication(loggedUser.getId(), patientId);

        if (input.getDate() == null)
            throw new BadRequestException("Property 'date' is missing or not presented!");
        if (input.getTime() == null)
            throw new BadRequestException("Property 'time' is missing or not presented!");
        if (StringUtils.isBlank(input.getFirstName()))
            throw new BadRequestException("Property 'dateFrom' is missing or not presented!");
        if (StringUtils.isBlank(input.getLastName()))
            throw new BadRequestException("Property 'dateFrom' is missing or not presented!");
        if (input.getDoctorId() == null)
            throw new BadRequestException("Property 'doctorId' is missing or not presented!");

        Doctor doctor = doctorDAO.getDoctor(input.getDoctorId());
        if (doctor == null)
            throw  new BadRequestException("Doctor with id like that does not exist!");
        if (!doctor.isWorkingHoursSet() || !doctor.isApproved() || doctor.getDateOfValidity()==null)
            throw new BadRequestException("Its not possible to make appointment at selected Doctor!");

        Date date = input.getDate();
        LocalTime inputTime = new LocalTime(input.getTime());
        int docId = input.getDoctorId();

        boolean isTimeValid = false;
        boolean isDateValid = true;

        Date dateOfValidity = doctor.getDateOfValidity();
        int appDuration = doctor.getAppointmentsDuration();

        if (date.compareTo(new Date()) == -1 || date.compareTo(dateOfValidity) == -1 || publicHolidaysDAO.isDatePublicHoliday(date))
            isDateValid = false;

        List<Appointment> appointments = appointmentDAO.getDoctorsAppointmentsByDate(docId, date);
        List<WorkingHours> workingHoursList = workingHoursDAO.getDoctorsWorkingHours(docId);
        int dayOfWeek = new LocalDate(date).getDayOfWeek();

        OfficeHours officeHours = new OfficeHours();
        officeHours.setOfficeHours(dayOfWeek, workingHoursList);

        if (officeHours.getFrom() == null && officeHours.getFrom2() == null)
            isDateValid = false;

        List<FreeHours> freeHoursList = freeHoursDAO.getDoctorsFreeHours(docId);
        if (isTimeAtFreeHours(freeHoursList, inputTime, new LocalDate(input.getDate())))
            throw new BadRequestException("Selected Time Doctor has free hours!");

        //String times into joda.LocalTime
        DateTimeFormatter format= DateTimeFormat.forPattern("HH:mm");
        LocalTime officeHoursFrom;
        LocalTime officeHoursTo;
        //Morning hours
        if (officeHours.getFrom() != null) {
            officeHoursFrom = format.parseLocalTime(officeHours.getFrom());
            officeHoursTo = format.parseLocalTime(officeHours.getTo());
            isTimeValid = isTimeOfAnAppointmentValid(officeHoursFrom, officeHoursTo, appointments, appDuration, inputTime);
        }
        //Afternoon hours
        if (!isTimeValid && officeHours.getFrom2() != null) {
            officeHoursFrom = format.parseLocalTime(officeHours.getFrom2());
            officeHoursTo = format.parseLocalTime(officeHours.getTo2());
            isTimeValid = isTimeOfAnAppointmentValid(officeHoursFrom, officeHoursTo, appointments, appDuration, inputTime);
        }

        if (!isDateValid || !isTimeValid)
            throw new BadRequestException("Selected Time or Date is not VALID!");

        if (isDateValid && isTimeValid)
            appointmentDAO.createNewAppointment(input, patientId);

    }

    public List<Doctor> getListOfAvailableDoctorsInDateInterval(AvailableDoctorsInput input) {
        return null;
    }
}
