package com.DocGL.resources;

import com.DocGL.DB.DoctorDAO;
import com.DocGL.entities.Doctor;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Rasťo on 15.4.2017.
 */
@Path("/doctors")
@Produces(MediaType.APPLICATION_JSON)

public class DoctorResource {
    private DoctorDAO doctorDAO;

    public DoctorResource(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
    }

    @GET
    @UnitOfWork
    public List<Doctor> getListOfAllDoctors(){
        return doctorDAO.getAllDoctors();
    }
}
