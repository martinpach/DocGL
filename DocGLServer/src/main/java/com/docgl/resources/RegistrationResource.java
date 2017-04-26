package com.docgl.resources;

import com.docgl.UserType;
import com.docgl.ValidationException;
import com.docgl.api.RegistrationInput;
import com.docgl.db.DoctorDAO;
import com.docgl.db.PatientDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by Martin on 26.4.2017.
 */

@Path("/registration")
public class RegistrationResource {

    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;
    private byte[] tokenSecret;

    public RegistrationResource(DoctorDAO doctorDAO, PatientDAO patientDAO, byte[] tokenSecret) {
        this.doctorDAO = doctorDAO;
        this.patientDAO = patientDAO;
        this.tokenSecret = tokenSecret;
    }

    @POST
    @UnitOfWork
    public void registerPatient(RegistrationInput registrationInput){
        if(registrationInput.getUserType().equals(UserType.ADMIN)){
            throw new ValidationException("Parameter userType should be 'DOCTOR' or 'PATIENT'");
        }
        if(registrationInput.getUserType().equals(UserType.PATIENT)){
            if(patientDAO.isUserNameAndEmailUnique(registrationInput.getUserName(), registrationInput.getEmail()))
                patientDAO.registerPatient(registrationInput);
            else
                throw new ValidationException("Username or email is taken");
        }
    }
}
