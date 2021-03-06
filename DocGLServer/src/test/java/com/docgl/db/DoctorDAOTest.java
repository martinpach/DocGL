package com.docgl.db;


import com.docgl.Cryptor;
import com.docgl.api.RegistrationInput;
import com.docgl.entities.Doctor;
import com.docgl.enums.SortableDoctorColumns;
import com.docgl.enums.SortingWays;
import com.docgl.enums.SpecializationsEnum;
import com.docgl.enums.UserType;
import com.docgl.exceptions.ValidationException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Rasťo on 6.5.2017.
 * Doctor DAO tests.
 */
public class DoctorDAOTest extends AbstractDAO {

    private final DoctorDAO dao = new DoctorDAO(sessionFactory);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

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

    @Test
    public void getAllDoctorsTest11(){
        exception.expect(ValidationException.class);
        List<Doctor> doctor = dao.getAllDoctors(5, 0, SortableDoctorColumns.ID, null,
                "Button", null);
        assertEquals(0, doctor.size());
    }

    @Test
    public void getAllDoctorsTest12(){
        List<Doctor> doctor = dao.getAllDoctors(5, 0, null, null,
                "Button", null);
        assertEquals(0, doctor.size());
    }

    @Test
    public void getAllDoctorsTest13(){
        exception.expect(ValidationException.class);
        List<Doctor> doctor = dao.getAllDoctors(5, 1, null, null,
                null, null);
        assertEquals(0, doctor.size());
    }

    @Test
    public void getAllDoctorsTest14(){
        exception.expect(ValidationException.class);
        List<Doctor> doctor = dao.getAllDoctors(-5, -1, null, null,
                null, null);
        assertEquals(0, doctor.size());
    }
    @Test
    public void getAllDoctorsTest15(){
        List<Doctor> doctor = dao.getAllDoctors(-5, 0, SortableDoctorColumns.ID, SortingWays.ASC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals(2, doctor.size());
    }

    @Test
    public void getAllDoctorsTest16(){
        List<Doctor> doctor = dao.getAllDoctors(5, -2, SortableDoctorColumns.ID, SortingWays.ASC,
                null, SpecializationsEnum.CARDIOLOGIST);
        assertEquals(2, doctor.size());
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
        dao.approveDoctor(3);
        Doctor doctor = dao.getLoggedDoctorInformation("jinn", "docwho123");
        assertTrue(doctor.isApproved());
    }
    @Test
    public void approveDoctorTest2() {
        exception.expect(BadRequestException.class);
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
        assertTrue(doctor.isApproved());
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
    /**
     * isPasswordDifferent test
     */
    @Test
    public void isPasswordDifferentTest() {
        Boolean isUnique = dao.isPasswordDifferent("docwho123", 1);
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
        Doctor doctor = dao.getLoggedDoctorInformation("doctorwho","blablabla123");
        assertEquals("blablabla123", Cryptor.decrypt(doctor.getPassword()));
    }

    @Test
    public void getDoctorTest() {
        Doctor doctor = dao.getDoctor(1);
        assertEquals(1, doctor.getId());
    }

    /**
     * setAppointmentsDuration test
     */
    @Test
    public void setAppointmentsDurationTest() {
        dao.setAppointmentsDuration(30, 1);
        Doctor doctor = dao.getLoggedDoctorInformation("doctorwho", "docwho123");
        assertEquals(30, doctor.getAppointmentsDuration());
    }

    @Test
    public void setDoctorsValidityTest() {
        dao.setDoctorsValidity(1, new Date());
        Doctor doctor = dao.getDoctor(1);
        assertNotNull(doctor.getDateOfValidity());
    }
    /**
     * addLikeToDoctor test
     */
    @Test
    public void addLikeToDoctorTest() {
        dao.addLikeToDoctor(1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals(11, doctor.getLikes());
    }
    /**
     * removeLikeFromDoctor test
     */
    @Test
    public void removeLikeFromDoctorTest() {
        dao.removeLikeFromDoctor(1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals(9, doctor.getLikes());
    }

    @Test
    public void markDoctorSetWorkingHoursTest(){
        dao.markDoctorSetWorkingHours(1);
        Doctor doctor = dao.getDoctor(1);
        assertTrue(doctor.isWorkingHoursSet());
    }

    @Test
    public void updateProfileTest(){
        dao.updateProfile("rastotest", "buttontest",
                "rasto@buttontest.sk", "rastobutton123test", "0944341879", 1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals("rastotest", doctor.getFirstName());
        assertEquals("buttontest", doctor.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(doctor.getPassword()));
        assertEquals("rasto@buttontest.sk", doctor.getEmail());
        assertEquals("0944341879", doctor.getPhone());
    }

    @Test
    public void updateProfileTest2(){
        dao.updateProfile("", "buttontest",
                "rasto@buttontest.sk", "rastobutton123test", "0944341879", 1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals("doctor", doctor.getFirstName());
        assertEquals("buttontest", doctor.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(doctor.getPassword()));
        assertEquals("rasto@buttontest.sk", doctor.getEmail());
        assertEquals("0944341879", doctor.getPhone());
    }

    @Test
    public void updateProfileTest3(){
        dao.updateProfile("rastotest", "",
                "rasto@buttontest.sk", "rastobutton123test", "0944341879", 1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals("rastotest", doctor.getFirstName());
        assertEquals("who", doctor.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(doctor.getPassword()));
        assertEquals("rasto@buttontest.sk", doctor.getEmail());
        assertEquals("0944341879", doctor.getPhone());
    }

    @Test
    public void updateProfileTest4(){
        dao.updateProfile("rastotest", "buttontest",
                "", "rastobutton123test", "999999999", 1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals("rastotest", doctor.getFirstName());
        assertEquals("buttontest", doctor.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(doctor.getPassword()));
        assertEquals("doctor@who.sk", doctor.getEmail());
        assertEquals("999999999", doctor.getPhone());
    }

    @Test
    public void updateProfileTest5(){
        dao.updateProfile("rastotest", "buttontest",
                "", "rastobutton123test", "", 1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals("rastotest", doctor.getFirstName());
        assertEquals("buttontest", doctor.getLastName());
        assertEquals("rastobutton123test", Cryptor.decrypt(doctor.getPassword()));
        assertEquals("doctor@who.sk", doctor.getEmail());
        assertEquals("0949473196", doctor.getPhone());
    }

    @Test
    public void updateProfileEmptyPasswordTest(){
        dao.updateProfile("rastotest", "buttontest",
                "rasto@buttontest.sk","", "0944341879", 1);
        Doctor doctor = dao.getDoctor(1);
        assertEquals("rastotest", doctor.getFirstName());
        assertEquals("buttontest", doctor.getLastName());
        assertEquals("rasto@buttontest.sk", doctor.getEmail());
        assertEquals("docwho123", Cryptor.decrypt(doctor.getPassword()));
    }
}
