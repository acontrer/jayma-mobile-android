package cl.citiaps.informatica.mensajeriaemergencia.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.DeviceData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by kayjt on 25-11-2016.
 */

public class SendFirebaseTokenService extends IntentService {

    Constants constants = new Constants();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendFirebaseTokenService(String name) {
        super(name);
    }

    public SendFirebaseTokenService() {
        super("SendFirebaseTokenService");
    }



    @Override
    protected void onHandleIntent(Intent workIntent) {

        Log.d(constants.LOG_NOTIFICATION, "Empezó el servicio para enviar el token");

        String token = workIntent.getStringExtra(constants.INTENT_TOKEN);
        int userID = workIntent.getIntExtra(constants.INTENT_USER_ID, 0);

        if (userID != 0){
            RestService restService = RestService.retrofit.create(RestService.class);
            DeviceData deviceData = new DeviceData(userID, token);
            Call<DeviceData> call = restService.registerDevice(deviceData);
            try {
                Response<DeviceData> response = call.execute();

                if (response.code() == 500 || response.body().isError()){

                    if (response.code() == 500){
                        Log.d(constants.LOG_TOKEN, response.errorBody().string());
                    }
                    else {
                        Log.d(constants.LOG_TOKEN, response.body().getError_message());
                    }
                }

                else{
                    Context context = getApplicationContext();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(constants.SHARED_PREFERENCES_TOKEN_UPDATE, false);
                    editor.commit();

                    Log.d(constants.LOG_TOKEN, "Token actualizado en servidor");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d(constants.LOG_TOKEN, "ID usuario no disponible");
    }
}
