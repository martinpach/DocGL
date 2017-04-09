package com.DocGL.DB;

import com.DocGL.api.Admin;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by D33 on 4/8/2017.
 */
public class AdminDAO extends AbstractDAO<Admin> {

    public AdminDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Admin> getAllAdmins(){
        return list(namedQuery("com.DocGL.api.getAllAdmins"));
    }


}
