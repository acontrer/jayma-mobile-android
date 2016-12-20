package cl.citiaps.informatica.mensajeriaemergencia.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.activity.SendAlertActivity;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;

public class FirebaseMessageService extends FirebaseMessagingService {
    public FirebaseMessageService() {
    }

    int NEW_EMERGENCY_NOTIFICATION_ID = 1;
    String EMERGENCY_NOTIFICATION_SENDER = "/topics/emergency_";
    private static final int NOTIFICATION_TIME_MS= 30000;
    boolean notificationGroup = false;
    ArrayList<Map<String, String>> notificationDataList = new ArrayList<>();



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(Constants.LOG_NOTIFICATION, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            if(remoteMessage.getFrom().startsWith(EMERGENCY_NOTIFICATION_SENDER)){
                Log.d(Constants.LOG_NOTIFICATION, "Message data payload: " + remoteMessage.getData());

                notificationDataList.add(remoteMessage.getData());
                if (!notificationGroup){

                    notificationGroup = true;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run(){
                            String contentText = "";
                            if (notificationDataList.size() > 1){
                                int otherNotifications = notificationDataList.size() - 1;
                                contentText = getString(R.string.notification_text_1) +
                                        " (" +
                                        notificationDataList.get(0).get("emergencyTitle") +
                                        ", y de " + otherNotifications + " más). " +
                                        getString(R.string.notification_text_2);
                            }
                            else{
                                contentText = getString(R.string.notification_text_1) +
                                        " (" +
                                        notificationDataList.get(0).get("emergencyTitle") +
                                        "). " +
                                        getString(R.string.notification_text_2);
                            }
                            generateEmergencyNotification(contentText);
                            notificationGroup = false;
                            notificationDataList = new ArrayList<>();
                        }
                    }, NOTIFICATION_TIME_MS);


                }

            }


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(Constants.LOG_NOTIFICATION, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    public void generateEmergencyNotification(String contentText){

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.send_alert_icon)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setVibrate(new long[] { 1000, 1000});

        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(getString(R.string.notification_title))
                    .bigText(contentText);

        notificationBuilder.setStyle(bigTextStyle);






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
