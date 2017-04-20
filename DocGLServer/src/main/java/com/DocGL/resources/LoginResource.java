package com.DocGL.resources;

import com.DocGL.DB.AdminDAO;
import com.DocGL.api.AdminRepresentation;
import com.DocGL.api.LoginInput;
import com.DocGL.entities.Admin;
import io.dropwizard.hibernate.UnitOfWork;
import jersey.repackaged.com.google.common.base.Throwables;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

/**
 * Created by Martin on 11.4.2017.
 */

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
    private AdminDAO adminDAO;
    private byte[] tokenSecret;

    public LoginResource(AdminDAO adminDAO, byte[] tokenSecret) {
        this.adminDAO = adminDAO;
        this.tokenSecret=tokenSecret;
    }

    @POST
    @UnitOfWork
    public AdminRepresentation logInAdmin(LoginInput credentials){
        String username=credentials.getUsername();
        String password=credentials.getPassword();
        Admin adminInfo = adminDAO.getAdminInformation(username, password);
        if(username == null || username.trim().isEmpty()){
            throw new BadRequestException("Property 'username' is missing or not presented!");
        }
        if(password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Property 'password' is missing or not presented!");
        }

        if(adminInfo != null) {
            return new AdminRepresentation(adminInfo, generateValidToken("admin", adminInfo.getIdadmin()));
        }
        throw new NotAuthorizedException("Invalid credentials!");
    }


    public String generateValidToken(String role, int id) {
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
