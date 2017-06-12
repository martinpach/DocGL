package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.entities.Admin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Martin on 1.5.2017.
 * Admin DAO tests.
 */
public class AdminDAOTest extends AbstractDAO {

    private final AdminDAO dao = new AdminDAO(sessionFactory);

    @Test
    public void getLoggedByIdTest() {
        Admin admin = dao.getLoggedAdminInformation(1);

        assertEquals(1, admin.getId());
        assertEquals("rasto@button.sk", admin.getEmail());
        assertEquals("rastobutton", admin.getUserName());
        assertEquals("rasto", admin.getFirstName());
    }

    @Test
    public void getLoggedIncorrectPasswordTest(){
        Admin admin = dao.getLoggedAdminInformation("rastobutton","badpassword");
        assertNull(admin);
    }

    @Test
    public void getLoggedCorrectPasswordTest(){
        Admin admin = dao.getLoggedAdminInformation("rastobutton", "rastobutton123");
        assertEquals(1, admin.getId());
    }

    @Test
    public void getLoggedIncorrectUserNameTest(){
        Admin admin = dao.getLoggedAdminInformation("rastObutton", "rastobutton123");
        assertNull(null, admin);
    }

    @Test
    public void setPasswordTest(){
        dao.setPassword("testpassword", 1);
        Admin admin = dao.getLoggedAdminInformation(1);
        assertEquals("testpassword", Cryptor.decrypt(admin.getPassword()));
    }

    @Test
    public void updateProfileTest(){
        dao.updateProfile("rastotest", "buttontest", "rastobutton123test", "rasto@buttontest.sk", 1);
        Admin admin = dao.getLoggedAdminInformation(1);
        assertEquals("rastotest", admin.getFirstName());
        assertEquals("buttontest", admin.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(admin.getPassword()));
        assertEquals("rasto@buttontest.sk", admin.getEmail());
    }

    @Test
    public void updateProfileTest2(){
        dao.updateProfile("rastotest", "", "rastobutton123test", "rasto@buttontest.sk", 1);
        Admin admin = dao.getLoggedAdminInformation(1);
        assertEquals("rastotest", admin.getFirstName());
        assertEquals("button", admin.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(admin.getPassword()));
        assertEquals("rasto@buttontest.sk", admin.getEmail());
    }

    @Test
    public void updateProfileTest3(){
        dao.updateProfile("", "buttontest", "rastobutton123test", "rasto@buttontest.sk", 1);
        Admin admin = dao.getLoggedAdminInformation(1);
        assertEquals("rasto", admin.getFirstName());
        assertEquals("buttontest", admin.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(admin.getPassword()));
        assertEquals("rasto@buttontest.sk", admin.getEmail());
    }

    @Test
    public void updateProfileTest4(){
        dao.updateProfile("rastotest", "buttontest", "rastobutton123test", "", 1);
        Admin admin = dao.getLoggedAdminInformation(1);
        assertEquals("rastotest", admin.getFirstName());
        assertEquals("buttontest", admin.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(admin.getPassword()));
        assertEquals("rasto@button.sk", admin.getEmail());
    }

    @Test
    public void updateProfileEmptyPasswordTest(){
        dao.updateProfile("rastotest", "buttontest", "", "rasto@buttontest.sk", 1);
        Admin admin = dao.getLoggedAdminInformation(1);
        assertEquals("rastotest", admin.getFirstName());
        assertEquals("buttontest", admin.getLastName());
        assertEquals("rasto@buttontest.sk", admin.getEmail());
        assertEquals("rastobutton123", Cryptor.decrypt(admin.getPassword()));
    }

}