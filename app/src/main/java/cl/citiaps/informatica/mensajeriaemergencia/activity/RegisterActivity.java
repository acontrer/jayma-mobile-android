package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import cl.citiaps.informatica.mensajeriaemergencia.data.LoginData;
import cl.citiaps.informatica.mensajeriaemergencia.data.RegisterData;
import cl.citiaps.informatica.mensajeriaemergencia.data.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void register(View view) {

        final Context thisContext = view.getContext();

        EditText emailEditText = (EditText) findViewById(R.id.register_email);
        EditText passwordEditText = (EditText) findViewById(R.id.register_password);
        EditText passwordConfirmationEditText = (EditText) findViewById(
                R.id.register_password_confirmation);

        EditText firstNameEditText = (EditText) findViewById(R.id.register_first_name);
        EditText secondNameEditText = (EditText) findViewById(R.id.register_second_name_edit_text);
        EditText lastNameEditText = (EditText) findViewById(R.id.register_last_name_edit_text);
        EditText secondSurnameEditText = (EditText) findViewById(R.id.register_second_surname_edit_text);
        DatePicker birthdateDatePicker = (DatePicker) findViewById(R.id.register_birthdate_date_picker);
        EditText phoneNumberEditText = (EditText) findViewById(R.id.register_phone_number_edit_text);

        Date birthdate = yearMonthDayToDate(
                birthdateDatePicker.getYear(),
                birthdateDatePicker.getMonth(),
                birthdateDatePicker.getDayOfMonth());

        if (checkPasswords(
                passwordEditText.getText().toString(),
                passwordConfirmationEditText.getText().toString()))
        {
            RegisterData registerData = new RegisterData(
                emailEditText.getText().toString(), passwordEditText.getText().toString(),
                firstNameEditText.getText().toString(), secondNameEditText.getText().toString(),
                lastNameEditText.getText().toString(), secondSurnameEditText.getText().toString(),
                birthdate, Integer.parseInt(phoneNumberEditText.getText().toString())
            );

            RestService restService = RestService.retrofit.create(RestService.class);

            Call<LoginData> call = restService.register(registerData);
            call.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {

                    if (response.code() == 500 || response.body().isError()) {
                        // ERROR EN EL SERVIDOR //
                        Context context = getApplicationContext();
                        CharSequence text = "";
                        if (response.code() == 500) {
                            text = "Error en el servidor";
                            Log.d("register", response.errorBody().toString());
                        }
                        else{
                            text = response.body().getError_message();
                        }

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        
                    } else {

                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(getString(R.string.shared_preferences_user_id),
                                response.body().getUser_id());
                        editor.commit();


                        Intent toMainMenuIntent = new Intent(context , MainMenuActivity.class);
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

            Context context = getApplicationContext();
            CharSequence text = "Las contraseñas no coinciden";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }



    }


    public Date yearMonthDayToDate(int year, int month, int day){

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }

    public boolean checkPasswords(String password, String passwordConfirmation){

        return password.compareTo(passwordConfirmation) == 0;
    }
}
