package com.docgl.db;

import com.docgl.entities.WorkingHours;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Martin on 7.5.2017.
 */
public class WorkingHoursDAOTest extends AbstractDAO {
    private final WorkingHoursDAO dao = new WorkingHoursDAO(sessionFactory);

    @Test
    public void getDoctorsWorkingHoursTest(){
        List<WorkingHours> workingHours = dao.getDoctorsWorkingHours(1);
        assertEquals(2, workingHours.size());
    }
}
