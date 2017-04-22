package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.db.AdminDAO;
import com.docgl.api.AdminInput;
import com.docgl.entities.Admin;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;

/**
 * Created by wdfeww on 4/15/17.
 */


@Path("/admins/{id}/profile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminProfileResource {
    private AdminDAO adminDAO;
    private Authorizer authorizer;

    public AdminProfileResource(AdminDAO adminDAO){
        this.adminDAO=adminDAO;
        this.authorizer=new Authorizer();
    }

    @Path("password")
    @PUT
    @UnitOfWork
    public void changePassword(@Auth Principal loggedUser, @PathParam("id") int id, Admin admin){
        authorizer.checkAuthorization(loggedUser.getName(), "admin");
        adminDAO.setPassword(admin.getPassword(), id);
    }

    @PUT
    @UnitOfWork
    public void updateProfile(@Auth Principal loggedUser, @PathParam("id") int id, AdminInput admin){
        authorizer.checkAuthorization(loggedUser.getName(), "admin");
        adminDAO.updateProfile(admin.getUsername(), admin.getPassword(), admin.getEmail(), id);
    }

    @GET
    @UnitOfWork
    public Admin getAdminProfile(@Auth Principal loggedUser, @PathParam("id") int id){
        authorizer.checkAuthorization(loggedUser.getName(), "admin");
        return adminDAO.getAdminInformation(id);
    }

}
