package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.api.RegistrationInput;
import com.docgl.enums.SpecializationsEnum;
import com.docgl.exceptions.ValidationException;
import com.docgl.entities.Doctor;
import com.docgl.enums.SortableDoctorColumns;
import com.docgl.enums.SortingWays;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;

/**
 * Created by Rasťo on 15.4.2017.
 */
public class DoctorDAO extends AbstractDAO<Doctor> {
    public DoctorDAO(SessionFactory factory) {
        super(factory);
    }

    /**
     * This function provides formatted return of doctors.
     * @param limit number of returned doctors
     * @param start first selected doctor
     * @param sortBy column that table will be sorted by
     * @param way sorted ascending or descending (asc,desc)
     * @return list of doctors filtered by entered params. If no params are presented, all doctors will be returned
     */
    public List<Doctor> getAllDoctors(int limit, int start, SortableDoctorColumns sortBy, SortingWays way, String name, SpecializationsEnum spec) {
        Criteria criteria = criteria();
        if (name != null || spec != null)
            searchDoctor(name, spec);
        else
            throw new ValidationException("Url must contain of parameter name or spec");

        if (limit > 0 && start >= 0) {
            criteria.setFirstResult(start)
                    .setMaxResults(limit);
        }
        if (sortBy != null) {
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

    /**
     * @return all likes
     */
    public long getNumberOfOverallLikes() {
        return (long) criteria()
                .setProjection(Projections.sum("likes")).uniqueResult();
    }

    /**
     * @param date selected date
     * @return number of doctor registration per selected date
     */
    public long getNumberOfRegistrations(Date date) {
        return (long) criteria()
                .add(Restrictions.eq("registrationDate", date))
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    /**
     * @param blocked true = block, false = unblock
     * @param id selected doctor
     */
    public void blockDoctor(boolean blocked, int id) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        doctor.setBlocked(blocked);
    }

    /**
     * By default doctor is not approved. Admin has to approve him to use application.
     * @param id selected doctor to be approved
     */
    public void approveDoctor(int id) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        if (doctor.isApproved() == true) {
            throw new BadRequestException("Doctor is already approved!");
        }
        doctor.setApproved(true);
    }

    public List<Doctor> searchDoctorByName(String name, String spec) {
        Criteria criteria = criteria();
        if (name != null || spec != null) {
            if (name != null) {
                Criterion firstname = Restrictions.ilike("firstName", "%" + name + "%");
                Criterion lastname = Restrictions.ilike("lastName", "%" + name + "%");
                criteria.add(Restrictions.or(firstname, lastname));
            }
            if (spec != null) {
                if (spec.equals("DENTIST") || spec.equals("CARDIOLOGIST") || spec.equals("ORTHOPEDIST")) {
                    criteria.add(Restrictions.eq("specialization", SpecializationsEnum.valueOf(spec)));
                } else if (spec.equals("")) {

                } else
                    throw new ValidationException("Invalid specialization. Choose between: DENTIST, CARDIOLOGIST or ORTHOPEDIST");
            }
        } else
            throw new ValidationException("Search must contain of parameter name or spec");

        return list(criteria);
    }

    public List<Doctor> searchDoctor(String name, SpecializationsEnum spec) {
        Criteria criteria = criteria();
        if (name != null) {
            Criterion firstname = Restrictions.ilike("firstName", "%" + name + "%");
            Criterion lastname = Restrictions.ilike("lastName", "%" + name + "%");
            criteria.add(Restrictions.or(firstname, lastname));
        }
        if (spec != null) {
            criteria.add(Restrictions.eq("specialization", spec));
        }
        return list(criteria);
    }

    /**
     * @return number of all doctors
     */
    public long getNumberOfAllDoctors(){
        return (long)criteria()
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    /**
     * This function is called during login. If credentials are incorrect function return null.
     * @param username login username
     * @param password login password
     * @return Doctor entity object with entered username and password
     */
    public Doctor getLoggedDoctorInformation(String username, String password) {
        Criteria criteria = criteria()
                .add(Restrictions.eq("userName", username))
                .add(Restrictions.eq("password", Cryptor.encrypt(password)));
        return (Doctor) criteria.uniqueResult();
    }

    /**
     * This function checked if selected userName and email are unique
     */
    public boolean isUserNameAndEmailUnique(String userName, String email) {
        Criterion userNameCondition = Restrictions.eq("userName", userName);
        Criterion emailCondtition = Restrictions.eq("email", email);
        Criteria criteria = criteria()
                .add(Restrictions.or(userNameCondition, emailCondtition));
        Doctor doctor = (Doctor) criteria.uniqueResult();
        return doctor == null;
    }

    /**
     * Save new doctor to the database
     * @param registrationInput object with all registration values
     */
    public void registerDoctor(RegistrationInput registrationInput) {
        currentSession().save(new Doctor(
            registrationInput.getFirstName(),
            registrationInput.getLastName(),
            registrationInput.getEmail(),
            registrationInput.getUserName(),
            registrationInput.getSpecialization(),
            new Cryptor().encrypt(registrationInput.getPassword()),
            new Date()
            ));
    }
}

