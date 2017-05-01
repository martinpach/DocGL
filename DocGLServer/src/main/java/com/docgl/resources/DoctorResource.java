package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.Views;
import com.docgl.api.ApprovedInput;
import com.docgl.api.BlockedInput;
import com.docgl.db.AppointmentDAO;
import com.docgl.entities.Appointment;
import com.docgl.enums.SortableDoctorColumns;
import com.docgl.enums.SortingWays;
import com.docgl.enums.SpecializationsEnum;
import com.docgl.enums.UserType;
import com.docgl.api.LoggedUser;
import com.docgl.db.DoctorDAO;
import com.docgl.entities.Doctor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    private Authorizer authorizer;

    public DoctorResource(DoctorDAO doctorDAO, AppointmentDAO appointmentDAO) {
        this.doctorDAO = doctorDAO;
        this.appointmentDAO = appointmentDAO;
        this.authorizer = new Authorizer();
    }

    @GET
    @UnitOfWork
    public List<Doctor> getListOfAllDoctors(@Auth LoggedUser loggedUser,
                                            @QueryParam("limit") int limit,
                                            @QueryParam("start") int start,
                                            @QueryParam("sortBy") SortableDoctorColumns sortBy,
                                            @QueryParam("way") SortingWays way,
                                            @QueryParam("name") String name,
                                            @QueryParam("spec") SpecializationsEnum spec
    ) {
        UserType[] roles = {UserType.ADMIN, UserType.PATIENT};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        return doctorDAO.getAllDoctors(limit, start, sortBy, way, name, spec);
    }

    @PUT
    @Path("{id}/blocked")
    @UnitOfWork
    public void changeBlockingState(@Auth LoggedUser loggedUser, @PathParam("id") int id, BlockedInput blockedInput) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        doctorDAO.blockDoctor(blockedInput.isBlocked(), id);
    }

    @PUT
    @Path("{id}/approved")
    @UnitOfWork
    public void changeApprovedStatus(@Auth LoggedUser loggedUser, @PathParam("id") int id) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        doctorDAO.approveDoctor(id);
    }

    @GET
    @Path("{id}/appointments")
    @UnitOfWork
    @JsonView(Views.DoctorView.class)
    public List<Appointment> getDoctorAppointments(@PathParam("id") int id) {
        return appointmentDAO.getAppointments(id, UserType.DOCTOR);
    }

    @GET
    @Path("likes")
    @UnitOfWork
    public LikesRepresentation getNumberOfOverallLikes(@Auth LoggedUser loggedUser) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        return new LikesRepresentation(doctorDAO.getNumberOfOverallLikes());
    }

    @GET
    @Path("count")
    @UnitOfWork
    public PatientCountRepresentation getNumberOfAllDoctors(){
        return new PatientCountRepresentation(doctorDAO.getNumberOfAllDoctors());
    }

    private class LikesRepresentation {
        @JsonProperty
        private long likes;

        public LikesRepresentation(long likes) {
            this.likes = likes;
        }
    }

    private class PatientCountRepresentation{
        @JsonProperty
        private long count;

        PatientCountRepresentation(long count) {
            this.count = count;
        }
    }

}
