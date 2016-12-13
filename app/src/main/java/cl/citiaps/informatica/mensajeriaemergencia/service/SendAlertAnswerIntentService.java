package cl.citiaps.informatica.mensajeriaemergencia.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.AlertAnswerData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ResponseData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendAlertAnswerIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    Constants constants;
    private static final String ACTION_SEND_ALERT_ANSWER =
            "cl.citiaps.informatica.mensajeriaemergencia.service.action.SEND_ALERT_ANSWER";
    private static final String EXTRA_USER_ID =
            "cl.citiaps.informatica.mensajeriaemergencia.service.extra.USER_ID";
    private static final String EXTRA_IS_OK =
            "cl.citiaps.informatica.mensajeriaemergencia.service.extra.IS_OK";
    private static final String EXTRA_PROBLEMS =
            "cl.citiaps.informatica.mensajeriaemergencia.service.extra.PROBLEMS";
    private static final String EXTRA_LATITUDE =
            "cl.citiaps.informatica.mensajeriaemergencia.service.extra.LATITUDE";
    private static final String EXTRA_LONGITUDE =
            "cl.citiaps.informatica.mensajeriaemergencia.service.extra.LONGITUDE";
    private static final String LOG_ALERT_ANSWER = "RESPUESTA_ALERTA";

    GoogleApiClient googleApiClient = null;

    public SendAlertAnswerIntentService() {
        super("SendAlertAnswerIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSendAlertAnswer(
            Context context, int userID, boolean isOK, String[] problems, Double latitude,
            Double longitude) {
        Intent intent = new Intent(context, SendAlertAnswerIntentService.class);
        intent.setAction(ACTION_SEND_ALERT_ANSWER);
        intent.putExtra(EXTRA_USER_ID, userID);
        intent.putExtra(EXTRA_IS_OK, isOK);
        intent.putExtra(EXTRA_PROBLEMS, problems);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_ALERT_ANSWER.equals(action)) {
                final int userID = intent.getIntExtra(EXTRA_USER_ID, 0);
                final boolean isOK = intent.getBooleanExtra(EXTRA_IS_OK, false);
                final String[] problems = intent.getStringArrayExtra(EXTRA_PROBLEMS);
                final Double latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0);
                final Double longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0);

                if (userID != 0){
                    handleActionSendAlertAnswer(userID, isOK, problems, latitude, longitude);
                }

            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSendAlertAnswer(int userID, boolean isOK, String[] problems,
                                             Double latitude, Double longitude) {

        AlertAnswerData alertAnswerData = new AlertAnswerData(userID, isOK, problems,
                latitude, longitude);

        RestService restService = RestService.retrofit.create(RestService.class);
        Call<ResponseData> call = restService.alertAnswer(alertAnswerData);

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.code() == 500 || response.body().isError()) {
                    alertAnswerResponseBroadcast(false);
                    if (response.code() == 500) {
                        Log.d(LOG_ALERT_ANSWER, response.errorBody().toString());
                    } else {
                        Log.d(LOG_ALERT_ANSWER, response.body().getError_message());
                    }
                } else {

                    Log.d(LOG_ALERT_ANSWER, "Respuesta de la alerta enviada al servidor");

                    alertAnswerResponseBroadcast(true);
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d(LOG_ALERT_ANSWER, t.getMessage());
                alertAnswerResponseBroadcast(false);
            }
        });

    }

    private void alertAnswerResponseBroadcast(boolean response){

        Intent localIntent =
                new Intent(constants.BROADCAST_NOTIFY_ALERT_ANSWER_RESPONSE)
                        .putExtra(constants.EXTENDED_ALERT_ANSWER_RESPONSE, response);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(SendAlertAnswerIntentService.this)
                .sendBroadcast(localIntent);
    }

}

