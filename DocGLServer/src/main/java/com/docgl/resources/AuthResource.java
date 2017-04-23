package com.docgl.resources;

import com.docgl.db.AdminDAO;
import com.docgl.api.AdminRepresentation;
import com.docgl.api.LoginInput;
import com.docgl.entities.Admin;
import io.dropwizard.hibernate.UnitOfWork;
import jersey.repackaged.com.google.common.base.Throwables;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

/**
 * Created by Martin on 11.4.2017.
 */

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private AdminDAO adminDAO;
    //private DoctorDAO doctorDAO;
    private byte[] tokenSecret;

    public AuthResource(AdminDAO adminDAO, byte[] tokenSecret) {
        this.adminDAO = adminDAO;
        this.tokenSecret=tokenSecret;
    }

    @POST
    @UnitOfWork
    @Path("login")
    public Object login(LoginInput credentials){
        String username=credentials.getUsername();
        String password=credentials.getPassword();
        if(username == null || username.trim().isEmpty()){
            throw new BadRequestException("Property 'username' is missing or not presented!");
        }
        if(password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Property 'password' is missing or not presented!");
        }

        Admin adminInfo = adminDAO.getLoggedAdminInformation(username, password);
        if(adminInfo != null){
            return new AdminRepresentation(adminInfo, generateValidToken("admin", adminInfo.getId()));
        }

        /*Doctor doctorInfo = doctorDAO.getDoctorInformation(username, password);
        if(doctorInfo != null){
            return new DoctorRepresentation(doctorInfo, generateValidToken("doctor", doctorInfo.getId()));
        }

        User userInfo = userDAO.getUserInformation(username, password);
        if(patientInfo != null){
            return new UserRepresentation(userInfo, generateValidToken("patient", userInfo.getIdpatient()));
        }*/

        throw new NotAuthorizedException("Invalid credentials!");
    }

    @POST
    @PermitAll
    @UnitOfWork
    @Path("logout")
    public Response logout() {
        return Response.noContent().build();
    }


    private String generateValidToken(String role, int id) {
        final JwtClaims claims = new JwtClaims();
        claims.setSubject(role);
        claims.setClaim("id", id);
        claims.setGeneratedJwtId();
        claims.setExpirationTimeMinutesInTheFuture(30);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));
        jws.setDoKeyValidation(false);//relaxes key length validation. might be removed later

        try {
            return jws.getCompactSerialization();
        }
        catch (JoseException e) { throw Throwables.propagate(e); }
    }
}
