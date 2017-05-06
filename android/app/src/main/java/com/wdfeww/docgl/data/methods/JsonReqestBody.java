package com.wdfeww.docgl.data.methods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wdfeww on 5/6/17.
 */

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
}
