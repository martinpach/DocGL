package com.docgl.db;

import com.docgl.entities.Doctor;
import com.docgl.entities.WorkingHours;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 7.5.2017.
 */
public class WorkingHoursDAO extends io.dropwizard.hibernate.AbstractDAO<WorkingHours> {

    public WorkingHoursDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Fetching in table working_hours by id
     * @param id chosen doctor_id
     * @return working hours for chosen doctor
     */
    public List<WorkingHours> getDoctorsWorkingHours(int id){
        return namedQuery("getDoctorsWorkingHours")
                .setParameter("id", id)
                .list();
    }

    /**
     * Setting working hours of doctor
     * @param workingHours working hours for whole week
     */
    public void setDoctorsWorkingHours(WorkingHours workingHours){
        currentSession().save(workingHours);
    }


    /**
     * Fill List of String with days that work.
     * @param doctorId chosen doctor
     * @return List of Strings
     */
    public List<String> getDoctorsWorkingDays(int doctorId) {
        Session session = currentSession();
        Doctor doctor = session.find(Doctor.class, doctorId);
        if (doctor.isWorkingHoursSet()) {
            List<String> workingDays = new ArrayList<String>();
            List<WorkingHours> workingHoursList = getDoctorsWorkingHours(doctorId);
            WorkingHours morningHours = workingHoursList.get(0);
            WorkingHours afternoonHours = workingHoursList.get(1);
            if (morningHours.getMondayFrom() != null || afternoonHours.getMondayFrom() != null)
                workingDays.add("Monday");
            if (morningHours.getThursdayFrom() != null || afternoonHours.getThursdayFrom() != null)
                workingDays.add("Tuesday");
            if (morningHours.getWednesdayFrom() != null || afternoonHours.getWednesdayFrom() != null)
                workingDays.add("Wednesday");
            if (morningHours.getThursdayFrom() != null || afternoonHours.getThursdayFrom() != null)
                workingDays.add("Thursday");
            if (morningHours.getFridayFrom() != null || afternoonHours.getFridayFrom() != null)
                workingDays.add("Friday");
            return workingDays;
        }
        return null;
    }
}
