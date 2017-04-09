package com.DocGL.resources;

import com.DocGL.DB.AdminDAO;
import com.DocGL.api.Admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by D33 on 4/8/2017.
 */

@Path("/admins")
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {
    private AdminDAO adminDAO;

    public AdminResource(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @GET
    public List<Admin> getAllAdmins(){
        return adminDAO.getAllAdmins();
    }
}
