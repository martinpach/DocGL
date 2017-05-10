package com.docgl.resources;

import com.docgl.api.*;
import com.docgl.entities.Doctor;
import com.docgl.enums.UserType;
import com.docgl.exceptions.ValidationException;
import com.docgl.db.AdminDAO;
import com.docgl.db.DoctorDAO;
import com.docgl.db.PatientDAO;
import com.docgl.entities.Admin;
import com.docgl.entities.Patient;
import io.dropwizard.hibernate.UnitOfWork;
import jersey.repackaged.com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
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
    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;
    private byte[] tokenSecret;

    public AuthResource(AdminDAO adminDAO, PatientDAO patientDAO, DoctorDAO doctorDAO, byte[] tokenSecret) {
        this.adminDAO = adminDAO;
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
        this.tokenSecret=tokenSecret;
    }

    /**
     * Resource for log in.
     * @param credentials LoginInput object with user type and credentials
     * @return If credentials are correct, token and selected user type object are returned. If doctor, patient are not approved or are blocked token will be empty
     */
    @POST
    @UnitOfWork
    @Path("login")
    public Object login(LoginInput credentials){
        String username = credentials.getUserName();
        String password = credentials.getPassword();
        UserType userType = credentials.getUserType();

        if(username == null || username.trim().isEmpty())
            throw new BadRequestException("Property 'username' is missing or not presented!");
        if(password == null || password.trim().isEmpty())
            throw new BadRequestException("Property 'password' is missing or not presented!");
        if(userType == null)
            throw new BadRequestException("Property 'userType' is missing or not presented!");

        if (userType.equals(UserType.ADMIN)) {
            Admin adminInfo = adminDAO.getLoggedAdminInformation(username, password);
            if(adminInfo != null)
                return new AdminRepresentation(adminInfo, generateValidToken("admin", adminInfo.getId()));
        }
        if (userType.equals(UserType.DOCTOR)) {
            Doctor doctorInfo = doctorDAO.getLoggedDoctorInformation(username, password);
            if(doctorInfo != null){
                String token = (!doctorInfo.isApproved() || doctorInfo.isBlocked()) ? "" : generateValidToken("doctor", doctorInfo.getId());
                return new DoctorRepresentation(doctorInfo, token);
            }
        }
        if (userType.equals(UserType.PATIENT)) {
            Patient patientInfo = patientDAO.getLoggedPatientInformation(username, password);
            if(patientInfo != null){
                String token = patientInfo.isBlocked() ? "" : generateValidToken("patient", patientInfo.getId());
                return new PatientRepresentation(patientInfo, token);
            }
        }

        throw new NotAuthorizedException("Invalid credentials!");
    }

    @POST
    @PermitAll
    @UnitOfWork
    @Path("logout")
    public Response logout() {
        return Response.noContent().build();
    }


    /**
     * Resource for registering new user
     * @param registrationInput values for new user
     * @return If registration is successful the registered user and token are returned. In case of doctor no token is returned (because he is not approved by default)
     */
    @POST
    @UnitOfWork
    @Path("registration")
    public Object register(RegistrationInput registrationInput) {
        String userName = registrationInput.getUserName();
        String email = registrationInput.getEmail();
        UserType userType = registrationInput.getUserType();

        if(userType.equals(UserType.ADMIN))
            throw new ValidationException("Parameter userType should be 'DOCTOR' or 'PATIENT'");
        if(StringUtils.isBlank(registrationInput.getFirstName()))
            throw new BadRequestException("Property 'firstName' is missing or not presented!");
        if(StringUtils.isBlank(registrationInput.getLastName()))
            throw new BadRequestException("Property 'lastName' is missing or not presented!");
        if(StringUtils.isBlank(registrationInput.getEmail()))
            throw new BadRequestException("Property 'email' is missing or not presented!");
        if(StringUtils.isBlank(registrationInput.getUserName()))
            throw new BadRequestException("Property 'userName' is missing or not presented!");
        if(StringUtils.isBlank(registrationInput.getPassword()))
            throw new BadRequestException("Property 'password' is missing or not presented!");

        if(userType.equals(UserType.PATIENT)) {
            if (patientDAO.isUserNameAndEmailUnique(userName, email)) {
                patientDAO.registerPatient(registrationInput);
                Patient patientInfo = patientDAO.getLoggedPatientInformation(userName, registrationInput.getPassword());
                return new PatientRepresentation(patientInfo, generateValidToken("patient", patientInfo.getId()));
            }
            else
                throw new ValidationException("Username or email is taken");
        }
        if (userType.equals(UserType.DOCTOR)) {
            if (registrationInput.getSpecialization() == null)
                throw new BadRequestException("Property 'specialization' is missing or not presented!");
            if (StringUtils.isBlank(registrationInput.getPhone()))
                throw new BadRequestException("Property 'phone' is missing or not presented!");
            if (StringUtils.isBlank(registrationInput.getCity()))
                throw new BadRequestException("Property 'city' is missing or not presented!");
            if (StringUtils.isBlank(registrationInput.getWorkplace()))
                throw new BadRequestException("Property 'workplace' is missing or not presented!");
            if (doctorDAO.isUserNameAndEmailUnique(userName, email)) {
                doctorDAO.registerDoctor(registrationInput);
                Doctor doctorInfo = doctorDAO.getLoggedDoctorInformation(userName, registrationInput.getPassword());
                return new DoctorRepresentation(doctorInfo, "");
            }
            else
                throw new ValidationException("Username or email is taken");
        }
        return null;
    }

    /**
     * Generating valid token.
     * @param role is user type of logged user (admin, patient, doctor)
     * @param id logged user id
     * @return
     */
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
