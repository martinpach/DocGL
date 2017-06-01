package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.DateParser;
import com.docgl.FCMessaging;
import com.docgl.Views;
import com.docgl.api.*;
import com.docgl.db.*;
import com.docgl.entities.Appointment;
import com.docgl.entities.FreeHours;
import com.docgl.entities.WorkingHours;
import com.docgl.enums.*;
import com.docgl.entities.Doctor;
import com.docgl.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Time;
import java.util.*;

/**
 * Created by Rasťo on 15.4.2017.
 * Java class for Resources that are related with Patient.
 */
@Path("/doctors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class DoctorResource {
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;
    private WorkingHoursDAO workingHoursDAO;
    private FreeHoursDAO freeHoursDAO;
    private PatientDAO patientDAO;
    private Authorizer authorizer;

    public DoctorResource(DoctorDAO doctorDAO, AppointmentDAO appointmentDAO, WorkingHoursDAO workingHoursDAO, FreeHoursDAO freeHoursDAO, PatientDAO patientDAO) {
        this.doctorDAO = doctorDAO;
        this.appointmentDAO = appointmentDAO;
        this.workingHoursDAO = workingHoursDAO;
        this.freeHoursDAO = freeHoursDAO;
        this.patientDAO = patientDAO;
        this.authorizer = new Authorizer();
    }

    /**
     * Resource for getting doctors.
     * @param loggedUser is user that is sending a request
     * @param limit is number of returned doctors
     * @param start is first doctor to be returned
     * @param sortBy column to sort results by
     * @param way ascending or descending (asc, desc)
     * @param name searched doctor name
     * @param spec searched doctor specialization
     * @return List of filtered doctors. If no params are presented then all doctors are returned.
     */
    @GET
    @UnitOfWork
    @PermitAll
    public List<Doctor> getListOfAllDoctors(@Auth LoggedUser loggedUser,
                                            @QueryParam("limit") int limit,
                                            @QueryParam("start") int start,
                                            @QueryParam("sortBy") SortableDoctorColumns sortBy,
                                            @QueryParam("way") SortingWays way,
                                            @QueryParam("name") String name,
                                            @QueryParam("spec") SpecializationsEnum spec
    ) {
        return doctorDAO.getAllDoctors(limit, start - 1, sortBy, way, name, spec);
    }

    /**
     * Resource for block/unblock doctor
     * @param loggedUser user that is sending a request
     * @param id selected doctor
     * @param blockedInput json input with one property (blocked : true/false)
     */
    @PUT
    @Path("{id}/blocked")
    @UnitOfWork
    public void changeBlockingState(@Auth LoggedUser loggedUser, @PathParam("id") int id, BlockedInput blockedInput) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        doctorDAO.blockDoctor(blockedInput.isBlocked(), id);
    }

    /**
     * Resource for approve doctor
     * @param loggedUser user that is sending a request
     * @param id selected doctor
     */
    @PUT
    @Path("{id}/approved")
    @UnitOfWork
    public void changeApprovedStatus(@Auth LoggedUser loggedUser, @PathParam("id") int id) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        doctorDAO.approveDoctor(id);
    }

    /**
     * Resource for getting all appointments of doctor
     * @param id selected doctor
     * @return list of all appointments
     */
    @GET
    @Path("{id}/appointments")
    @UnitOfWork
    @JsonView(Views.DoctorView.class)
    public List<?> getDoctorAppointments(@Auth LoggedUser loggedUser,
                                                   @PathParam("id") int id,
                                                   @QueryParam("date") String dateInput,
                                                   @QueryParam("timePeriod") TimePeriod timePeriod) {
        UserType[] roles = {UserType.ADMIN, UserType.DOCTOR};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        if (loggedUser.getUserType() == UserType.DOCTOR)
            authorizer.checkAuthentication(loggedUser.getId(), id);

        if(StringUtils.isNotBlank(dateInput)) {
            Date date = DateParser.parseStringToUtilDate(dateInput);
            Date defaultDate = date;

            if (date != null && (timePeriod == null || timePeriod.equals(TimePeriod.TODAY))) {
                List<Appointment> appointments = appointmentDAO.getDoctorsAppointmentsByDate(id, DateParser.addDaysToDate(1, date));
                Collections.sort(appointments);
                return appointments;
            }
            if (date != null && timePeriod.equals(TimePeriod.WEEK)) {
                List<WeeklyAppointmentsRepresentation> weeklyAppointments = new ArrayList<>();
                for (int i = 1; i < 8; i++) {
                    date = DateParser.addDaysToDate(i, defaultDate);
                    WeeklyAppointmentsRepresentation weeklyAppointment =
                            new WeeklyAppointmentsRepresentation(date, appointmentDAO.getDoctorsAppointmentsByDate(id, date).size());
                    weeklyAppointments.add(weeklyAppointment);
                }
                return weeklyAppointments;
            }
        }
        return appointmentDAO.getAppointments(id, UserType.DOCTOR);
    }

    /**
     * Resource for getting number of all likes
     * @param loggedUser user that is sending a request
     * @return Number of likes in json representation (count : {numberOfLikes})
     */
    @GET
    @Path("likes")
    @UnitOfWork
    public CountRepresentation getNumberOfOverallLikes(@Auth LoggedUser loggedUser) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        return new CountRepresentation(doctorDAO.getNumberOfOverallLikes());
    }

    /**
     * Resource for getting number of all doctors
     * @return number of all doctors in json representation (count : {numberOfDoctors})
     */
    @GET
    @Path("count")
    @UnitOfWork
    public CountRepresentation getNumberOfAllDoctors(){
        return new CountRepresentation(doctorDAO.getNumberOfAllDoctors());
    }
    /**
     * Resource for getting doctor specializations
     * @return list of all specializations
     */
    @GET
    @Path("specializations")
    @UnitOfWork
    public List<SpecializationsEnum> getSpecializations(){
        SpecializationsEnum[] array = SpecializationsEnum.values();
        return Arrays.asList(array);
    }
    /**
     * Resource for changing doctors password
     * @param loggedUser is logged user that is sending request
     * @param id chosen admin
     * @param passwordInput new password
     */
    @PUT
    @Path("{id}/profile/password")
    @UnitOfWork
    public void changePassword(@Auth LoggedUser loggedUser, @PathParam("id") int id, PasswordInput passwordInput) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        if (StringUtils.isBlank(passwordInput.getPassword())){
            throw new BadRequestException("Property 'password' is missing or not presented!");
        }
        if (!doctorDAO.isPasswordDifferent(passwordInput.getPassword(), id)){
            throw new BadRequestException("New password must be different than the old one!");
        }
        doctorDAO.setPassword(passwordInput.getPassword(), id);
    }
    /**
     * Resource for changing appointments duration
     * @param loggedUser is logged user that is sending request
     * @param id chosen admin
     * @param durationInput appointments duration in minutes
     */
    @PUT
    @Path("{id}/duration")
    @UnitOfWork
    public void setAppointmentsDuration(@Auth LoggedUser loggedUser, @PathParam("id") int id, AppointmentsDurationInput durationInput) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        if (durationInput.getDuration() == 0) {
            throw new BadRequestException("Property 'duration' is missing or not presented!");
        }
        doctorDAO.setAppointmentsDuration(durationInput.getDuration(), id);
    }

    /**
     * Resource for setting date of validity for doctor
     * @param id chosen doctor
     * @param date chosen date
     */
    @PUT
    @Path("{id}/validity")
    @UnitOfWork
    public void setDoctorsValidity(@Auth LoggedUser loggedUser, @PathParam("id") int id, DateOfValidityInput date){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        doctorDAO.setDoctorsValidity(id, date.getDate());
    }

    /**
     * Resource for getting working hours for exact doctor
     * @param id chosen doctor
     * @return working hours of doctor
     */
    @GET
    @Path("{id}/workingHours")
    @UnitOfWork
    @PermitAll
    public List<WorkingHours> getDoctorsWorkingHours(@PathParam("id") int id){
        return workingHoursDAO.getDoctorsWorkingHours(id);
    }

    /**
     * Resource for setting working hours for exact doctor
     * @param id chosen doctor
     * @param workingHours working hours for whole week (if list has length 2 then second item represents second working time interval
     */
    @POST
    @Path("{id}/workingHours")
    @UnitOfWork
    public void setDoctorsWokingHours(@Auth LoggedUser loggedUser, @PathParam("id") int id, List<WorkingHours> workingHours){
        authorizer.checkAuthentication(loggedUser.getId(), id);
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        WorkingHours interval = workingHours.get(0);
        Doctor doctor = doctorDAO.getDoctor(id);
        interval.setDoctor(doctor);
        workingHoursDAO.setDoctorsWorkingHours(interval);

        if(workingHours.size() == 2){
            interval = workingHours.get(1);
            interval.setDoctor(doctor);
            workingHoursDAO.setDoctorsWorkingHours(interval);
        }
        doctorDAO.markDoctorSetWorkingHours(id);
    }

    /**
     * Resource for updating working hours for exact doctor
     * @param loggedUser user that is sending request
     * @param id chosen doctor
     * @param workingHours updated working hours
     */
    @PUT
    @Path("{id}/workingHours")
    @UnitOfWork
    public void changeDoctorsWorkingHours(@Auth LoggedUser loggedUser, @PathParam("id") int id, List<WorkingHours> workingHours){
        authorizer.checkAuthentication(loggedUser.getId(), id);
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        Appointment lastAppointment = appointmentDAO.getDoctorsLastAppointment(id);
        if(new Date().before(lastAppointment.getDate()) || new Date().before(lastAppointment.getTime())){
            throw new BadRequestException("Cannot set working hours because you have appointments already planned");
        }
        workingHoursDAO.updateDoctorsWorkingHours(id, workingHours);

    }

    /**
     * Resource for updating doctors' profile
     * @param loggedUser user that is sending request
     * @param id chosen doctor
     * @param doctor json input with updated fields
     * @return updated doctor
     */
    @PUT
    @Path("{id}/profile")
    @UnitOfWork
    public Doctor updateDoctorsProfile(@Auth LoggedUser loggedUser, @PathParam("id") int id, DoctorInput doctor){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        doctorDAO.updateProfile(doctor.getFirstName(), doctor.getLastName(),
                doctor.getEmail(), doctor.getPassword(), doctor.getPhone(), id);
        return doctorDAO.getDoctor(id);
    }

    /**
     * Resource for getting chosen doctor
     * @param id chosen doctor
     * @return chosen doctor
     */
    @GET
    @Path("{id}")
    @UnitOfWork
    @PermitAll
    public Doctor getDoctorsProfile(@PathParam("id") int id){
        return doctorDAO.getDoctor(id);
    }

    /**
     * Resource for setting doctors' free hours
     * @param loggedUser user that is sending request
     * @param id chosen doctor
     * @param freeHours json input with selected date and from - to time interval
     */
    @PUT
    @Path("{id}/freeHours")
    @UnitOfWork
    public void setDoctorsFreeHours(@Auth LoggedUser loggedUser, @PathParam("id") int id, FreeHours freeHours){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.DOCTOR);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        if(freeHours.getFrom() == null){
            throw new ValidationException("Property 'from' is missing");
        }
        if(freeHours.getTo() == null){
            throw new ValidationException("Property 'to' is missing");
        }
        if(freeHours.getDate() == null){
            throw new ValidationException("Property 'date' is missing");
        }
        freeHours.setFrom(new Time((long)(freeHours.getFrom().getTime() + 3.6e+6)));
        freeHours.setTo(new Time((long)(freeHours.getTo().getTime() + 3.6e+6)));
        freeHours.setDoctor(doctorDAO.getDoctor(id));
        freeHoursDAO.setDoctorsFreeHours(freeHours);
        List<Appointment> cancelledAppointments = appointmentDAO.cancelDoctorsAppoitmentsByDateBetweenTimeInterval(id,
                freeHours.getDate(),
                freeHours.getFrom(),
                freeHours.getTo()
        );
        for(Appointment cancelledAppointment : cancelledAppointments) {
            try {
                new FCMessaging().pushFCMNotification(patientDAO.getFCMRegistrationToken(cancelledAppointment.getPatient().getId()), "DocGL", "Your appointment (" + cancelledAppointment.getDate() + ") was cancelled.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Resource for getting doctors' free hours
     * @param id chosen doctor
     * @return chosen doctor free hours
     */
    @GET
    @Path("{id}/freeHours")
    @UnitOfWork
    @PermitAll
    public List<FreeHours> getDoctorsFreeHours(@PathParam("id") int id){
        return freeHoursDAO.getDoctorsFreeHours(id);
    }

    /**
     * Resource for getting List of doctors working days.
     * @param id chosen doctor
     * @return List of Strings
     */
    @GET
    @Path("{id}/days")
    @UnitOfWork
    public List<String> getDoctorsWorkingDays(@PathParam("id") int id) {
        Doctor doctor = doctorDAO.getDoctor(id);
        if (!doctor.isWorkingHoursSet())
            throw new BadRequestException("Doctor did not set up his working hours!");
        return workingHoursDAO.getDoctorsWorkingDays(id);
    }
}
