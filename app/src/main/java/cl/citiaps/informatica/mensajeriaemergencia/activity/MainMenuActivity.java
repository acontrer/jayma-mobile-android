package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.service.SendFirebaseTokenService;

public class MainMenuActivity extends AppCompatActivity {

    Constants constants = new Constants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        sendToken();



    }

    public void sendToken(){

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        boolean tokenUpdate = sharedPref.getBoolean(
                constants.SHARED_PREFERENCES_TOKEN_UPDATE, false);

        if (tokenUpdate){

            int userID = sharedPref.getInt(constants.SHARED_PREFERENCES_USER_ID, 0);

            String token = sharedPref.getString(constants.SHARED_PREFERENCES_TOKEN, "");

            Intent sendTokenIntent = new Intent(MainMenuActivity.this, SendFirebaseTokenService.class);
            sendTokenIntent.putExtra(constants.INTENT_TOKEN, token);
            sendTokenIntent.putExtra(constants.INTENT_USER_ID, userID);

            MainMenuActivity.this.startService(sendTokenIntent);
        }

    }
}
