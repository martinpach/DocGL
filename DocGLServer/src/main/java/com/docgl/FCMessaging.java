package com.docgl;

import org.jose4j.json.internal.json_simple.JSONObject;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Martin on 26.5.2017.
 */
public class FCMessaging {
    public final static String AUTH_KEY_FCM ="AAAAcNqromA:APA91bGw99T0mPr8uGB40agDHPN3KIGksstDM2FDrqEtSEPitJy3wTOhWffK" +
            "VbuBsoKpLw9t28oK2OgWgZxRV62iHj7TriDGPudk9JXQfmK9ymfxI9HMiVt3i-W99mPzRuBJ4vJMwslV";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

// userDeviceIdKey is the device id you will query from your database

    public void pushFCMNotification(String userDeviceIdKey, String title, String message) throws Exception{

        String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        URLConnection conn = url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization","key="+authKey);
        conn.setRequestProperty("Content-Type","application/json");

        JSONObject json = new JSONObject();
        json.put("to",userDeviceIdKey.trim());
        json.put("notification", message);
        JSONObject info = new JSONObject();
        info.put("body", message);
        info.put("title", title);
        json.put("notification", info);
        System.out.println(json.toString());

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        conn.getInputStream();
    }
}
