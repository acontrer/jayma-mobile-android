package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.Manifest;
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

import java.util.HashSet;
import java.util.Set;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.service.SendAlertAnswerIntentService;

public class SendProblemActivity extends AppCompatActivity  {

    Set<String> problems = new HashSet<>();
    private String health_problem;
    private String panic_problem;
    private String infrastructure_problem;
    private String supplies_problem;

    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_problem);

        health_problem = getString(R.string.health_problems_text);
        panic_problem = getString(R.string.panic_problems_text);
        infrastructure_problem = getString(R.string.infrastructure_problems_text);
        supplies_problem = getString(R.string.supplies_problems_text);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(Constants.INTENT_LATITUDE, 0);
        longitude = intent.getDoubleExtra(Constants.INTENT_LONGITUDE, 0);


    }


    public void selectProblem(View view){

        Button pressedButton = (Button) view;
        pressedButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));


        String selectedProblem = pressedButton.getText().toString();

        if (health_problem.compareTo(selectedProblem) == 0){
            insertProblemOnSet(Constants.ALERT_PROBLEM_HEALTH);
        }

        else if (panic_problem.compareTo(selectedProblem) == 0){
            insertProblemOnSet(Constants.ALERT_PROBLEM_PANIC);
        }

        else if (supplies_problem.compareTo(selectedProblem) == 0){
            insertProblemOnSet(Constants.ALERT_PROBLEM_SUPPLIES);
        }

        else if (infrastructure_problem.compareTo(selectedProblem) == 0){
            insertProblemOnSet(Constants.ALERT_PROBLEM_INFRASTRUCTURE);
        }
    }

    private void insertProblemOnSet(String problem){

        if (problems.contains(problem)){
            problems.remove(problem);
        }
        else{
            problems.add(problem);
        }
        Log.d("PROBLEMS", problems.toString());
    }

    public void sendAlertAnswerWithProblems(View view){

        SendAlertAnswerIntentService sendAlertAnswerIntentService =
                new SendAlertAnswerIntentService();

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        int userID = sharedPref.getInt(Constants.SHARED_PREFERENCES_USER_ID, 0);
        String[] problemsString = problems.toArray(new String[problems.size()]);

        sendAlertAnswerIntentService.startActionSendAlertAnswer(this, userID, false, problemsString,
                latitude, longitude);
        Intent toMainMenuIntent = new Intent(this, MainMenuActivity.class);
        startActivity(toMainMenuIntent);
    }



}
