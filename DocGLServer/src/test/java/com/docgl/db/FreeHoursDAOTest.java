package com.docgl.db;

import com.docgl.entities.Doctor;
import com.docgl.entities.FreeHours;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Martin on 10.5.2017.
 */
public class FreeHoursDAOTest extends AbstractDAO{
    private final FreeHoursDAO freeHoursDAO = new FreeHoursDAO(sessionFactory);
    private final DoctorDAO doctorDAO = new DoctorDAO(sessionFactory);

    @Test
    public void setDoctorsFreeHoursTest(){
        FreeHours freeHours = new FreeHours();
        Doctor doctor = doctorDAO.getDoctor(2);

        freeHours.setDate(new Date());
        freeHours.setDoctor(doctor);
        freeHours.setFrom("14:30");
        freeHours.setTo("15:15");
        freeHoursDAO.setDoctorsFreeHours(freeHours);
        assertEquals(1, doctor.getFreeHours().size());
    }
}
