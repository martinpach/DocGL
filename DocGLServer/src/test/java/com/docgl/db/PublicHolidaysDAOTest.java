package com.docgl.db;

import com.docgl.entities.PublicHolidays;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void isDatePublicHoliday() {
        assertTrue(dao.isDatePublicHoliday(parseStringToUtilDate("2017-07-05")));
    }
    @Test
    public void isDatePublicHoliday2() {
        assertFalse(dao.isDatePublicHoliday(parseStringToUtilDate("2017-07-06")));
    }

    private Date parseStringToUtilDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException ex){
        }
        return date;
    }
}