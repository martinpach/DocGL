package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.UserType;
import com.docgl.db.DoctorDAO;
import com.docgl.entities.Doctor;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;

/**
 * Created by Rasťo on 15.4.2017.
 */
@Path("/doctors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class DoctorResource {
    private DoctorDAO doctorDAO;
    private Authorizer authorizer;

    public DoctorResource(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
        this.authorizer = new Authorizer();
    }

    @GET
    @UnitOfWork
    public List<Doctor> getListOfAllDoctors(@Auth Principal loggedUser, @QueryParam("limit") int limit, @QueryParam("start") int start){
        authorizer.checkAuthorization(loggedUser.getName(), UserType.ADMIN);
        return doctorDAO.getAllDoctors(limit, start);
    }
}
