package com.docgl.resources;

import com.docgl.db.PublicHolidaysDAO;
import com.docgl.entities.PublicHolidays;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Rasťo on 10.5.2017.
 * Java class for Resources that are related to PublicHolidaysDAO.
 */

@Path("/holidays")
@Produces(MediaType.APPLICATION_JSON)
public class HolidaysResource {
    private PublicHolidaysDAO publicHolidaysDAO;

    public HolidaysResource(PublicHolidaysDAO publicHolidaysDAO) {
        this.publicHolidaysDAO = publicHolidaysDAO;
    }


    /**
     * Resource for getting all public holidays.
     * @return List of Public Holidays
     */
    @GET
    @Path("public")
    @UnitOfWork
    public List<PublicHolidays> getPublicHolidays() {
        return publicHolidaysDAO.getPublicHolidays();
    }

}
