package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.enums.UserType;
import com.docgl.api.LoggedUser;
import com.docgl.api.PasswordInput;
import com.docgl.db.AdminDAO;
import com.docgl.api.AdminInput;
import com.docgl.entities.Admin;
import io.dropwizard.auth.Auth;
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
    private Authorizer authorizer;

    public AdminProfileResource(AdminDAO adminDAO){
        this.adminDAO=adminDAO;
        this.authorizer=new Authorizer();
    }

    @Path("password")
    @PUT
    @UnitOfWork
    public void changePassword(@Auth LoggedUser loggedUser, @PathParam("id") int id, PasswordInput passwordInput){
        if(passwordInput.getPassword() == null || passwordInput.getPassword().isEmpty()){
            throw new BadRequestException("Property 'password' is missing or not presented!");
        }
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        adminDAO.setPassword(passwordInput.getPassword(), id);
    }

    @PUT
    @UnitOfWork
    public void updateProfile(@Auth LoggedUser loggedUser, @PathParam("id") int id, AdminInput admin){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        adminDAO.updateProfile(admin.getUserName(), admin.getPassword(), admin.getEmail(), id);
    }

    @GET
    @UnitOfWork
    public Admin getAdminProfile(@Auth LoggedUser loggedUser, @PathParam("id") int id){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        return adminDAO.getLoggedAdminInformation(id);
    }

}
