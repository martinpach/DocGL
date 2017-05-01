package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.Views;
import com.docgl.api.BlockedInput;
import com.docgl.db.AppointmentDAO;
import com.docgl.entities.Appointment;
import com.docgl.enums.SortablePatientColumns;
import com.docgl.enums.SortingWays;
import com.docgl.enums.UserType;
import com.docgl.api.LoggedUser;
import com.docgl.db.PatientDAO;
import com.docgl.entities.Patient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    private Authorizer authorizer;

    public PatientResource(PatientDAO patientDAO, AppointmentDAO appointmentDAO) {
        this.patientDAO = patientDAO;
        this.appointmentDAO = appointmentDAO;
        this.authorizer = new Authorizer();
    }

    @GET
    @UnitOfWork
    public List<Patient> getListOfAllPatients(@Auth LoggedUser loggedUser,
                                              @QueryParam("limit") int limit,
                                              @QueryParam("start") int start,
                                              @QueryParam("sortBy") SortablePatientColumns sortBy,
                                              @QueryParam("way") SortingWays way) {
        UserType[] roles = {UserType.ADMIN, UserType.DOCTOR};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        return patientDAO.getAllPatients(limit, start, sortBy, way);
    }

    @GET
    @Path("{id}/appointments")
    @UnitOfWork
    @JsonView(Views.PatientView.class)
    public List<Appointment> getPatientAppointments(@PathParam("id") int id){
        return appointmentDAO.getAppointments(id, UserType.PATIENT);
    }

    @PUT
    @Path("{id}/blocked")
    @UnitOfWork
    public void changeBlockingState(@Auth LoggedUser loggedUser, @PathParam("id") int id, BlockedInput blockedInput) {
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        patientDAO.blockPatient(blockedInput.isBlocked(), id);
    }

    @GET
    @Path("count")
    @UnitOfWork
    public PatientCountRepresentation getNumberOfAllPatients(){
        return new PatientCountRepresentation(patientDAO.getNumberOfAllPatients());
    }

    private class PatientCountRepresentation{
        @JsonProperty
        private long count;

        PatientCountRepresentation(long count) {
            this.count = count;
        }
    }
}
