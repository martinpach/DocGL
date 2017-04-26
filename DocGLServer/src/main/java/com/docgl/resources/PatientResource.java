package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.UserType;
import com.docgl.api.LoggedUser;
import com.docgl.api.RegistrationInput;
import com.docgl.db.PatientDAO;
import com.docgl.entities.Patient;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;

/**
 * Created by Rasťo on 22.4.2017.
 */
@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PatientResource {
    private PatientDAO patientDAO;
    private Authorizer authorizer;

    public PatientResource(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
        this.authorizer = new Authorizer();
    }

    @GET
    @UnitOfWork
    public List<Patient> getListOfAllPatients(@Auth LoggedUser loggedUser) {
        UserType[] roles = {UserType.ADMIN, UserType.DOCTOR};
        authorizer.checkAuthorization(loggedUser.getUserType(), roles);
        return patientDAO.getAllPatients();
    }
}
