package com.DocGL.DB;

import com.DocGL.entities.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Rasťo on 22.4.2017.
 */
public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> getAllUsers(){
        return list(namedQuery("getAllUsers"));
    }
}
