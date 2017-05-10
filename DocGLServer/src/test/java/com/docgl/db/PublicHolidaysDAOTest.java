package com.docgl.db;

import com.docgl.entities.PublicHolidays;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rasťo on 10.5.2017.
 */
public class PublicHolidaysDAOTest extends AbstractDAO {
    private final PublicHolidaysDAO dao = new PublicHolidaysDAO(sessionFactory);

    @Test
    public void getPublicHolidaysTest() {
        List<PublicHolidays> publicHolidays = dao.getPublicHolidays();
        assertEquals(new LocalDate(2017,1,1), new LocalDate(publicHolidays.get(0).getDate()));
        assertEquals(new LocalDate(2017,1,6), new LocalDate(publicHolidays.get(1).getDate()));
        assertEquals(new LocalDate(2017,5,8), new LocalDate(publicHolidays.get(5).getDate()));
        assertEquals(15, publicHolidays.size());
    }
}
