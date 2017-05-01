package db;

import com.docgl.db.AbstractDAO;
import com.docgl.db.AdminDAO;
import com.docgl.entities.Admin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Martin on 1.5.2017.
 */
public class AdminDAOTest extends AbstractDAO {

    private final AdminDAO dao = new AdminDAO(sessionFactory);

    @Test
    public void getLoggedAdminInformationTest() {
        Admin admin = dao.getLoggedAdminInformation(1);

        assertEquals(1, admin.getId());
        assertEquals("rasto@button.sk", admin.getEmail());
    }

}