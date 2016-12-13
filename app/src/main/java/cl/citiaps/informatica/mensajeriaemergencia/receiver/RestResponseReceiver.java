package cl.citiaps.informatica.mensajeriaemergencia.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.widget.Toast;

import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;

/**
 * Created by kayjt on 08-12-2016.
 */

public class RestResponseReceiver extends BroadcastReceiver
{

    // Prevents instantiation
    public RestResponseReceiver() {
    }
    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        switch (action){

            case Constants.BROADCAST_NOTIFY_ALERT_ANSWER_RESPONSE:

                Boolean isResponseOK = intent.getBooleanExtra(Constants.EXTENDED_ALERT_ANSWER_RESPONSE, false);

                if (isResponseOK){
                    generateToastNotification(context, "Estado enviado con éxito");
                }
                else{
                    generateToastNotification(context, "Error enviado tu estado");
                }
        }
    }


    private void generateToastNotification(Context context, String notificationText){

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, notificationText, duration);
        toast.show();
    }
}
