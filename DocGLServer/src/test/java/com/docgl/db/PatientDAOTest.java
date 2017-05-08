package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.api.RegistrationInput;
import com.docgl.entities.Doctor;
import com.docgl.entities.Patient;
import com.docgl.enums.SortablePatientColumns;
import com.docgl.enums.SortingWays;
import com.docgl.enums.UserType;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by wdfeww on 5/4/17.
 */
public class PatientDAOTest extends AbstractDAO {

    private final PatientDAO dao = new PatientDAO(sessionFactory);
    private final DoctorDAO doctorDAO = new DoctorDAO(sessionFactory);

    @Test
    public void getAllPatientsTest() {
        List<Patient> patient = dao.getAllPatients(5, 0, SortablePatientColumns.ID, SortingWays.ASC, "");
        assertEquals(4, patient.size());
    }

    @Test
    public void getLoggedIncorrectPasswordTest() {
        Patient patient = dao.getLoggedPatientInformation("patientwho", "badpassword");
        assertNull(patient);
    }

    @Test
    public void getLoggedCorrectPasswordTest() {
        Patient patient = dao.getLoggedPatientInformation("patientwho", "patwho123");
        assertNotNull(patient.getId());
    }

    @Test
    public void getLoggedIncorrectUserNameTest() {
        Patient patient = dao.getLoggedPatientInformation("badLoginName", "patwho123");
        assertNull(patient);
    }

    @Test
    public void isUserNameAndEmailUniqueTest() {
        boolean isUnique = dao.isUserNameAndEmailUnique("patientwho", "patient@who.cz");
        assertFalse(isUnique);
    }

    @Test
    public void isUserNameAndEmailUniqueTest2() {
        boolean isUnique = dao.isUserNameAndEmailUnique("asdafas", "patient@who.cz");
        assertFalse(isUnique);
    }

    @Test
    public void isUserNameAndEmailUniqueTest3() {
        boolean isUnique = dao.isUserNameAndEmailUnique("patientwho", "asd@asdd.sd");
        assertFalse(isUnique);
    }

    @Test
    public void isUserNameAndEmailUniqueTest4() {
        boolean isUnique = dao.isUserNameAndEmailUnique("asdsafasd", "asd@asdd.sd");
        assertTrue(isUnique);
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
        assertTrue(patient.isBlocked());
    }

    @Test
    public void blockPatientTest2() {
        dao.blockPatient(true, 1);
        dao.blockPatient(false, 1);
        Patient patient = dao.getLoggedPatientInformation("patientwho", "patwho123");
        assertFalse(patient.isBlocked());
    }

    @Test
    public void getNumberOfAllPatientsTest() {
        long count = dao.getNumberOfAllPatients();
        assertEquals(4, count);
    }

    /**
     * getLoggedPatientInformation test
     */
    @Test
    public void getLoggedPatientInformationTest() {
        Patient patient = dao.getLoggedPatientInformation("dsdfsdwho", "patwho123");
        assertNull(patient);
    }
    //Case sensitive tests
    @Test
    public void getLoggedPatientInformationTest2() {
        Patient patient = dao.getLoggedPatientInformation("patientwho", "Patwho123");
        assertNull(patient);
    }
    @Test
    public void getLoggedPatientInformationTest3() {
        Patient patient = dao.getLoggedPatientInformation("Patientwho", "patwho123");
        assertNull(patient);
    }
    //correct credentials test
    @Test
    public void getLoggedPatientInformationTest4() {
        Patient patient = dao.getLoggedPatientInformation("patientwho", "patwho123");
        assertEquals("patient", patient.getFirstName());
        assertEquals("who", patient.getLastName());
        assertEquals("patient@who.cz", patient.getEmail());
        assertEquals("patientwho", patient.getUserName());
        Date today = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
        assertEquals(today.getTime(), patient.getRegistrationDate().getTime());
        assertFalse(patient.isBlocked());
    }
    /**
     * registerPatient test
     */
    @Test
    public void registerPatientTest() {
        RegistrationInput registrationInput = new RegistrationInput("NewPat", "NewPat23",
                "FirstPatient", "LastPatient", "new.patient@who.cz", UserType.PATIENT);
        dao.registerPatient(registrationInput);
        Patient patient = dao.getLoggedPatientInformation("NewPat", "NewPat23");
        assertEquals("FirstPatient", patient.getFirstName());
        assertEquals("LastPatient", patient.getLastName());
        assertEquals("new.patient@who.cz", patient.getEmail());
        assertEquals("NewPat", patient.getUserName());
        Date today = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
        Date registrationDate = DateUtils.truncate(patient.getRegistrationDate(), java.util.Calendar.DAY_OF_MONTH);
        assertEquals(today.getTime(), registrationDate.getTime());
        assertFalse(patient.isBlocked());
    }
    /**
     * isPasswordDifferent test
     */
    @Test
    public void isPasswordDifferentTest() {
        Boolean isUnique = dao.isPasswordDifferent("patwho123", 1);
        assertEquals(false, isUnique);
    }
    @Test
    public void isPasswordDifferentTest2() {
        Boolean isUnique = dao.isPasswordDifferent("blablabla123", 1);
        assertEquals(true, isUnique);
    }
    /**
     * setPassword test
     */
    @Test
    public void setPasswordTest() {
        dao.setPassword("blablabla123", 1);
        Patient patient = dao.getLoggedPatientInformation("patientwho","blablabla123");
        assertEquals("blablabla123", Cryptor.decrypt(patient.getPassword()));
    }
    /**
     * getFavouriteDoctors test
     */
    @Test
    public void getFavouriteDoctorsTest() {
        Collection<Doctor> favouriteDoctors = dao.getFavouriteDoctors(1);
        assertEquals(2, favouriteDoctors.size());
    }
    @Test
    public void getFavouriteDoctorsTest2() {
        Collection<Doctor> favouriteDoctors = dao.getFavouriteDoctors(1);
        Doctor doctor = doctorDAO.getDoctor(1);
        assertTrue(favouriteDoctors.contains(doctor));
    }
    @Test
    public void getFavouriteDoctorsTest3() {
        Collection<Doctor> favouriteDoctors = dao.getFavouriteDoctors(1);
        Doctor doctor = doctorDAO.getDoctor(3);
        assertFalse(favouriteDoctors.contains(doctor));
    }
    /**
     * addDoctorToFavourite test
     */
    @Test
    public void addDoctorToFavouriteTest() {
        dao.addDoctorToFavourite(1,3);
        Collection<Doctor> favouriteDoctors = dao.getFavouriteDoctors(1);
        Doctor doctor = doctorDAO.getDoctor(3);
        assertTrue(favouriteDoctors.contains(doctor));
    }
    @Test
    public void addDoctorToFavouriteTest2() {
        dao.addDoctorToFavourite(1,10);
        Collection<Doctor> favouriteDoctors = dao.getFavouriteDoctors(1);
        assertNull(doctorDAO.getDoctor(10));
    }
    /**
     * removeDoctorFromFavourite test
     */
    @Test
    public void removeDoctorFromFavourite() {
        dao.removeDoctorFromFavourite(1,2);
        Collection<Doctor> favouriteDoctors = dao.getFavouriteDoctors(1);
        Doctor doctor = doctorDAO.getDoctor(2);
        assertFalse(favouriteDoctors.contains(doctor));
    }

}
