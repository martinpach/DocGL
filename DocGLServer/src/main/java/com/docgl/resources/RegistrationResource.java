package com.docgl.resources;

import com.docgl.db.DoctorDAO;
import com.docgl.db.PatientDAO;
import com.docgl.enums.TimePeriod;
import com.docgl.enums.UserTypes;
import com.docgl.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 29.4.2017.
 * Java class for Resources that are related with registrations.
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

    /**
     * Resource for getting number of registrations.
     * @param timePeriod how many days to return (WEEK/TODAY)
     * @param userTypes type of users (DOCTORS/PATIENTS)
     * @return array of objects with date and number of registrations
     */
    @GET
    @UnitOfWork
    @PermitAll
    public List<RegistrationCountRepresentation> getNumberOfRegistration(@QueryParam("timePeriod") TimePeriod timePeriod, @QueryParam("userTypes") UserTypes userTypes){
        List<RegistrationCountRepresentation> registrations = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString;
        Date date;

        if (timePeriod == null){
            throw new ValidationException("Missing 'timePeriod' query param.");
        }
        if (userTypes == null){
            throw new ValidationException("Missing 'userTypes' query param.");
        }

        if(timePeriod.equals(TimePeriod.WEEK)) {
            RegistrationCountRepresentation registrationCountRepresentation = null;
            for(int i=0; i<7; i++){
                Calendar cal = GregorianCalendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -i);
                date = cal.getTime();
                dateString = sdf.format(date);
                if (userTypes == UserTypes.DOCTORS)
                    registrationCountRepresentation = new RegistrationCountRepresentation(dateString, doctorDAO.getNumberOfRegistrations(date));
                else
                    registrationCountRepresentation = new RegistrationCountRepresentation(dateString, patientDAO.getNumberOfRegistrations(date));
                registrations.add(registrationCountRepresentation);
            }
        }
        else{
            date = new Date();
            dateString = sdf.format(date);
            if (userTypes == UserTypes.DOCTORS)
                registrations.add(new RegistrationCountRepresentation(dateString, doctorDAO.getNumberOfRegistrations(date)));
            else
                registrations.add(new RegistrationCountRepresentation(dateString, patientDAO.getNumberOfRegistrations(date)));
        }
        return registrations;
    }

    private class RegistrationCountRepresentation{
        @JsonProperty
        private String date;
        @JsonProperty
        private long count;

        private RegistrationCountRepresentation(String date, long count) {
            this.date = date;
            this.count = count;
        }

        public String getDate() {
            return date;
        }

        public long getCount() {
            return count;
        }
    }
}
