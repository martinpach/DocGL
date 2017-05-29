package com.wdfeww.docgl.data.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DocGL.db";


    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String patientsTable = "CREATE TABLE " + PatientsContract.Patients.TABLE_NAME + " (" +
                PatientsContract.Patients._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PatientsContract.Patients.COLUMN_PATIENTFIRSTNAME + " TEXT ," +
                PatientsContract.Patients.COLUMN_PATIENTLASTNAME + " TEXT ," +
                PatientsContract.Patients.COLUMN_USERID + " INT );";
        db.execSQL(patientsTable);

        String notificationsTable = "CREATE TABLE " + NotificationsContract.Notifications.TABLE_NAME + " (" +
                NotificationsContract.Notifications._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NotificationsContract.Notifications.COLUMN_NOTIFICATIONS + " TINYINT(1) DEFAULT 1 ," +
                NotificationsContract.Notifications.COLUMN_USERID + " INT );";
        db.execSQL(notificationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PatientsContract.Patients.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NotificationsContract.Notifications.TABLE_NAME);
        onCreate(db);
    }

    public void addPatient(Patients patient) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PatientsContract.Patients.COLUMN_PATIENTFIRSTNAME, patient.get_patientFirstName());
        values.put(PatientsContract.Patients.COLUMN_PATIENTLASTNAME, patient.get_patientLastName());
        values.put(PatientsContract.Patients.COLUMN_USERID, patient.get_userId());
        db.insert(PatientsContract.Patients.TABLE_NAME, null, values);
        db.close();
    }

    public void deletePatient(int patientId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + PatientsContract.Patients.TABLE_NAME + " WHERE " + PatientsContract.Patients._ID + "=" + patientId + ";");
    }

    public List<DBOutput> databaseToString(int userId) {
        List<DBOutput> dbString = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + PatientsContract.Patients.TABLE_NAME + " WHERE " + PatientsContract.Patients.COLUMN_USERID + "= " + userId + ";";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {

            dbString.add(new DBOutput(cursor.getInt(cursor.getColumnIndex(PatientsContract.Patients._ID)),
                    cursor.getString(cursor.getColumnIndex(PatientsContract.Patients.COLUMN_PATIENTFIRSTNAME)),
                    cursor.getString(cursor.getColumnIndex(PatientsContract.Patients.COLUMN_PATIENTLASTNAME))));
        }
        cursor.close();
        db.close();

        return dbString;
    }

    public boolean isNotificationsEnabled(int userId) {

        SQLiteDatabase db = getWritableDatabase();
        int isEnabled = 1;
        String query = "SELECT " + NotificationsContract.Notifications.COLUMN_NOTIFICATIONS + " FROM " + NotificationsContract.Notifications.TABLE_NAME + " WHERE " + NotificationsContract.Notifications.COLUMN_USERID + "= " + userId + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()) {
            isEnabled = cursor.getInt(cursor.getColumnIndex(NotificationsContract.Notifications.COLUMN_NOTIFICATIONS));
        }
        else{
            ContentValues values = new ContentValues();
            values.put(NotificationsContract.Notifications.COLUMN_USERID,userId);
            db.insert(NotificationsContract.Notifications.TABLE_NAME, null, values);
        }

        cursor.close();
        db.close();

        if (isEnabled == 1) {
            return true;
        }
        return false;
    }

    public void setNotifications(boolean notifications, int userId) {
        SQLiteDatabase db = getWritableDatabase();
        int bool;

        if(notifications==true)
            bool = 1;
        else
            bool =0;

        ContentValues values = new ContentValues();
        values.put( NotificationsContract.Notifications.COLUMN_NOTIFICATIONS ,bool);
        db.update(NotificationsContract.Notifications.TABLE_NAME, values,NotificationsContract.Notifications.COLUMN_USERID +" = "+ userId, null);



    }
}
