package com.DocGL.resources;

import com.DocGL.Authorizer;
import com.DocGL.DB.UserDAO;
import com.DocGL.entities.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;

/**
 * Created by Rasťo on 22.4.2017.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)

public class UserResource {
    private UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @UnitOfWork
    public List<User> getListOfAllUsers(@Auth Principal loggedUser) {
        Authorizer authorizer = new Authorizer();
        if(authorizer.hasPermission(loggedUser.getName(), "admin") ||
                authorizer.hasPermission(loggedUser.getName(), "doctor")) {
            return userDAO.getAllUsers();
        }
        throw new NotAuthorizedException("Don't have permission!");
    }
}
