package com.docgl.db;

import com.docgl.entities.Admin;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.List;


/**
 * Created by D33 on 4/8/2017.
 */
public class AdminDAO extends AbstractDAO<Admin> {

    public AdminDAO(SessionFactory factory) {
        super(factory);
    }


    public Admin getLoggedAdminInformation(String username, String password){
        Criteria criteria = criteria()
                .add(Restrictions.eq("userName", username))
                .add(Restrictions.eq("password", password));
        return (Admin) criteria.uniqueResult();
    }

    public void setPassword(String password, int id){
        try {
            Session session = currentSession();
            Admin admin = session.find(Admin.class, id);
            admin.setPassword(password);
            admin.setPasswordChanged(1);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void updateProfile(String userName, String password, String email, int id){
        try {
            Session session = currentSession();
            Admin admin = session.find(Admin.class, id);
            admin.setUserName(userName);
            admin.setPassword(password);
            admin.setEmail(email);
        }catch(Exception ex) {
            System.out.println(ex);
        }
    }

    public Admin getLoggedAdminInformation(int id){
        Criteria criteria = criteria()
                .add(Restrictions.eq("id", id));
        return (Admin) criteria.uniqueResult();
    }


}
