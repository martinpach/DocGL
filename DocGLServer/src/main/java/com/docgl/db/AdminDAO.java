package com.docgl.db;

import com.docgl.entities.Admin;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


/**
 * Created by D33 on 4/8/2017.
 */
public class AdminDAO extends AbstractDAO<Admin> {

    public AdminDAO(SessionFactory factory) {
        super(factory);
    }


    public Admin getAdminInformation(String username, String password){
        List<Admin> admin = list(namedQuery("getAdminInformation")
                .setParameter("username",username)
                .setParameter("password", password));
        if(admin.isEmpty()){
            return null;
        }
        return admin.get(0);
    }

    public void setPassword(String password, int id){
        try {
            namedQuery("setPassword")
                    .setParameter("password", password)
                    .setParameter("id", id)
                    .executeUpdate();
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void updateProfile(String userName, String password, String email, int id){
        try {
            namedQuery("setProfile")
                    .setParameter("username", userName)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .setParameter("id", id)
                    .executeUpdate();
        }catch(Exception ex) {
            System.out.println(ex);
        }
    }

    public Admin getAdminInformation(int id){
        return (Admin) namedQuery("getAdminInformationById")
                .setParameter("id", id)
                .getSingleResult();
    }


}
