package com.docgl.resources;

import com.docgl.db.DoctorDAO;
import com.docgl.db.PatientDAO;
import com.docgl.enums.TimePeriod;
import com.docgl.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Martin on 29.4.2017.
 */
@Path("/registrations")
@Produces(MediaType.APPLICATION_JSON)
public class RegistrationResource {

    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;

    public RegistrationResource(DoctorDAO doctorDAO, PatientDAO patientDAO) {
        this.doctorDAO = doctorDAO;
        this.patientDAO = patientDAO;
    }

    @GET
    @UnitOfWork
    public List<RegistrationCountRepresentation> getNumberOfRegistration(@QueryParam("timePeriod") TimePeriod timePeriod){
        List<RegistrationCountRepresentation> registrations = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString;
        Date date;

        if(timePeriod == null){
            throw new ValidationException("Missing 'timePeriod' query param.");
        }

        if(timePeriod.equals(TimePeriod.WEEK)) {
            for(int i=0; i<7; i++){
                Calendar cal = GregorianCalendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -i);
                date = cal.getTime();
                dateString = sdf.format(date);
                RegistrationCountRepresentation registrationCountRepresentation =
                        new RegistrationCountRepresentation(dateString, doctorDAO.getNumberOfRegistrations(date) +
                        patientDAO.getNumberOfRegistrations(date));
                registrations.add(registrationCountRepresentation);
            }
        }
        else{
            date = new Date();
            dateString = sdf.format(date);
            registrations.add(new RegistrationCountRepresentation(dateString, doctorDAO.getNumberOfRegistrations(date) +
            patientDAO.getNumberOfRegistrations(date)));
        }
        return registrations;
    }

    private class RegistrationCountRepresentation{
        @JsonProperty
        private String date;
        @JsonProperty
        private long count;

        public RegistrationCountRepresentation(String date, long count) {
            this.date = date;
            this.count = count;
        }
    }
}