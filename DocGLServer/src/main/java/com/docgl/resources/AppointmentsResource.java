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
    public Appointment getAppointment(@PathParam("id") int id) {
        Appointment appointment = appointmentDAO.getAppointment(id);
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
    public void cancelAppointment(@PathParam("id") int id) {
        Appointment appointment = appointmentDAO.getAppointment(id);
        if (appointment.isCanceled() == true) {
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
    public void markAppointmentAsDone(@PathParam("id") int id){
        appointmentDAO.markAppointmentAsDone(id);
    }
}
