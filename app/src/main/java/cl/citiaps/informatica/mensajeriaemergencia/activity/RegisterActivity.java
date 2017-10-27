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
    EditText facebook;


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
        facebook = (EditText)findViewById(R.id.facebook);


        String birthdate = dateParser(birthdateDatePicker);

        if (checkPasswords(
                passwordEditText.getText().toString(),
                passwordConfirmationEditText.getText().toString()) && checkFields())
        {
            RegisterData registerData = new RegisterData(
                emailEditText.getText().toString(), passwordEditText.getText().toString(),
                firstNameEditText.getText().toString(), secondNameEditText.getText().toString(),
                lastNameEditText.getText().toString(), secondSurnameEditText.getText().toString(),
                birthdate, String.valueOf(phoneNumberEditText),facebook.getText().toString());


            RestService restService = RestService.service.create(RestService.class);

            Call<RegisterData> call = restService.registerUser(registerData);
            call.enqueue(new Callback<RegisterData>() {
                @Override
                public void onResponse(Call<RegisterData> call, Response<RegisterData> response) {

                    if (response.code() == 500 || response.code() == 404 ) {
                        // ERROR EN EL SERVIDOR //
                        Context context = getApplicationContext();
                        CharSequence text = "";
                        if (response.code() == 500 || response.code() == 404) {
                            text = "Error en el servidor";
                            Log.d("registerUser", response.errorBody().toString());
                        }


                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    } else {

                        Context appContext = getApplicationContext();
                        SharedPreferences sharedPref = appContext.getSharedPreferences(
                                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.apply();


                        Intent toMainMenuIntent = new Intent(RegisterActivity.this , LoginActivity.class);
                        Toast.makeText(getApplicationContext(),"Registrado con éxito",Toast.LENGTH_LONG).show();
                        startActivity(toMainMenuIntent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<RegisterData> call, Throwable t) {

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


    public String dateParser(DatePicker datePicker){

        return String.valueOf(datePicker.getYear())+"-"+String.valueOf(datePicker.getMonth())+"-"
                +String.valueOf(datePicker.getDayOfMonth()+"T00:00:00Z");
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
