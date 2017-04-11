package com.DocGL.resources;

import com.DocGL.DB.AdminDAO;
import com.DocGL.api.Admin;
import com.DocGL.api.Credentials;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.hibernate.UnitOfWork;
import jersey.repackaged.com.google.common.base.Throwables;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * Created by D33 on 4/8/2017.
 */

@Path("/admins")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {
    private AdminDAO adminDAO;

    public AdminResource(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @GET
    @UnitOfWork
    public List getAllAdmins(){
        return adminDAO.getAllAdmins();
    }

}


