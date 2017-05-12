package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.Views;
import com.docgl.api.*;
import com.docgl.db.AppointmentDAO;
import com.docgl.db.DoctorDAO;
import com.docgl.entities.Appointment;
import com.docgl.entities.Doctor;
import com.docgl.enums.SortablePatientColumns;
import com.docgl.enums.SortingWays;
import com.docgl.enums.UserType;
import com.docgl.db.PatientDAO;
import com.docgl.entities.Patient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * Created by Rasťo on 22.4.2017.
 */
@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PatientResource {
    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;
    private DoctorDAO doctorDAO;
    private Authorizer authorizer;

    public PatientResource(PatientDAO patientDAO, AppointmentDAO appointmentDAO, DoctorDAO doctorDAO) {
        this.patientDAO = patientDAO;
        this.appointmentDAO = appointmentDAO;
        this.doctorDAO = doctorDAO;
        this.authorizer = new Authorizer();
    }


    /**
     * Resource for getting patients.
     * @param loggedUser is user that is sending a request
     * @param limit is number of returned patients
     * @param start is first patient to be returned
     * @param sortBy column to sort results by
     * @param way ascending or descending (asc, desc)
     * @param name searched patient name
     * @return List of filtered patients. If no params are presented then all patients are returned.
     */
    @GET
    @UnitOfWork
    public List<Patient> getListOfAllPatients(@Auth LoggedUser loggedUser,
                                              @QueryParam("limit") int limit,
                                              @QueryParam("start") int start,
                                              @QueryParam("sortBy") SortablePatientColumns sortBy,
                                              @QueryParam("way") SortingWays way,
                                              @QueryParam("name") String name)
    {
        UserType[] roles = {UserType.ADMIN, UserType.DOCTOR};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        return patientDAO.getAllPatients(limit, start - 1, sortBy, way, name);
    }

    /**
     * Resource for getting all appointments of patient
     * @param id selected patient
     * @return list of all appointments
     */
    @GET
    @Path("{id}/appointments")
    @UnitOfWork
    @JsonView(Views.PatientView.class)
    public List<Appointment> getPatientAppointments(@Auth LoggedUser loggedUser, @PathParam("id") int id){
        UserType[] roles = {UserType.ADMIN, UserType.PATIENT};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        if (loggedUser.getUserType() == UserType.PATIENT)
            authorizer.checkAuthentication(loggedUser.getId(), id);
        return appointmentDAO.getAppointments(id, UserType.PATIENT);
    }

    /**
     * Resource for block/unblock patient
     * @param loggedUser user that is sending a request
     * @param id selected patient
     * @param blockedInput json input with one property (blocked : true/false)
     */
    @PUT
    @Path("{id}/blocked")
    @UnitOfWork
    public void changeBlockingState(@Auth LoggedUser loggedUser, @PathParam("id") int id, BlockedInput blockedInput) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        patientDAO.blockPatient(blockedInput.isBlocked(), id);
    }

    /**
     * Resource for getting number of all patients
     * @return number of all patients in json representation (count : {numberOfPatients})
     */
    @GET
    @Path("count")
    @UnitOfWork
    @PermitAll
    public CountRepresentation getNumberOfAllPatients(){
        return new CountRepresentation(patientDAO.getNumberOfAllPatients());
    }

    /**
     * Resource for changing patients password
     * @param loggedUser is logged user that is sending request
     * @param id chosen patient
     * @param passwordInput new password
     */
    @PUT
    @Path("{id}/profile/password")
    @UnitOfWork
    public void changePassword(@Auth LoggedUser loggedUser, @PathParam("id") int id, PasswordInput passwordInput) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.PATIENT);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        if(StringUtils.isBlank(passwordInput.getPassword())){
            throw new BadRequestException("Property 'password' is missing or not presented!");
        }
        if (!patientDAO.isPasswordDifferent(passwordInput.getPassword(), id)){
            throw new BadRequestException("New password must be different than the old one!");
        }
        patientDAO.setPassword(passwordInput.getPassword(), id);
    }
    /**
     * Resource for getting all doctors in patient favourite collection
     * @param id selected patient
     * @return Collection of favourite doctors
     */
    @GET
    @Path("{id}/favourite")
    @UnitOfWork
    public Collection<Doctor> getFavouriteDoctors(@Auth LoggedUser loggedUser, @PathParam("id") int id){
        UserType[] roles = {UserType.ADMIN, UserType.PATIENT};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        if (loggedUser.getUserType() == UserType.PATIENT)
            authorizer.checkAuthentication(loggedUser.getId(), id);

        return patientDAO.getFavouriteDoctors(id);
    }
    /**
     * Resource for adding and removing doctor from patient favourite list alse add and remove one doctors like
     * @param loggedUser is logged user that is sending request
     * @param id chosen patient
     * @param doctorIdInput new favourite doctor ID
     */
    @PUT
    @Path("{id}/favourite")
    @UnitOfWork
    public void addDoctorToFavourite(@Auth LoggedUser loggedUser, @PathParam("id") int id, DoctorIdInput doctorIdInput) {
        UserType[] roles = {UserType.ADMIN, UserType.PATIENT};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        if (loggedUser.getUserType() == UserType.PATIENT)
            authorizer.checkAuthentication(loggedUser.getId(), id);

        if (doctorIdInput.getDoctorId() == null) {
            throw  new BadRequestException("Property 'doctorId' is missing or not presented!");
        }
        Doctor doctor = doctorDAO.getDoctor(doctorIdInput.getDoctorId());
        if (doctorDAO.getDoctor(doctorIdInput.getDoctorId()) == null)
            throw  new BadRequestException("Doctor with id like that does not exist!");
        if (patientDAO.getFavouriteDoctors(id).contains(doctor)) {
            patientDAO.removeDoctorFromFavourite(id, doctorIdInput.getDoctorId());
            doctorDAO.removeLikeFromDoctor(doctorIdInput.getDoctorId());
        }
        else {
            patientDAO.addDoctorToFavourite(id, doctorIdInput.getDoctorId());
            doctorDAO.addLikeToDoctor(doctorIdInput.getDoctorId());
        }
    }

    /**
     * Resource for updating chosen patient profile
     * @param loggedUser user that is sending request
     * @param id chosen patient
     * @param patient json input with update values (if any field is empty the value will not be updated)
     */
    @PUT
    @Path("{id}/profile")
    @UnitOfWork
    public void updatePatientsProfile(@Auth LoggedUser loggedUser, @PathParam("id") int id, UserInput patient){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.PATIENT);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        patientDAO.updateProfile(patient.getFirstName(), patient.getLastName(), patient.getEmail(), id);
    }
}
