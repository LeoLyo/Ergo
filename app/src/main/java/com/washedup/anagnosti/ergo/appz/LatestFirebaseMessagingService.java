package com.washedup.anagnosti.ergo.appz;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.eventPerspective.EventPerspectiveActivity;
import com.washedup.anagnosti.ergo.otherHomePossibilities.ChooseEventForPerspectiveActivity;

public class LatestFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "LatestFirebaseMService";
    private static final int BROADCAST_NOTIFICATION_ID = 1;

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            sendRegistrationToServer(s, mAuth);
        }else{
            Log.d(TAG,"User not logged in yet.");
        }
        /*final String token = s;
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        });*/

    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationBody = "";
        String notificationTitle = "";
        String notificationData = "";
        try {
            notificationData = remoteMessage.getData().toString();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        } catch (NullPointerException e) {
            Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage());

        }
        Log.d(TAG, "onMessageReceived: data: " + notificationData);
        Log.d(TAG, "onMessageReceived: notification body: " + notificationBody);
        Log.d(TAG, "onMessageReceived: notification title: " + notificationTitle);

        String dataType = remoteMessage.getData().get(getString(R.string.data_type));
        if (dataType.equals(getString(R.string.obligation_message))) {
            Log.d(TAG, "onMessageReceived: new incoming obligation message.");
            String title = remoteMessage.getData().get(getString(R.string.data_title));
            String message = remoteMessage.getData().get(getString(R.string.data_message));
            //String messageId = remoteMessage.getData().get(getString(R.string.data_message_id));
            sendObligationMessageNotification(title, message);
        } else if (dataType.equals(getString(R.string.break_message))) {
            Log.d(TAG, "onMessageReceived: new incoming break message.");
            String title = remoteMessage.getData().get(getString(R.string.data_title));
            String message = remoteMessage.getData().get(getString(R.string.data_message));
            //String messageId = remoteMessage.getData().get(getString(R.string.data_message_id));
            sendObligationMessageNotification(title, message);
        }

    }

    /**
     * Build a push notification for a chat message
     *
     * @param title
     * @param message
     */
    private void sendObligationMessageNotification(String title, String message) {
        Log.d(TAG, "sendObligationMessageNotification: building a chat message notification");

        //get the notification id
        int notificationId = buildNotificationId(title);

        //Instantiate a builder object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.obligations_channel_id));

        //Creates an Intent for the Activity
        Intent pendingIntent = new Intent(this, ChooseEventForPerspectiveActivity.class);

        //Sets the Activity to start in a new, empty task
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Creates the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this,
                0,
                pendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        builder.setSmallIcon(R.drawable.ic_launcher_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setColor(Color.parseColor("#1FAFDC"))
                .setAutoCancel(true)
                .setSubText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOnlyAlertOnce(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, builder.build());

    }

    private int buildNotificationId(String id){
        Log.d(TAG, "buildNotificationId: building a notification id.");
        int notificationId = 0;
        for(int i = 0; i < 9; i++){
            notificationId = notificationId + id.charAt(0);
        }
        Log.d(TAG, "buildNotificationId: id: " + id);
        Log.d(TAG, "buildNotificationId: notification id:" + notificationId);
        return notificationId;
    }


    private void sendRegistrationToServer(String token, FirebaseAuth mAuth) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        db.collection("Users").document(userEmail).update("messaging_token", token);
    }
}
