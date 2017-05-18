package com.wdfeww.docgl.data.SQLiteDatabase;


public class Patients {

    private int _id;
    private String _patientFirstName;
    private String _patientLastName;
    private int _userId;

    public Patients() {}

    public Patients(String patientFirstName, String patientLastName,int userId ) {

        this._patientFirstName = patientFirstName;
        this._patientLastName = patientLastName;
        this._userId = userId;

    }

    public int get_userId() {
        return _userId;
    }

    public void set_userId(int userId) {
        this._userId = userId;
    }

    public String get_patientFirstName() {
        return _patientFirstName;
    }

    public void set_patientFirstName(String patientFirstName) {
        this._patientFirstName = patientFirstName;
    }

    public String get_patientLastName() {
        return _patientLastName;
    }

    public void set_patientLastName(String patientLastName) {
        this._patientLastName = patientLastName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
