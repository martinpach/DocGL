package com.wdfeww.docgl.data.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.wdfeww.docgl.Home;
import com.wdfeww.docgl.R;
import com.wdfeww.docgl.data.SQLiteDatabase.MyDBHandler;
import com.wdfeww.docgl.data.model.User;

/**
 * Created by wdfeww on 5/25/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    SharedPreferences prefs;
    private static final String TAG = "MyFirebaseMessagingService";
    User user;
    @Override
    public void onCreate() {
        super.onCreate();
        prefs = this.getSharedPreferences("com.wdfeww.docgl", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("LoggedUser", "");
        user = gson.fromJson(json, User.class);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(new MyDBHandler(getApplicationContext()).isNotificationsEnabled(user.getPatient().getId())){
        Log.d(TAG, "FROM:"+remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "Message data: " +remoteMessage.getData());
        }
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message body: " +remoteMessage.getNotification().getBody() );
            sendNotification(remoteMessage.getNotification().getBody());
        }}
    }

    private void sendNotification(String body) {
        Intent intent = new Intent (this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        //notification sound
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.docgl_launcher)
                .setContentTitle("DocGL")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifiBuilder.build());
    }
}
