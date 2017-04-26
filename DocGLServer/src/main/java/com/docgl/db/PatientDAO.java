package com.docgl.db;

import com.docgl.api.RegistrationInput;
import com.docgl.entities.Patient;
import com.docgl.entities.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Rasťo on 22.4.2017.
 */
public class PatientDAO extends AbstractDAO<Patient> {
    public PatientDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Patient> getAllPatients(){
        return list(namedQuery("getAllPatients"));
    }

    public Patient getLoggedPatientInformation(String userName, String password){
        Criteria criteria = criteria()
                .add(Restrictions.eq("userName", userName))
                .add(Restrictions.eq("password", password));
        return (Patient) criteria.uniqueResult();
    }

    public void registerPatient(RegistrationInput registrationInput){
        currentSession().save(new Patient(
                registrationInput.getFirstName(),
                registrationInput.getLastName(),
                registrationInput.getEmail(),
                registrationInput.getUserName(),
                registrationInput.getPassword()
                ));
    }

    public boolean isUserNameAndEmailUnique(String userName, String email){
        Criterion userNameCondition = Restrictions.eq("userName", userName);
        Criterion emailCondtition = Restrictions.eq("email", email);
        Criteria criteria = criteria()
                .add(Restrictions.or(userNameCondition, emailCondtition));
        Patient patient = (Patient) criteria.uniqueResult();
        return patient == null;
    }
}
