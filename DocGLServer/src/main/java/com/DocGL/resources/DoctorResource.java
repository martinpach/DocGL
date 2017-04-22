package com.docgl.resources;

import com.docgl.db.DoctorDAO;
import com.docgl.entities.Doctor;
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

    public DoctorResource(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
    }

    @GET
    @UnitOfWork
    public List<Doctor> getListOfAllDoctors(@QueryParam("limit") int limit, @QueryParam("start") int start){
        return doctorDAO.getAllDoctors(limit, start);
    }

    /*@GET
    @UnitOfWork
    @Path("")
    public List<Doctor> getListOfDoctors(@QueryParam("limit") int limit, @QueryParam("start") int start){
        return doctorDAO.getFilteredDoctors(limit, start);
    }*/
}
