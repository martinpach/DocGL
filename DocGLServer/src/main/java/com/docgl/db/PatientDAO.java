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

    /**
     * This function provides formatted return of patients.
     * @param limit number of returned doctors
     * @param start first selected doctor
     * @param sortBy column that table will be sorted by
     * @param way sorted ascending or descending (asc,desc)
     * @param name searched patient name
     * @return list of patients filtered by entered params. If no params are presented, all patients will be returned
     */
    public List<Patient> getAllPatients(int limit, int start, SortablePatientColumns sortBy, SortingWays way, String name){
        Criteria criteria = criteria();
        if (name != null) {
            criteria = searchPatient(name);

        }
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
        System.out.println("!!!!!!!!!!!!!!!"+list(criteria).size());

        return list(criteria);
    }

    /**
     * This function is called during login. If credentials are incorrect function return null.
     * @param userName login username
     * @param password login password
     * @return Patient entity object with entered username and password
     */
    public Patient getLoggedPatientInformation(String userName, String password) {
        Criteria criteria = criteria()
                .add(Restrictions.eq("userName", userName))
                .add(Restrictions.eq("password", new Cryptor().encrypt(password)));
        return (Patient) criteria.uniqueResult();
    }

    /**
     * Save new patient to the database
     * @param registrationInput object with all registration values
     */
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

    /**
     * This function checked if selected userName and email are unique
     * @param userName to test if it is unique
     * @param email to test if it is unique
     * @return true if both params are unique
     */
    public boolean isUserNameAndEmailUnique(String userName, String email) {
        Criterion userNameCondition = Restrictions.eq("userName", userName);
        Criterion emailCondtition = Restrictions.eq("email", email);
        Criteria criteria = criteria()
                .add(Restrictions.or(userNameCondition, emailCondtition));
        Patient patient = (Patient) criteria.uniqueResult();
        return patient == null;
    }


    /**
     * @param date selected date
     * @return number of patient registration per selected date
     */
    public long getNumberOfRegistrations(Date date) {
        return (long) criteria()
                .add(Restrictions.eq("registrationDate", date))
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    /**
     * @param blocked true = block, false = unblock
     * @param id selected patient
     */
    public void blockPatient(boolean blocked, int id) {
        currentSession();
        Patient patient = currentSession().find(Patient.class, id);
        patient.setBlocked(blocked);
    }

    /**
     * @return number of all patients
     */
    public long getNumberOfAllPatients(){
        return (long)criteria()
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }
    public Criteria searchPatient(String name) {
        Criteria criteria = criteria();
            Criterion firstname = Restrictions.ilike("firstName", "%" + name + "%");
            Criterion lastname = Restrictions.ilike("lastName", "%" + name + "%");
            Criterion email = Restrictions.ilike("email", "%" + name + "%");
            criteria.add(Restrictions.or(firstname, lastname, email));
        return criteria;
    }
}
