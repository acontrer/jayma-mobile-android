package cl.citiaps.informatica.mensajeriaemergencia.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.activity.SendAlertActivity;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;

public class FirebaseMessageService extends FirebaseMessagingService {
    public FirebaseMessageService() {
    }

    Constants constants = new Constants();
    int NEW_EMERGENCY_NOTIFICATION_ID = 1;
    String EMERGENCY_NOTIFICATION_SENDER = "/topics/emergency_";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(constants.LOG_NOTIFICATION, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            if(remoteMessage.getFrom().startsWith(EMERGENCY_NOTIFICATION_SENDER)){

                Log.d(constants.LOG_NOTIFICATION, "Message data payload: " + remoteMessage.getData());
                String contentText = getString(R.string.notification_text_1) +
                        remoteMessage.getData().get("title") + getString(R.string.notification_text_2);


                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.send_alert_icon)
                                .setContentTitle(getString(R.string.notification_title))
                                .setContentText(contentText);
// Creates an explicit intent for an Activity in your app
                Intent sendAlertIntent = new Intent(this, SendAlertActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(SendAlertActivity.class);
                stackBuilder.addNextIntent(sendAlertIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                notificationBuilder.setContentIntent(resultPendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NEW_EMERGENCY_NOTIFICATION_ID, notificationBuilder.build());
            }


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(constants.LOG_NOTIFICATION, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
