package com.docgl.db;

import com.docgl.entities.WorkingHours;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

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
        return list(criteria()
                .add(Restrictions.eq("doctorId", id)));
    }

    /**
     * Setting working hours of doctor
     * @param workingHours working hours for whole week
     */
    public void setDoctorsWorkingHours(WorkingHours workingHours){
        currentSession().save(workingHours);
    }
}
