package com.wdfeww.docgl.data.SQLiteDatabase;

import android.provider.BaseColumns;

public final class PatientsContract {

    private PatientsContract() {}

    public static class Patients implements BaseColumns {

        public static final String TABLE_NAME = "Patients";
        public static final String COLUMN_PATIENTFIRSTNAME = "PatientFirstName";
        public static final String COLUMN_PATIENTLASTNAME = "PatientLastName";
        public static final String COLUMN_USERID = "UserID";
    }
}