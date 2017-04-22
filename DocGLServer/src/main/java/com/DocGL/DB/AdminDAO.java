package com.docgl.db;

import com.docgl.entities.Admin;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


/**
 * Created by D33 on 4/8/2017.
 */
public class AdminDAO extends AbstractDAO<Admin> {
    private Session session = null;
    private Transaction tx = null;

    public AdminDAO(SessionFactory factory) {
        super(factory);
        session=factory.openSession();
    }


    public Admin getAdminInformation(String username, String password){
        Query query= session.getNamedQuery("com.DocGL.api.getAdminInformation");
        query.setParameter("username",username);
        query.setParameter("password", password);
        if(query.list().isEmpty()){
            return null;
        }
        return (Admin) query.getSingleResult() ;
    }

    public boolean setPassword(String password, int id){
        boolean isChanged=false;
        try {
            tx = session.beginTransaction();
            Query query = session.getNamedQuery("com.DocGL.api.setPassword");
            query.setParameter("password", password);
            query.setParameter("id", id);
            query.executeUpdate();
            tx.commit();
            isChanged= true;
        }catch(Exception ex){
            isChanged=false;
            System.out.println(ex);
            tx.rollback();
        }

        return isChanged;
    }

    public void updateProfile(String userName, String email, String password, int id){
        try {
            tx = session.beginTransaction();
            Query query = session.getNamedQuery("com.DocGL.api.setProfile");
            query.setParameter("username", userName);
            query.setParameter("email", email);
            query.setParameter("password", password);
            query.setParameter("id", id);
            query.executeUpdate();
            tx.commit();
        }catch(Exception ex) {
            System.out.println(ex);
            tx.rollback();
        }
    }

    public Admin getAdminInformation(int id){
        Query query = session.getNamedQuery("com.DocGL.api.getAdminInformationById");
        query.setParameter("id", id);
        if(query.list().isEmpty()){
            return null;
        }
        return (Admin) query.getSingleResult();
    }


}
