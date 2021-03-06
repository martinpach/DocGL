package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.api.UserInput;
import com.docgl.enums.UserType;
import com.docgl.api.LoggedUser;
import com.docgl.api.PasswordInput;
import com.docgl.db.AdminDAO;
import com.docgl.entities.Admin;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by wdfeww on 4/15/17.
 * Java class for Resources that are related with Admin.
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

    /**
     * Resource for changing admin password
     * @param loggedUser is logged user that is sending request
     * @param id chosen admin
     * @param passwordInput new password
     */
    @Path("password")
    @PUT
    @UnitOfWork
    public void changePassword(@Auth LoggedUser loggedUser, @PathParam("id") int id, PasswordInput passwordInput){
        if(StringUtils.isBlank(passwordInput.getPassword())){
            throw new BadRequestException("Property 'password' is missing or not presented!");
        }
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        adminDAO.setPassword(passwordInput.getPassword(), id);
    }

    /**
     * Resource for updating admin profile
     * @param loggedUser is logged user that is sending request
     * @param id chosen admin
     * @param admin AdminInput object with values to update
     * @return updated Admin object
     */
    @PUT
    @UnitOfWork
    public Admin updateProfile(@Auth LoggedUser loggedUser, @PathParam("id") int id, UserInput admin){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        adminDAO.updateProfile(admin.getFirstName(), admin.getLastName(), admin.getPassword(), admin.getEmail(), id);
        return adminDAO.getLoggedAdminInformation(1);
    }

    /**
     * Resource for getting selected admin profile
     * @param loggedUser is logged user that is sending request
     * @param id chosen admin
     * @return chosen admin
     */
    @GET
    @UnitOfWork
    public Admin getAdminProfile(@Auth LoggedUser loggedUser, @PathParam("id") int id){
        authorizer.checkAuthorization(loggedUser.getUserType(), UserType.ADMIN);
        authorizer.checkAuthentication(loggedUser.getId(), id);
        return adminDAO.getLoggedAdminInformation(id);
    }

}
