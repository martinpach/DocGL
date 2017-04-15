package com.DocGL.DB;

import com.DocGL.api.Admin;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Session session;

    public AdminDAO(SessionFactory factory) {
        super(factory);
        session=factory.openSession();
    }

    public List<Admin> getAllAdmins(){
        return list(namedQuery("com.DocGL.api.getAllAdmins"));
    }


    public Admin getAdminInformation(String username, String password){
        Query query= session.getNamedQuery("com.DocGL.api.getAdminInformation");
        query.setString("username",username);
        query.setString("password", password);
        if(query.list().isEmpty()){
            return null;
        }
        return (Admin) query.getSingleResult() ;
    }


}
