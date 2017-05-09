package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.Views;
import com.docgl.api.*;
import com.docgl.db.AppointmentDAO;
import com.docgl.db.WorkingHoursDAO;
import com.docgl.entities.Appointment;
import com.docgl.entities.WorkingHours;
import com.docgl.enums.SortableDoctorColumns;
import com.docgl.enums.SortingWays;
import com.docgl.enums.SpecializationsEnum;
import com.docgl.enums.UserType;
import com.docgl.db.DoctorDAO;
import com.docgl.entities.Doctor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import sun.rmi.runtime.Log;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rasťo on 15.4.2017.
 */
@Path("/doctors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class DoctorResource {
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;
    private WorkingHoursDAO workingHoursDAO;
    private Authorizer authorizer;

    public DoctorResource(DoctorDAO doctorDAO, AppointmentDAO appointmentDAO, WorkingHoursDAO workingHoursDAO) {
        this.doctorDAO = doctorDAO;
        this.appointmentDAO = appointmentDAO;
        this.workingHoursDAO = workingHoursDAO;
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
        return doctorDAO.getAllDoctors(limit, start, sortBy, way, name, spec);
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
    public List<Appointment> getDoctorAppointments(@Auth LoggedUser loggedUser, @PathParam("id") int id) {
        UserType[] roles = {UserType.ADMIN, UserType.DOCTOR};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        if (loggedUser.getUserType() == UserType.DOCTOR)
            authorizer.checkAuthentication(loggedUser.getId(), id);
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
    @PermitAll
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
    @PermitAll
    public List<SpecializationsEnum> getSpecializations(){
        SpecializationsEnum[] array = SpecializationsEnum.values();
        List<SpecializationsEnum> list = Arrays.asList(array);
        return list;
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
        if (passwordInput.getPassword() == null || passwordInput.getPassword().trim().isEmpty()){
            throw new BadRequestException("Property 'password' is missing or not presented!");
        }
        if (doctorDAO.isPasswordDifferent(passwordInput.getPassword(), id)==false) {
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
            throw  new BadRequestException("Property 'duration' is missing or not presented!");
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

}
