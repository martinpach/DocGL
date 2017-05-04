package com.docgl.db;

import com.docgl.Cryptor;
import com.docgl.entities.Admin;
import com.docgl.entities.Patient;
import com.docgl.enums.SortablePatientColumns;
import com.docgl.enums.SortingWays;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by wdfeww on 5/4/17.
 */
public class PatientDAOTest extends AbstractDAO {

    private final PatientDAO dao = new PatientDAO(sessionFactory);

    @Test
    public void getAllPatientsTest(){
       List<Patient> patient =  dao.getAllPatients(5,0,  SortablePatientColumns.ID, SortingWays.ASC,"");
       assertEquals(4, patient.size());
    }

    @Test
    public void getLoggedIncorrectPasswordTest(){
        Patient patient = dao.getLoggedPatientInformation("patientwho","badpassword");
        assertEquals(null, patient);
    }

    @Test
    public void getLoggedCorrectPasswordTest(){
        Patient patient = dao.getLoggedPatientInformation("patientwho", "patwho123");
        assertEquals(1, patient.getId());
    }

    @Test
    public void getLoggedIncorrectUserNameTest(){
        Patient patient = dao.getLoggedPatientInformation("badLoginName", "patwho123");
        assertEquals(null, patient);
    }

   

}
