package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.entities.Admin;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;

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
                .add(Restrictions.eq("password", Cryptor.encrypt(password)));
        return (Admin) criteria.uniqueResult();
    }

    public void setPassword(String password, int id){
        Session session = currentSession();
        Admin admin = session.find(Admin.class, id);
        admin.setPassword(Cryptor.encrypt(password));
        admin.setPasswordChanged(true);
    }

    public void updateProfile(String userName, String password, String email, int id){
        Session session = currentSession();
        Admin admin = session.find(Admin.class, id);
        if(!userName.equals("")) {
            admin.setUserName(userName);
        }
        if(!password.equals("")) {
            admin.setPassword(Cryptor.encrypt(password));
        }
        if(!email.equals("")) {
            admin.setEmail(email);
        }
    }

    public Admin getLoggedAdminInformation(int id){
        Criteria criteria = criteria()
                .add(Restrictions.eq("id", id));
        return (Admin) criteria.uniqueResult();
    }


}
