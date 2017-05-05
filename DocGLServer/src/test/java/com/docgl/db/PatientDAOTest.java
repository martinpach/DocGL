package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.entities.Admin;
import com.docgl.entities.Patient;
import com.docgl.enums.SortablePatientColumns;
import com.docgl.enums.SortingWays;
import org.junit.Test;


import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by wdfeww on 5/4/17.
 */
public class PatientDAOTest extends AbstractDAO {

    private final PatientDAO dao = new PatientDAO(sessionFactory);

    @Test
    public void getAllPatientsTest() {
        List<Patient> patient = dao.getAllPatients(5, 0, SortablePatientColumns.ID, SortingWays.ASC, "");
        assertEquals(4, patient.size());
    }

    @Test
    public void getLoggedIncorrectPasswordTest() {
        Patient patient = dao.getLoggedPatientInformation("patientwho", "badpassword");
        assertEquals(null, patient);
    }

    @Test
    public void getLoggedCorrectPasswordTest() {
        Patient patient = dao.getLoggedPatientInformation("patientwho", "patwho123");
        assertEquals(1, patient.getId());
    }

    @Test
    public void getLoggedIncorrectUserNameTest() {
        Patient patient = dao.getLoggedPatientInformation("badLoginName", "patwho123");
        assertEquals(null, patient);
    }

    @Test
    public void isUserNameAndEmailUniqueTest() {
        boolean isUnique = dao.isUserNameAndEmailUnique("patientwho", "patient@who.cz");
        assertEquals(false, isUnique);
    }

    @Test
    public void isUserNameAndEmailUniqueTest2() {
        boolean isUnique = dao.isUserNameAndEmailUnique("asdafas", "patient@who.cz");
        assertEquals(false, isUnique);
    }

    @Test
    public void isUserNameAndEmailUniqueTest3() {
        boolean isUnique = dao.isUserNameAndEmailUnique("patientwho", "asd@asdd.sd");
        assertEquals(false, isUnique);
    }

    @Test
    public void isUserNameAndEmailUniqueTest4() {
        boolean isUnique = dao.isUserNameAndEmailUnique("asdsafasd", "asd@asdd.sd");
        assertEquals(true, isUnique);
    }

    @Test
    public void getNumberOfRegistrationsTest() {
        long countOfRegistrationToday = dao.getNumberOfRegistrations(new Date());
        assertEquals(4, countOfRegistrationToday);
    }

    @Test
    public void blockPatientTest() {
        dao.blockPatient(true, 1);
        Patient patient = dao.getLoggedPatientInformation("patientwho", "patwho123");
        assertEquals(true, patient.isBlocked());
    }

    @Test
    public void blockPatientTest2() {
        dao.blockPatient(true, 1);
        dao.blockPatient(false, 1);
        Patient patient = dao.getLoggedPatientInformation("patientwho", "patwho123");
        assertEquals(false, patient.isBlocked());
    }

    @Test
    public void getNumberOfAllPatientsTest() {
        long count = dao.getNumberOfAllPatients();
        assertEquals(4, count);
    }

}
