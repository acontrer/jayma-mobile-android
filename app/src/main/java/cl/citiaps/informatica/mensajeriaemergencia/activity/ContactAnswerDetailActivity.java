package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;

public class ContactAnswerDetailActivity extends AppCompatActivity {

    private MapFragment mapFragment;
    private GoogleMap map;

    Double latitude;
    Double longitude;

    TextView fullNameTextView;
    TextView stateTextView;
    TextView fullAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_answer_detail);

        Intent contactAnswerListIntent = getIntent();
        String fullName = contactAnswerListIntent.getStringExtra(Constants.EXTRA_FULL_NAME);
        Boolean isOK = contactAnswerListIntent.getBooleanExtra(Constants.EXTRA_CONTACT_IS_OK, false);
        Date date = new Date();
        date.setTime(contactAnswerListIntent.getLongExtra(Constants.EXTRA_DATE, -1));
        String fullAddress = contactAnswerListIntent.getStringExtra(Constants.EXTRA_FULL_ADDRESS);
        latitude = contactAnswerListIntent.getDoubleExtra(Constants.EXTRA_LATITUDE, 0);
        longitude = contactAnswerListIntent.getDoubleExtra(Constants.EXTRA_LONGITUDE, 0);

        fullNameTextView = (TextView) findViewById(R.id.full_name_contact_answer_detail_text);
        fullNameTextView.setText(fullName);

        stateTextView = (TextView) findViewById(R.id.state_contact_answer_detail_text);
        stateTextView.setText(formatState(fullName, isOK, date));

        fullAddressTextView = (TextView) findViewById(R.id.full_address_contact_answer_detail_text);
        fullAddressTextView.setText(fullAddress);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_contact_answer_detail);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        }

    }

    void loadMap(GoogleMap googleMap){

        map = googleMap;
        if (map != null) {
            Log.d("MAPA", "yey se inicializó el mapa");
            /*LatLngBounds missionAreaBounds = toBounds(missionCenter, missionRadius);*/
            /*map.moveCamera(CameraUpdateFactory.newLatLngBounds(missionAreaBounds,0));*/

            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    LatLng lastAnswer = new LatLng(latitude, longitude);
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude)));

                    map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                }
            });

        } else {
            Log.d("MAPA", "Error inicializando mapa :c");
        }

    }


     String formatState(String fullName, Boolean isOK, Date date){

        String isOKString = "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
        DateFormat hoursFormat = new SimpleDateFormat("HH:mm");


        String dateString = dateFormat.format(date);
        String hoursString = hoursFormat.format(date);

        if (isOK){
            isOKString = "se encuentra bien";
        }
        else {
            isOKString = "no se encuentra bien";
        }

        String state = fullName + " indicó que se " + isOKString + " por última vez el día " +
                dateString + " a las " + hoursString;

        return state;
    }
}
