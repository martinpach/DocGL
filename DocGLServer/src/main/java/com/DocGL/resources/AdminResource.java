package com.DocGL.resources;

import com.DocGL.DB.AdminDAO;

import io.dropwizard.hibernate.UnitOfWork;
import jersey.repackaged.com.google.common.base.Throwables;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

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
    private byte[] tokenSecret;

    public AdminResource(AdminDAO adminDAO, byte[] tokenSecret) {
        this.adminDAO = adminDAO;
        this.tokenSecret=tokenSecret;
    }

    @GET
    @UnitOfWork
    public List getAllAdmins(){
        return adminDAO.getAllAdmins();
    }

   /* @GET
    @Path("/generate-valid-token")
    public Map<String, String> generateValidToken() {
        final JwtClaims claims = new JwtClaims();
        claims.setSubject("admin");
        claims.setGeneratedJwtId();
        claims.setExpirationTimeMinutesInTheFuture(30);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));
        jws.setDoKeyValidation(false);//relaxes key length validation. might be removed later

        try {
            return singletonMap("token", jws.getCompactSerialization());
        }
        catch (JoseException e) { throw Throwables.propagate(e); }
    } */

}


