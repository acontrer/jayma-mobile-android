package cl.citiaps.informatica.mensajeriaemergencia.rest;

import android.view.ViewDebug;

import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kayjt on 21-11-2016.
 */

public interface RestService {

    String API_URL = "https://fondef-mensajeria-citiaps.herokuapp.com/mobile/";
    String API = "http://158.170.140.28:3000/";

    /// Integrados
    @POST("login")
    Call<LoginData> login(@Body LoginData loginData);



    /// Faltantes



    @POST("usuario/")
    Call<RegisterData> registerUser(@Body RegisterData registerData);

    @POST("devices/")
    Call<DeviceData> registerDevice(@Body DeviceData deviceData);

    @POST("positions/")
    Call<ResponseData> registerLocation(@Body LocationData locationData);

    @GET("user_on_active_emergency/{user_id}")
    Call<CheckUserInEmergencyData> checkUserInEmergency(@Path("user_id") int userId);

    @POST("alert_answer/")
    Call<ResponseData> alertAnswer(@Body AlertAnswerData alertAnswerData);

    @GET("check_important_contacts/")
    Call<CheckImportantContactData> checkImportantContacts(@Query("phone_numbers") ArrayList<String> phoneNumbers, @Query("user_id") int userId);

    @POST("set_important_contact/")
    Call<ResponseData> setImportantContact(@Body SetImportantContactData setImportantContactData);

    @GET("last_answers_important_contacts/{user_id}")
    Call<ImportantContactInEmergencyData> lastAnswersImportantContacts(@Path("user_id") int userId);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit service = new Retrofit.Builder()
            .baseUrl(API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
