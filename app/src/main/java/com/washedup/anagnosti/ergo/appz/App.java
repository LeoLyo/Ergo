package com.washedup.anagnosti.ergo.appz;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.washedup.anagnosti.ergo.R;

public class App extends Application {

    public static final String  CHANNEL_OBLIGATIONS = "obligations_channel_624";
    public static final String CHANNEL_BREAKS = "breaks_channel_224";
    //public static final String CHANNEL_INVITATIONS = "invitations_channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel obligations_channel = new NotificationChannel(
              CHANNEL_OBLIGATIONS,
              "Obligations Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            obligations_channel.setDescription("This is the channel for obligations received from superiors. ");

            NotificationChannel breaks_channel = new NotificationChannel(
                    CHANNEL_BREAKS,
                    "Break Requests Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            breaks_channel.setDescription("This is the channel for break requests received from volunteers. ");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(obligations_channel);
            manager.createNotificationChannel(breaks_channel);

        }
    }
}
