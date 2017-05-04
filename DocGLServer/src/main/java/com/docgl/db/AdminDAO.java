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

    /**
     * This function is called during login. If credentials are incorrect function return null.
     * @param username login username
     * @param password login password
     * @return Admin entity object with entered username and password
     */
    public Admin getLoggedAdminInformation(String username, String password){
        Criteria criteria = criteria()
                .add(Restrictions.eq("userName", username))
                .add(Restrictions.eq("password", Cryptor.encrypt(password)));
        return (Admin) criteria.uniqueResult();
    }

    /**
     * This function sets admin password and encrypt it. It also sets password_changed property in database to true
     * @param password new password
     * @param id admin id in database
     */
    public void setPassword(String password, int id){
        Session session = currentSession();
        Admin admin = session.find(Admin.class, id);
        admin.setPassword(Cryptor.encrypt(password));
        admin.setPasswordChanged(true);
    }

    /**
     * @param userName new userName
     * @param password new password
     * @param email new email
     * @param id represents unique admin
     */
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

    /**
     * @param id represents unique admin
     * @return Admin entity object based on entered id
     */
    public Admin getLoggedAdminInformation(int id){
        Criteria criteria = criteria()
                .add(Restrictions.eq("id", id));
        return (Admin) criteria.uniqueResult();
    }


}
