package com.docgl.db;

import com.docgl.entities.Admin;
import com.docgl.entities.Appointment;
import com.docgl.entities.Doctor;
import com.docgl.entities.Patient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;

/**
 * Created by Martin on 1.5.2017.
 */
public class AbstractDAO {
    protected final SessionFactory sessionFactory;
    protected Transaction tx;

    public AbstractDAO() {
        Configuration config = new Configuration();
        config.addAnnotatedClass(Admin.class);
        config.addAnnotatedClass(Doctor.class);
        config.addAnnotatedClass(Patient.class);
        config.addAnnotatedClass(Appointment.class);

        config.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        config.setProperty("hibernate.connection.username", "sa");
        config.setProperty("hibernate.connection.password", "sa");
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        config.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        config.setProperty("hibernate.show_sql", "true");
        config.setProperty("hibernate.current_session_context_class", "thread");
        config.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        config.setProperty("hibernate.hbm2ddl.import_files", "testing.sql");

        sessionFactory = config.buildSessionFactory();
    }

    @Before
    public void setUp() {
        Session session = sessionFactory.getCurrentSession();
        tx = session.beginTransaction();
    }

    @After
    public void tearDown() {
        if (tx != null) {
            tx.commit();
        }
    }
}
