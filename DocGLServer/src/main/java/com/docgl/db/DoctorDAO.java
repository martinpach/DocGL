package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.api.RegistrationInput;
import com.docgl.enums.SpecializationsEnum;
import com.docgl.exceptions.ValidationException;
import com.docgl.entities.Doctor;
import com.docgl.enums.SortableDoctorColumns;
import com.docgl.enums.SortingWays;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.commons.lang3.StringUtils;
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
     * @param name name to search
     * @param spec specialization to search
     * @return list of doctors filtered by entered params. If no params are presented, all doctors will be returned
     */
    public List<Doctor> getAllDoctors(int limit, int start, SortableDoctorColumns sortBy, SortingWays way, String name, SpecializationsEnum spec) {
        Criteria criteria = criteria();
        if (name != null || spec != null)
            criteria = searchDoctor(name, spec);
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

    /**
     * This function search doctors by theirs name and specialization.
     * @param name name to search
     * @param spec specialization to search
     * @return Criteria
     */
    public Criteria searchDoctor(String name, SpecializationsEnum spec) {
        Criteria criteria = criteria();
        if (name != null) {
            Criterion firstname = Restrictions.ilike("firstName", "%" + name + "%");
            Criterion lastname = Restrictions.ilike("lastName", "%" + name + "%");
            Criterion email = Restrictions.ilike("email", "%" + name + "%");
            criteria.add(Restrictions.or(firstname, lastname, email));
        }
        if (spec != null) {
            criteria.add(Restrictions.eq("specialization", spec));
        }
        return criteria;
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
     * @param userName to test if it is unique
     * @param email to test if it is unique
     * @return true if both params are unique
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
                registrationInput.getPhone(),
                Cryptor.encrypt(registrationInput.getPassword()),
                new Date(),
                registrationInput.getCity(),
                registrationInput.getWorkplace()
        ));
    }
    /**
     * This function sets doctors password and encrypt it.
     * @param password new password
     * @param id doctor id in database
     */
    public void setPassword(String password, int id) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        doctor.setPassword(Cryptor.encrypt(password));
    }
    /**
     * This function compare the new password with the old one.
     * @param password new password
     * @param id doctor id in database
     * @return returns true if password are different, false if are not
     */
    public boolean isPasswordDifferent(String password, int id) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        if (Cryptor.encrypt(password).equals(doctor.getPassword()))
            return false;
        return true;
    }

    /**
     * Returns chosen doctor
     * @param id chosen doctor
     * @return chosen doctor
     */
    public Doctor getDoctor(int id){
        return (Doctor) criteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    /**
     * This function set doctors appointments duration.
     * @param minutes appointments duration in minutes
     * @param id doctor id in database
     */
    public void setAppointmentsDuration(int minutes, int id) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        doctor.setAppointmentsDuration(minutes);
    }

    /**
     * This function sets date of validity for doctor.
     * @param id chosen doctor
     * @param date chosen date
     */
    public void setDoctorsValidity(int id, Date date){
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        doctor.setDateOfValidity(date);
    }

    /**
     * This function gives one like to doctor.
     * @param id doctors id
     */
    public void addLikeToDoctor(int id) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        doctor.setLikes(doctor.getLikes()+1);
    }
    /**
     * This function remove one like from doctor.
     * @param id doctors id
     */
    public void removeLikeFromDoctor(int id) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        doctor.setLikes(doctor.getLikes()-1);
    }

    /**
     * This function marks that doctor already sets his working hours
     * @param id chosen doctor
     */
    public void markDoctorSetWorkingHours(int id){
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        doctor.setWorkingHoursSet(true);
    }

    /**
     * This function updates doctors' profile in database. If aparam is empty the field will not be updated.
     * @param firstName to update
     * @param lastName to update
     * @param email to update
     * @param password to update
     * @param phone to update
     * @param id chosen doctor
     */
    public void updateProfile(String firstName, String lastName, String email, String password, String phone, int id){
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, id);
        if(StringUtils.isNotBlank(firstName)){
            doctor.setFirstName(firstName);
        }
        if(StringUtils.isNotBlank(lastName)) {
            doctor.setLastName(lastName);
        }
        if(StringUtils.isNotBlank(password)) {
            doctor.setPassword(Cryptor.encrypt(password));
        }
        if(StringUtils.isNotBlank(email)) {
            doctor.setEmail(email);
        }
        if(StringUtils.isNotBlank(phone)) {
            doctor.setPhone(phone);
        }
    }
}

