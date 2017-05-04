package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.api.LoggedUser;
import com.docgl.db.AppointmentDAO;
import com.docgl.enums.TimePeriod;
import com.docgl.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    public AppointmentsCountRepresentation getNumberOfAppointments(@Auth LoggedUser loggedUser){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        return new AppointmentsCountRepresentation(appointmentDAO.getNumberOfAppointments());
    }

    private class AppointmentsCountRepresentation {
        @JsonProperty
        private long count;

        AppointmentsCountRepresentation(long count) {
            this.count = count;
        }
    }
}
