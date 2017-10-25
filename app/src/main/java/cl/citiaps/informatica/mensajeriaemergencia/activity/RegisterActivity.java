package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.LoginData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RegisterData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Constants constants = new Constants();
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordConfirmationEditText;
    EditText firstNameEditText;
    EditText secondNameEditText;
    EditText lastNameEditText;
    EditText secondSurnameEditText;
    DatePicker birthdateDatePicker;
    EditText phoneNumberEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void register(View view) {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound1);
        mp.start();
        final Context thisContext = view.getContext();

        emailEditText = (EditText) findViewById(R.id.register_email);
        passwordEditText = (EditText) findViewById(R.id.register_password);
        passwordConfirmationEditText = (EditText) findViewById(
                R.id.register_password_confirmation);

        firstNameEditText = (EditText) findViewById(R.id.register_first_name);
        secondNameEditText = (EditText) findViewById(R.id.register_second_name_edit_text);
        lastNameEditText = (EditText) findViewById(R.id.register_last_name_edit_text);
        secondSurnameEditText = (EditText) findViewById(R.id.register_second_surname_edit_text);
        birthdateDatePicker = (DatePicker) findViewById(R.id.register_birthdate_date_picker);
        phoneNumberEditText = (EditText) findViewById(R.id.register_phone_number_edit_text);

        Date birthdate = yearMonthDayToDate(
                birthdateDatePicker.getYear(),
                birthdateDatePicker.getMonth(),
                birthdateDatePicker.getDayOfMonth());

        if (checkPasswords(
                passwordEditText.getText().toString(),
                passwordConfirmationEditText.getText().toString()) && checkFields())
        {
            RegisterData registerData = new RegisterData(
                emailEditText.getText().toString(), passwordEditText.getText().toString(),
                firstNameEditText.getText().toString(), secondNameEditText.getText().toString(),
                lastNameEditText.getText().toString(), secondSurnameEditText.getText().toString(),
                birthdate, Integer.parseInt(phoneNumberEditText.getText().toString())
            );

            RestService restService = RestService.retrofit.create(RestService.class);

            Call<LoginData> call = restService.registerUser(registerData);
            call.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {

                    if (response.code() == 500 || response.code() == 404 || response.body().isError()) {
                        // ERROR EN EL SERVIDOR //
                        Context context = getApplicationContext();
                        CharSequence text = "";
                        if (response.code() == 500 || response.code() == 404) {
                            text = "Error en el servidor";
                            Log.d("registerUser", response.errorBody().toString());
                        }
                        else{
                            text = response.body().getError_message();
                        }

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    } else {

                        Context appContext = getApplicationContext();
                        SharedPreferences sharedPref = appContext.getSharedPreferences(
                                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(constants.SHARED_PREFERENCES_USER_ID,
                                response.body().getUser_id());
                        editor.commit();


                        Intent toMainMenuIntent = new Intent(RegisterActivity.this , MainMenuActivity.class);
                        startActivity(toMainMenuIntent);
                    }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {

                /*Log.d("LOGIN", t.getLocalizedMessage());*/

                    Context context = getApplicationContext();
                    CharSequence text = "Error al enviar registro";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            });
        }
        else{


        }



    }

    public boolean checkFields(){
        String campoRequerido = "Este campo es requerido";
        emailEditText = (EditText) findViewById(R.id.register_email);
        passwordEditText = (EditText) findViewById(R.id.register_password);
        passwordConfirmationEditText = (EditText) findViewById(
                R.id.register_password_confirmation);

        firstNameEditText = (EditText) findViewById(R.id.register_first_name);
        secondNameEditText = (EditText) findViewById(R.id.register_second_name_edit_text);
        lastNameEditText = (EditText) findViewById(R.id.register_last_name_edit_text);
        secondSurnameEditText = (EditText) findViewById(R.id.register_second_surname_edit_text);
        birthdateDatePicker = (DatePicker) findViewById(R.id.register_birthdate_date_picker);
        phoneNumberEditText = (EditText) findViewById(R.id.register_phone_number_edit_text);

        if(emailEditText.getText().toString().matches("")){
            emailEditText.setError(campoRequerido);
            return false;
        }
        if(passwordEditText.getText().toString().matches("")){
            passwordEditText.setError(campoRequerido);
            return false;
        }
        if(passwordConfirmationEditText.getText().toString().matches("")){
            passwordConfirmationEditText.setError(campoRequerido);
            return false;
        }
        if(firstNameEditText.getText().toString().matches("")){
            firstNameEditText.setError(campoRequerido);
            return false;
        }
        if(lastNameEditText.getText().toString().matches("")){
            lastNameEditText.setError(campoRequerido);
            return false;
        }
        if(phoneNumberEditText.getText().toString().matches("")){
            phoneNumberEditText.setError(campoRequerido);
            return false;
        }
        return true;
    }


    public Date yearMonthDayToDate(int year, int month, int day){

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }

    public boolean checkPasswords(String password, String passwordConfirmation){
        if(password.compareTo(passwordConfirmation) != 0){
            Context context = getApplicationContext();
            CharSequence text = "Las contraseñas no coinciden";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        return password.compareTo(passwordConfirmation) == 0;
    }
}
