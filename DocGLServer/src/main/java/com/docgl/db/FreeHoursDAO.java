package com.docgl.db;

import com.docgl.entities.FreeHours;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Martin on 10.5.2017.
 */
public class FreeHoursDAO extends io.dropwizard.hibernate.AbstractDAO<FreeHours> {
    public FreeHoursDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * This function sets free hours in database to chosen doctor
     * @param freeHours input with chosen free hours
     */
    public void setDoctorsFreeHours(FreeHours freeHours){
        currentSession().save(freeHours);
    }

    /**
     * This function returns all doctors' free hours from database
     * @param id chosen doctor
     * @return chosen doctor free hours
     */
    public List<FreeHours> getDoctorsFreeHours(int id){
        return namedQuery("getDoctorsFreeHours")
                .setParameter("id", id)
                .list();
    }
}
