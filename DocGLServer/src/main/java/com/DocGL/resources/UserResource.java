package com.docgl.resources;

import com.docgl.Authorizer;
import com.docgl.db.UserDAO;
import com.docgl.entities.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
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
    private Authorizer authorizer;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authorizer = new Authorizer();
    }

    @GET
    @UnitOfWork
    public List<User> getListOfAllUsers(@Auth Principal loggedUser) {
        String[] roles = {"admin", "doctor"};
        authorizer.checkAuthorization(loggedUser.getName(), roles);
        return userDAO.getAllUsers();
    }
}
