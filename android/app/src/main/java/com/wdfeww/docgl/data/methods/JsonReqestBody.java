package com.wdfeww.docgl.data.methods;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReqestBody {
    public static JSONObject login(String username, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("userName", username);
            json.put("password", password);
            json.put("userType", "PATIENT");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static JSONObject register(String username, String password, String firstName, String lastName, String email) {
        JSONObject json = new JSONObject();
        try {
            json.put("userName", username);
            json.put("password", password);
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("email", email);
            json.put("userType", "PATIENT");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static JSONObject changePass(String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static JSONObject updateProfile(String firstname, String lastname, String email){
        JSONObject json = new JSONObject();
        try {
            json.put("firstName", firstname);
            json.put("lastName", lastname);
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static JSONObject FreeDateToAppoitnment(int id, String dateFrom, String dateTo){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("dateFrom", dateFrom);
            json.put("dateTo", dateTo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static JSONObject newAppointment(String date,String time, String firstName, String lastName, int id, String note){
        JSONObject json = new JSONObject();
        try {
            json.put("date", date);
            json.put("time", time+":00");
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("doctorId", id);
            json.put("note", note);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static JSONObject addDoctorToFavourite( int DoctorIdInput ){
        JSONObject json = new JSONObject();
        try {
            json.put("doctorId", DoctorIdInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static JSONObject updateFCMRegistrationToken( String fcmRegistrationToken ){
        JSONObject json = new JSONObject();
        try {
            json.put("fcmRegistrationToken", fcmRegistrationToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
