package cl.citiaps.informatica.mensajeriaemergencia.constants;

/**
 * Created by kayjt on 25-11-2016.
 */

public class Constants {

    // INTENTS
    public static final String INTENT_USER_ID = "intentUserID";
    public static final String INTENT_TOKEN = "intentToken";
    public static final String INTENT_LATITUDE = "intentLatitude";
    public static final String INTENT_LONGITUDE = "intentlongitude";

    // LOG TAGS
    public static final String LOG_TOKEN = "TOKEN";
    public static final String LOG_NOTIFICATION = "NOTIFICACION";
    public static final String LOG_LOCATION = "LOCALIZACION";
    public static final String LOG_SEND_ALERT = "ENVIO ALERTA";
    public static final String LOG_CONTACTS = "CONTACTOS";

    // SHARED PREFERENCES //

    public static final String SHARED_PREFERENCES_FILE = "cl.citiaps.mensajeriaemergencia.SHARED_PREFERENCES_FILE";
    public static final String SHARED_PREFERENCES_TOKEN_UPDATE = "SHARED_PREFERENCES_TOKEN_UPDATE";
    public static final String SHARED_PREFERENCES_TOKEN= "SHARED_PREFERENCES_TOKEN";
    public static final String SHARED_PREFERENCES_USER_ID = "SHARED_PREFERENCES_USER_ID";
    public static final String SHARED_PREFERENCES_FIRST_LOCATION = "SHARED_PREFERENCES_FIRST_LOCATION";
    public static final String SHARED_PREFERENCES_NEW_USER_LOGIN = "SHARED_PREFERENCES_NEW_USER_LOGIN";


    // BROADCAST
    public static final String BROADCAST_NOTIFY_ALERT_ANSWER_RESPONSE =
            "cl.citiaps.mensajeriaemergencia.BROADCAST_NOTIFY_ALERT_ANSWER_RESPONSE";
    public static final String EXTENDED_ALERT_ANSWER_RESPONSE =
            "cl.citiaps.mensajeriaemergencia.EXTENDED_ALERT_ANSWER_RESPONSE";

    // ALERT PROBLEMS
    public static final String ALERT_PROBLEM_HEALTH = "ALERT_PROBLEM_HEALTH";
    public static final String ALERT_PROBLEM_SUPPLIES = "ALERT_PROBLEM_SUPPLIES";
    public static final String ALERT_PROBLEM_PANIC = "ALERT_PROBLEM_PANIC";
    public static final String ALERT_PROBLEM_INFRASTRUCTURE = "ALERT_PROBLEM_INFRASTRUCTURE";

    // BUTTONS_TAG

    public static final int IS_IMPORTANT_BUTTON_TAG = 1;

    // EXTRA INTENT

    public static final String EXTRA_CONTACT_IS_OK = "EXTRA_CONTACT_IS_OK";
    public static final String EXTRA_FIRST_NAME = "EXTRA_FIRST_NAME";
    public static final String EXTRA_SECOND_NAME = "EXTRA_SECOND_NAME";
    public static final String EXTRA_FULL_NAME = "EXTRA_FULL_NAME";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    public static final String EXTRA_FULL_ADDRESS = "EXTRA_FULL_ADDRESS";


}
