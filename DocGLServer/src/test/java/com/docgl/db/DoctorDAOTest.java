package com.docgl.db;


import com.docgl.api.RegistrationInput;
import com.docgl.entities.Doctor;
import com.docgl.enums.SortableDoctorColumns;
import com.docgl.enums.SortingWays;
import com.docgl.enums.SpecializationsEnum;
import com.docgl.enums.UserType;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Rasťo on 6.5.2017.
 */
public class DoctorDAOTest extends AbstractDAO {

    private final DoctorDAO dao = new DoctorDAO(sessionFactory);

    /**
     * getAllDoctorsTest tests
     */
    //By specializations tests
    @Test
    public void getAllDoctorsTest(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.ID, SortingWays.ASC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals(2, doctor.size());
    }
    @Test
    public void getAllDoctorsTest2(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.ID, SortingWays.ASC,
                null, SpecializationsEnum.DENTIST);
        assertEquals(1, doctor.size());
    }
    //Limit test
    @Test
    public void getAllDoctorsTest3(){
        List<Doctor> doctor = dao.getAllDoctors(1, 0, SortableDoctorColumns.ID, SortingWays.ASC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals(1, doctor.size());
    }
    //SortingWays ASC test
    @Test
    public void getAllDoctorsTest4(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.ID, SortingWays.ASC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals(4, doctor.get(1).getId());
    }
    //SortingWays DESC test
    @Test
    public void getAllDoctorsTest5(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.ID, SortingWays.DESC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals(2, doctor.get(1).getId());
    }
    //Start test
    @Test
    public void getAllDoctorsTest6(){
        List<Doctor> doctor = dao.getAllDoctors(5, 1, SortableDoctorColumns.ID, SortingWays.ASC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals(4, doctor.get(0).getId());
    }
    //SortableDoctorColumns tests
    @Test
    public void getAllDoctorsTest7(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.LASTNAME, SortingWays.ASC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals("D2", doctor.get(0).getLastName());
    }
    @Test
    public void getAllDoctorsTest8(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.LASTNAME, SortingWays.DESC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals("Kenobi", doctor.get(0).getLastName());
    }
    //Search by name tests
    @Test
    public void getAllDoctorsTest9(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.ID, SortingWays.DESC,
                "ken", null);
        assertEquals("Kenobi", doctor.get(0).getLastName());
    }
    @Test
    public void getAllDoctorsTest10(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.ID, SortingWays.DESC,
                "Button", null);
        assertEquals(0, doctor.size());
    }

    /**
     * isUserNameAndEmailUnique tests
     */
    //Not unique email test
    @Test
    public void isUserNameAndEmailUniqueTest() {
        boolean isUnique = dao.isUserNameAndEmailUnique("hgfhfg", "doctor@who.sk");
        assertFalse(isUnique);
    }
    //Not unique userName test
    @Test
    public void isUserNameAndEmailUniqueTest2() {
        boolean isUnique = dao.isUserNameAndEmailUnique("doctorwho", "docor@who.sk");
        assertFalse(isUnique);
    }
    //Unique userName and email
    @Test
    public void isUserNameAndEmailUniqueTest3() {
        boolean isUnique = dao.isUserNameAndEmailUnique("asdafas", "docor@who.sk");
        assertTrue(isUnique);
    }
    //Case sensitive tests
    @Test
    public void isUserNameAndEmailUniqueTest4() {
        boolean isUnique = dao.isUserNameAndEmailUnique("Doctorwho", "fgfdg@who.sk");
        assertTrue(isUnique);
    }
    @Test
    public void isUserNameAndEmailUniqueTest5() {
        boolean isUnique = dao.isUserNameAndEmailUnique("hgfhfg", "Doctor@who.sk");
        assertTrue(isUnique);
    }
    /**
     * getNumberOfOverallLikes tests
     */
    @Test
    public void getNumberOfOverallLikesTest() {
        long likes = dao.getNumberOfOverallLikes();
        assertEquals(38, likes);
    }
    /**
     * getNumberOfRegistrations tests
     */
    @Test
    public void getNumberOfRegistrationsTest() {
        long countOfRegistrationToday = dao.getNumberOfRegistrations(new Date());
        assertEquals(4, countOfRegistrationToday);
    }
    /**
     * blockDoctor tests
     */
    @Test
    public void blockDoctorTest() {
        dao.blockDoctor(true, 1);
        Doctor patient = dao.getLoggedDoctorInformation("doctorwho", "docwho123");
        assertTrue(patient.isBlocked());
    }
    @Test
    public void blockDoctorTest2() {
        dao.blockDoctor(true, 1);
        dao.blockDoctor(false, 1);
        Doctor doctor = dao.getLoggedDoctorInformation("doctorwho", "docwho123");
        assertFalse(doctor.isBlocked());
    }
    /**
     * approveDoctor test
     */
    @Test
    public void approveDoctorTest() {
        dao.approveDoctor(1);
        Doctor doctor = dao.getLoggedDoctorInformation("doctorwho", "docwho123");
        assertTrue(doctor.isApproved());
    }
    /**
     * getNumberOfAllDoctors test
     */
    @Test
    public void getNumberOfAllDoctors() {
        long count = dao.getNumberOfAllDoctors();
        assertEquals(4, count);
    }
    /**
     * getLoggedDoctorInformationTest test
     */
    @Test
    public void getLoggedDoctorInformationTest() {
        Doctor doctor = dao.getLoggedDoctorInformation("dsdfsdwho", "docwho123");
        assertNull(doctor);
    }
    //Case sensitive tests
    @Test
    public void getLoggedDoctorInformationTest2() {
        Doctor doctor = dao.getLoggedDoctorInformation("doctorwho", "Docwho123");
        assertNull(doctor);
    }
    @Test
    public void getLoggedDoctorInformationTest3() {
        Doctor doctor = dao.getLoggedDoctorInformation("Doctorwho", "docwho123");
        assertNull(doctor);
    }
    //correct credentials test
    @Test
    public void getLoggedDoctorInformationTest4() {
        Doctor doctor = dao.getLoggedDoctorInformation("doctorwho", "docwho123");
        assertEquals("doctor", doctor.getFirstName());
        assertEquals("who", doctor.getLastName());
        assertEquals("doctor@who.sk", doctor.getEmail());
        assertEquals("doctorwho", doctor.getUserName());
        Date today = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
        assertEquals(today.getTime(), doctor.getRegistrationDate().getTime());
        assertEquals(10, doctor.getLikes());
        assertEquals(SpecializationsEnum.DENTIST, doctor.getSpecialization());
        assertEquals("0949473196", doctor.getPhone());
        assertEquals("Kosice", doctor.getCity());
        assertEquals("Kukucinova 5", doctor.getWorkplace());
        assertFalse(doctor.isBlocked());
        assertFalse(doctor.isApproved());
    }
    /**
     * registerDoctor test
     */
    @Test
    public void registerDoctorTest() {
        RegistrationInput registrationInput = new RegistrationInput("NewDoc", "doctor123",
                "FirstDoctor", "LastDoctor", "doctor@new.com", UserType.DOCTOR,
                SpecializationsEnum.ORTHOPEDIST, "0925648528", "Kosice", "Jarna 8");
        dao.registerDoctor(registrationInput);
        Doctor doctor = dao.getLoggedDoctorInformation("NewDoc", "doctor123");
        assertEquals("FirstDoctor", doctor.getFirstName());
        assertEquals("LastDoctor", doctor.getLastName());
        assertEquals("doctor@new.com", doctor.getEmail());
        assertEquals("NewDoc", doctor.getUserName());
        Date today = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
        Date registrationDate = DateUtils.truncate(doctor.getRegistrationDate(), java.util.Calendar.DAY_OF_MONTH);
        assertEquals(today.getTime(), registrationDate.getTime());
        assertEquals(0, doctor.getLikes());
        assertEquals(SpecializationsEnum.ORTHOPEDIST, doctor.getSpecialization());
        assertEquals("0925648528", doctor.getPhone());
        assertEquals("Kosice", doctor.getCity());
        assertEquals("Jarna 8", doctor.getWorkplace());
        assertFalse(doctor.isBlocked());
        assertFalse(doctor.isApproved());
    }

}
