package cl.citiaps.informatica.mensajeriaemergencia.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

import cl.citiaps.informatica.mensajeriaemergencia.activity.LoginActivity;
import cl.citiaps.informatica.mensajeriaemergencia.activity.MainMenuActivity;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.LocationData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ResponseData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationUpdateService extends Service {

    private static final int UPDATE_INTERVAL = 30000;
    private static final int UPDATE_FASTEST_INTERVAL = 10000;
    private static final String THREAD_NAME = "Location_Thread";
    private static final String LOG_LOCATION = "LOCALIZACION";
    private boolean mRunning;
    Constants constants = new Constants();

    public LocationUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(LOG_LOCATION, "Servicio creado");
        mRunning = false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!mRunning){
            /*super.onStartCommand(intent, flags, startId);*/
            Log.d(LOG_LOCATION, "Servicio inicializado");
            // For each start request, send a message to start a job and deliver the
            // start ID so we know which request we're stopping when we finish the job
            mRunning = true;
            /*Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);*/
            LocationThread locationThread = new LocationThread();
            locationThread.start();

        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    class LocationThread extends Thread implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener {

        GoogleApiClient googleApiClient;
        LocationRequest locationRequest;

        public void run() {
            Log.d(LOG_LOCATION, "Empezamos el thread");
            createLocationRequest();
            buildGoogleApiClient();
            googleApiClient.connect();
        }


        protected void createLocationRequest() {
            locationRequest = new LocationRequest();

            locationRequest.setInterval(UPDATE_INTERVAL);
            locationRequest.setFastestInterval(UPDATE_FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setSmallestDisplacement(0);
            Log.d(LOG_LOCATION, "LocationRequest creado");

        }

        protected void buildGoogleApiClient() {
            Log.d(LOG_LOCATION, "inicializando API google");
            googleApiClient = new GoogleApiClient.Builder(LocationUpdateService.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        protected void startLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(LocationUpdateService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationUpdateService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d(LOG_LOCATION, "No hay permisos para obtener localización");
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
            Log.d(LOG_LOCATION, "Comenzó el sistema de localización");
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(LOG_LOCATION, "Ubicación cambió a: " + location.getLatitude() + " " +
                    location.getLongitude());
            sendLocation(location.getLatitude(), location.getLongitude());
        }

        ///
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.d(LOG_LOCATION, "Connected to GoogleApiClient");
            if (ActivityCompat.checkSelfPermission(LocationUpdateService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationUpdateService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                Log.d(LOG_LOCATION, "No hay permisos para obtener localización");
                return;
            }

            startLocationUpdates();
        }

        @Override
        public void onConnectionSuspended(int i) {

            Log.d(LOG_LOCATION, "Se suspendió la localización: " + i);
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            Log.d(LOG_LOCATION, "Falló la conexión a localización: " +
                    connectionResult.getErrorMessage());

        }

        private void sendLocation(double latitude, double longitude) {

            Context appContext = getApplicationContext();
            SharedPreferences sharedPref = appContext.getSharedPreferences(
                    constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

            int userID = sharedPref.getInt(constants.SHARED_PREFERENCES_USER_ID, 0);

            if (userID != 0){

                Log.d(LOG_LOCATION, "Voy a mandar una nueva localización al servidor");
                LocationData locationData = new LocationData(userID, latitude, longitude);

                RestService restService = RestService.retrofit.create(RestService.class);
                Call<ResponseData> call = restService.registerLocation(locationData);

                call.enqueue(new Callback<ResponseData>(){
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.code() == 500 || response.body().isError()){

                            if (response.code() == 500){
                                Log.d(LOG_LOCATION, response.errorBody().toString());
                            }
                            else {
                                Log.d(LOG_LOCATION, response.body().getError_message());
                            }
                        }

                        else{

                            Log.d(LOG_LOCATION, "Localización actualizada en el servidor");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        // something went completely south (like no internet connection)
                        Log.d(LOG_LOCATION, t.getMessage());
                    }
                });


            }

            else{
                Log.d(LOG_LOCATION, "USER ID no disponible");
            }
        }


    }


}

