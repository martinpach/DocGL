package com.wdfeww.docgl.data.SQLiteDatabase;



public class DBOutput {
    String firstname;
    String lastname;
    int patientID;

    public DBOutput(int patientID, String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patientID = patientID;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getPatientID() {
        return patientID;
    }
}
