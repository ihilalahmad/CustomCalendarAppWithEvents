package com.example.semesterschedulingapp.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.Utils.Config;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Context mContext;
    String task_title;
    String task_details;
    String click_acion;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size()>0){

            Log.d(TAG, "Data Payload: " + remoteMessage.getData().toString());

            task_title = remoteMessage.getData().get("task_title");
            task_details = remoteMessage.getData().get("task_details");
            click_acion = remoteMessage.getData().get("click_action");

            if (task_title != null && task_details != null && click_acion != null){

                Log.d("taskTitle", task_title);
                Log.d("taskDetails", task_details);

                getNotification();
            }

        }
    }

    private void getNotification() {

        Intent intent = new Intent(click_acion);
        intent.putExtra("title",task_title);
        intent.putExtra("details", task_details);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //this is unique key for multiple notifications, with every notify its generate unique key.
        Random random = new Random();
        int pendingRandomID = random.nextInt(9999 - 1000) + 1000;
        Log.d("RandomNumberPending", String.valueOf(pendingRandomID));

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), pendingRandomID, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

// notification icon
        final int icon = R.drawable.notification_icon;

//Creating channel for Oreo and upper than Oreo version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(Config.NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Config.NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(task_title);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setContentText(task_details);
        builder.setSound(sounduri);
        builder.setSmallIcon(icon);


        //this is unique key for multiple notifications, with every notify its generate unique key.
        Random notifyRandom = new Random();
        int notifyRandomID = notifyRandom.nextInt(9999 - 1000) + 1000;
        Log.d("RandomNumberNotify", String.valueOf(notifyRandomID));
        notificationManager.notify(notifyRandomID, builder.build());
    }
}
