package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.service.SendAlertAnswerIntentService;

public class SendAlertActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient = null;
    private double latitude;
    private double longitude;
    private Button answerYesButton;
    private Button answerNoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alert);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

        answerYesButton = (Button) findViewById(R.id.alert_answer_yes_button);
        answerNoButton = (Button) findViewById(R.id.alert_answer_no_button);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    public void toSendProblemActivity(View view){

        Intent toSendProblemIntent = new Intent(this, SendProblemActivity.class);
        toSendProblemIntent.putExtra(Constants.INTENT_LATITUDE, latitude);
        toSendProblemIntent.putExtra(Constants.INTENT_LONGITUDE, longitude);
        startActivity(toSendProblemIntent);
    }

    public void sendAlertAnswerOK(View view){

        SendAlertAnswerIntentService sendAlertAnswerIntentService =
                new SendAlertAnswerIntentService();

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        int userID = sharedPref.getInt(Constants.SHARED_PREFERENCES_USER_ID, 0);
        String[] problems = new String[0];

        sendAlertAnswerIntentService.startActionSendAlertAnswer(this, userID, true, problems,
                latitude, longitude);

        Intent toMainMenuIntent = new Intent(this, MainMenuActivity.class);
        startActivity(toMainMenuIntent);
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
        latitude = lastLocation.getLatitude();
        longitude = lastLocation.getLongitude();
        answerNoButton.setEnabled(true);
        answerYesButton.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(Constants.LOG_LOCATION,connectionResult.getErrorMessage());
    }
}
