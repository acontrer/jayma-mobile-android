package cl.citiaps.informatica.mensajeriaemergencia.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;

/**
 * Created by kayjt on 24-11-2016.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {

    Constants constants = new Constants();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", "Refreshed token: " + refreshedToken);

        saveRefreshedToken(refreshedToken);
    }

    private void saveRefreshedToken(String refreshedToken){

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(constants.SHARED_PREFERENCES_TOKEN,
                refreshedToken);
        editor.putBoolean(constants.SHARED_PREFERENCES_TOKEN_UPDATE, true);
        editor.commit();

    }
}
