package com.docgl.resources;

import com.docgl.Authorizer;
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

    public DoctorResource(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
    }

    @GET
    @UnitOfWork
    public List<Doctor> getListOfAllDoctors(@Auth Principal loggedUser, @QueryParam("limit") int limit, @QueryParam("start") int start){
        Authorizer authorizer = new Authorizer();
        if(authorizer.hasPermission(loggedUser.getName(), "admin")){
            return doctorDAO.getAllDoctors(limit, start);
        }
        throw new NotAuthorizedException("Don't have permission!");
    }
}
