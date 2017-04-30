package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.exceptions.ValidationException;
import com.docgl.api.RegistrationInput;
import com.docgl.entities.Patient;
import com.docgl.enums.SortablePatientColumns;
import com.docgl.enums.SortingWays;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by Rasťo on 22.4.2017.
 */
public class PatientDAO extends AbstractDAO<Patient> {
    public PatientDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Patient> getAllPatients(int limit, int start, SortablePatientColumns sortBy, SortingWays way){
        Criteria criteria = criteria();
        if(limit > 0 && start >= 0) {
            criteria.setFirstResult(start)
                    .setMaxResults(limit);
        }
        if(sortBy != null) {
            if (way != null) {
                if (way.getValue().equals("asc")) {
                    criteria.addOrder(Order.asc(sortBy.getValue()));
                } else if (way.getValue().equals("desc")) {
                    criteria.addOrder(Order.desc(sortBy.getValue()));
                }
            } else {
                throw new ValidationException("In case of sorting use 'way' query parameter with sortBy parameter");
            }
        }
        return list(criteria);
    }

    public Patient getLoggedPatientInformation(String userName, String password) {
        Criteria criteria = criteria()
                .add(Restrictions.eq("userName", userName))
                .add(Restrictions.eq("password", new Cryptor().encrypt(password)));
        return (Patient) criteria.uniqueResult();
    }

    public void registerPatient(RegistrationInput registrationInput) {
        currentSession().save(new Patient(
                registrationInput.getFirstName(),
                registrationInput.getLastName(),
                registrationInput.getEmail(),
                registrationInput.getUserName(),
                new Cryptor().encrypt(registrationInput.getPassword()),
                new Date()
                ));
    }

    public boolean isUserNameAndEmailUnique(String userName, String email) {
        Criterion userNameCondition = Restrictions.eq("userName", userName);
        Criterion emailCondtition = Restrictions.eq("email", email);
        Criteria criteria = criteria()
                .add(Restrictions.or(userNameCondition, emailCondtition));
        Patient patient = (Patient) criteria.uniqueResult();
        return patient == null;
    }

    public long getNumberOfRegistrations(Date date) {
        return (long) criteria()
                .add(Restrictions.eq("registrationDate", date))
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    public void blockPatient(boolean blocked, int id) {
        currentSession();
        Patient patient = currentSession().find(Patient.class, id);
        patient.setBlocked(blocked);
    }
}
