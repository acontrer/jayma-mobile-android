package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.receiver.RestResponseReceiver;
import cl.citiaps.informatica.mensajeriaemergencia.rest.CheckUserInEmergencyData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.LocationData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ResponseData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import cl.citiaps.informatica.mensajeriaemergencia.service.SendFirebaseTokenService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Constants constants = new Constants();
    GoogleApiClient googleApiClient = null;
    int userId;
    Button sendAlertButton;
    IntentFilter alertAnswerResponseFilter;
    RestResponseReceiver restResponseReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        sendToken();
        sendFirstLocation();
        checkUserInEmergency();
        sendAlertButton = (Button) findViewById(R.id.sendAlertButton);
        restResponseReceiver = new RestResponseReceiver();
        alertAnswerResponseFilter =
                new IntentFilter(Constants.BROADCAST_NOTIFY_ALERT_ANSWER_RESPONSE);




    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    public void toSendAlert(View view) {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound1);
        mp.start();
        Intent toSendAlertIntent = new Intent(this, SendAlertActivity.class);
        startActivity(toSendAlertIntent);
    }

    public void toCheckImportantContacts(View view){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound1);
        mp.start();
        Intent toCheckImportantContactsIntent = new Intent(this, CheckImportantContactsActivity.class);
        startActivity(toCheckImportantContactsIntent);
    }

    public void checkUserInEmergency(){

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        int userID = sharedPref.getInt(
                constants.SHARED_PREFERENCES_USER_ID, 0);

        if (userID !=0){

            new CheckUserInEmergencyTask().execute(userID);
        }
    }


    private class CheckUserInEmergencyTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... userID) {

            RestService restService = RestService.retrofit.create(RestService.class);

            Call<CheckUserInEmergencyData> call = restService.checkUserInEmergency(userID[0]);
            try {
                Response<CheckUserInEmergencyData> response  = call.execute();
                if (response.code() == 500 || response.body().isError()) {

                    if (response.code() == 500) {
                        Log.d(constants.LOG_SEND_ALERT, response.errorBody().toString());
                    }
                    else{
                        Log.d(constants.LOG_SEND_ALERT, response.body().getError_message());
                    }

                    return false;

                } else {

                    return response.body().isUser_in_active_emergency();
                }
            } catch (IOException e) {

                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

            sendAlertButton.setEnabled(result);
        }
    }


    public void sendToken() {

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        boolean tokenUpdate = sharedPref.getBoolean(
                constants.SHARED_PREFERENCES_TOKEN_UPDATE, false);

        boolean newUserLogin = sharedPref.getBoolean(
                constants.SHARED_PREFERENCES_NEW_USER_LOGIN, false);
        if (tokenUpdate || newUserLogin) {

            int userID = sharedPref.getInt(constants.SHARED_PREFERENCES_USER_ID, 0);
            String token = sharedPref.getString(constants.SHARED_PREFERENCES_TOKEN, "");

            Intent sendTokenIntent = new Intent(MainMenuActivity.this, SendFirebaseTokenService.class);
            sendTokenIntent.putExtra(constants.INTENT_TOKEN, token);
            sendTokenIntent.putExtra(constants.INTENT_USER_ID, userID);

            MainMenuActivity.this.startService(sendTokenIntent);
        }

    }

    public void sendFirstLocation() {

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        boolean firstLocation = sharedPref.getBoolean(
                constants.SHARED_PREFERENCES_TOKEN_UPDATE, true);
        userId = sharedPref.getInt(
                constants.SHARED_PREFERENCES_USER_ID, 0);

        if (firstLocation) {

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("LOCATION", "No hay permisos de localización suficientes");
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null && userId != 0) {

            LocationData locationData = new LocationData(
                    userId, lastLocation.getLatitude(), lastLocation.getLongitude());

            RestService restService = RestService.retrofit.create(RestService.class);
            Call<ResponseData> call = restService.registerLocation(locationData);

            call.enqueue(new Callback<ResponseData>(){
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.code() == 500 || response.body().isError()){

                        if (response.code() == 500){
                            Log.d(constants.LOG_LOCATION, response.errorBody().toString());
                        }
                        else {
                            Log.d(constants.LOG_LOCATION, response.body().getError_message());
                        }
                    }

                    else{

                        Log.d(constants.LOG_LOCATION, "Localización actualizada en el servidor");
                        Context appContext = getApplicationContext();
                        SharedPreferences sharedPref = appContext.getSharedPreferences(
                                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

                        boolean firstLocation = sharedPref.getBoolean(
                                constants.SHARED_PREFERENCES_FIRST_LOCATION, true);

                        if(firstLocation){

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(constants.SHARED_PREFERENCES_FIRST_LOCATION,
                                    false);
                            editor.commit();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    // something went completely south (like no internet connection)
                    Log.d(constants.LOG_LOCATION, t.getMessage());
                }
            });

        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(constants.LOG_LOCATION,connectionResult.getErrorMessage());
    }

    public void toSetImportanContacts(View view){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound1);
        mp.start();
        Intent toSetImportanContactsIntent = new Intent(this, SetImportantContactsActivity.class);
        startActivity(toSetImportanContactsIntent);
    }


}



