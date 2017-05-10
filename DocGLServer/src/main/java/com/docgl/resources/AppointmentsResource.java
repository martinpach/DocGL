package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.api.CountRepresentation;
import com.docgl.api.LoggedUser;
import com.docgl.db.AppointmentDAO;
import com.docgl.entities.Appointment;
import com.docgl.enums.TimePeriod;
import com.docgl.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Martin on 29.4.2017.
 */

@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentsResource {

    private AppointmentDAO appointmentDAO;
    private Authorizer authorizer;

    public AppointmentsResource(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
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
}
