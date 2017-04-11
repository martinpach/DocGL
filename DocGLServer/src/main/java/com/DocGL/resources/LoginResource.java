package com.DocGL.resources;

import com.DocGL.DB.AdminDAO;
import com.DocGL.api.Admin;
import com.DocGL.api.Credentials;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Martin on 11.4.2017.
 */

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
    private AdminDAO adminDAO;

    public LoginResource(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @POST
    @UnitOfWork
    public Admin logInAdmin(Credentials credentials){
        String username=credentials.getUsername();
        String password=credentials.getPassword();
        return adminDAO.getAdminInformation(username,password);
    }
}
