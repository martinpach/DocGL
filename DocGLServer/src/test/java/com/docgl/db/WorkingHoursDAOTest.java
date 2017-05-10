package com.docgl.db;

import com.docgl.entities.WorkingHours;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    /**
     * getDoctorsWorkingDays test
     */
    @Test
    public void getDoctorsWorkingDaysTest() {
        assertNull( dao.getDoctorsWorkingDays(3));
    }
    @Test
    public void getDoctorsWorkingDaysTest2() {
        List<String> workingDays =dao.getDoctorsWorkingDays(1);
        assertEquals(5, workingDays.size());
        assertEquals("Monday", workingDays.get(0));
        assertEquals("Tuesday", workingDays.get(1));
        assertEquals("Wednesday", workingDays.get(2));
        assertEquals("Thursday", workingDays.get(3));
        assertEquals("Friday", workingDays.get(4));
    }
    @Test
    public void getDoctorsWorkingDaysTest3() {
        List<String> workingDays =dao.getDoctorsWorkingDays(2);
        assertEquals(4, workingDays.size());
        assertEquals("Monday", workingDays.get(0));
        assertEquals("Tuesday", workingDays.get(1));
        assertEquals("Wednesday", workingDays.get(2));
        assertEquals("Thursday", workingDays.get(3));
    }

}
