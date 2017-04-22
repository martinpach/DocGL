package com.docgl.resources;

import com.docgl.db.UserDAO;
import com.docgl.entities.User;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    public List<User> getListOfAllUsers() {
        return userDAO.getAllUsers();
    }
}
