package com.DocGL.DB;

import com.DocGL.api.AdminInput;
import com.DocGL.entities.Admin;
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
    private Session session = null;
    private Transaction tx = null;
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

    public boolean setPassword(String password, int id){
        boolean isChanged=false;
        try {
            tx = session.beginTransaction();
            Query query = session.getNamedQuery("com.DocGL.api.setPassword");
            query.setString("password", password);
            query.setInteger("id", id);
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

    public void updateProfile(AdminInput admin, int id){
        try {
            tx = session.beginTransaction();
            Query query = session.getNamedQuery("com.DocGL.api.setPassword");
            query.setString("username", admin.getUserName());
            query.setString("email", admin.getEmail());
            query.setString("password", admin.getPassword());
            query.setInteger("id", id);
            query.executeUpdate();
            tx.commit();
        }catch(Exception ex) {
            System.out.println(ex);
            tx.rollback();
        }
    }


}
