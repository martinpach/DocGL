package com.DocGL.resources;

import com.DocGL.Authorizer;
import com.DocGL.DB.AdminDAO;
import com.DocGL.api.AdminInput;
import com.DocGL.api.LoggedUser;
import com.DocGL.entities.Admin;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
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

    public AdminProfileResource(AdminDAO adminDAO){
        this.adminDAO=adminDAO;
    }

    @Path("password")
    @PUT
    @UnitOfWork
    public void changePassword(@Auth Principal loggedUser, @PathParam("id") int id, Admin admin){
        if(new Authorizer().hasPermission(loggedUser.getName(), "admin")) {
            if (adminDAO.setPassword(admin.getPassword(), id)) {
                System.out.println("pass changed");
            } else System.out.println("pass not changed");
        }
    }

    @PUT
    @UnitOfWork
    public void updateProfile(@Auth Principal loggedUser, @PathParam("id") int id, AdminInput admin){
        if(new Authorizer().hasPermission(loggedUser.getName(), "admin")) {
            adminDAO.updateProfile(admin.getUsername(), admin.getPassword(), admin.getEmail(), id);
        }
        throw new NotAuthorizedException("Don't have permission!");
    }

    @GET
    @UnitOfWork
    public Admin getAdminProfile(@Auth Principal loggedUser, @PathParam("id") int id){
        Authorizer authorizer = new Authorizer();
        if(authorizer.hasPermission(loggedUser.getName(), "admin")){
            return adminDAO.getAdminInformation(id);
        }
        throw new NotAuthorizedException("Don't have permission!");
    }

}
