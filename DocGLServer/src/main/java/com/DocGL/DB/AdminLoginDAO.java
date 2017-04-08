package com.DocGL.DB;

import com.DocGL.api.Admin;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;



/**
 * Created by D33 on 4/8/2017.
 */
public class AdminLoginDAO extends AbstractDAO<Admin> {

    public AdminLoginDAO(SessionFactory factory) {

        super(factory);
    }


}
