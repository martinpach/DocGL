package com.DocGL.resources;

import com.DocGL.DB.AdminDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by wdfeww on 4/15/17.
 */


@Path("/admins/{id}/profile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminProfileResource {
    private AdminDAO adminDAO;

    public AdminProfileResource(AdminDAO adminDAO){
        this.adminDAO=adminDAO;
    }

    @Path("password")
    @PUT
    @UnitOfWork
    public void changePassword(@PathParam("id") int id, String password){
        if(adminDAO.setPassword(password,id))
        {
            System.out.println("pass changed");
        }else System.out.println("pass not changed");

    }

}
