package cl.citiaps.informatica.mensajeriaemergencia.rest;

import com.google.android.gms.location.LocationRequest;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kayjt on 21-11-2016.
 */

public interface RestService {

    String API_URL = "http://192.168.0.104:3000/mobile/";

    @POST("login/")
    Call<LoginData> login(@Body LoginData loginData);

    @POST("register/")
    Call<LoginData> registerUser(@Body RegisterData registerData);

    @POST("devices/")
    Call<DeviceData> registerDevice(@Body DeviceData deviceData);

    @POST("positions/")
    Call<ResponseData> registerLocation(@Body LocationData locationData);

    @GET("user_on_active_emergency/{user_id}")
    Call<CheckUserInEmergencyData> checkUserInEmergency(@Path("user_id") int userId);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
