package com.docgl.db;

import com.docgl.entities.PublicHolidays;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by Rasťo on 10.5.2017.
 */
public class PublicHolidaysDAO extends io.dropwizard.hibernate.AbstractDAO<PublicHolidays> {

    public PublicHolidaysDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Getting all public holidays.
     * @return List of Public Holidays
     */
    public List<PublicHolidays> getPublicHolidays() {
        return namedQuery("getAllPublicHolidays").list();
    }

    public boolean isDatePublicHoliday(Date date) {
        List<PublicHolidays> publicHolidaysList = namedQuery("getAllPublicHolidays").list();
        for (PublicHolidays ph:publicHolidaysList) {
            if (date.compareTo(ph.getDate()) == 0)
                return true;
        }
        return false;
    }
}
