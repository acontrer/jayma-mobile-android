package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.LoginData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import cl.citiaps.informatica.mensajeriaemergencia.service.LocationUpdateService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent locationIntent = new Intent(this, LocationUpdateService.class);
        startService(locationIntent);

    }

    Constants constants = new Constants();

    public void login(View view) {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound1);
        mp.start();
        if(checkFields()) {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cargando perfil...");
            progressDialog.show();

            emailEditText = (EditText) findViewById(R.id.login_email_editText);
            passwordEditText = (EditText) findViewById(R.id.login_password_editText);

            LoginData loginData = new LoginData(
                    emailEditText.getText().toString() , passwordEditText.getText().toString());

            RestService restService = RestService.service.create(RestService.class);

            Call<LoginData> call = restService.login(loginData);
            call.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {

                    if (response.code() == 500 ) {
                        // ERROR EN EL SERVIDOR //
                        Context context = getApplicationContext();
                        CharSequence text;
                        if (response.code() == 500) {
                            text = "Error en el servidor";
                            Log.d("registerUser", response.errorBody().toString());
                        } else {
                            text = "";
                            if(response.body().getCode() != null){
                                if(response.body().getCode() == 401){
                                    text = "Correo o contraseña incorrectos";
                                }
                            }

                        }

                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    } else {

                        saveUserIdSharedPreferences(response.body().getToken());
                        Intent toMainMenuIntent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        startActivity(toMainMenuIntent);
                        finish();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {

                /*Log.d("LOGIN", t.getLocalizedMessage());*/

                    Context context = getApplicationContext();
                    CharSequence text = "Error al ingresar";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public boolean checkFields(){
        emailEditText = (EditText) findViewById(R.id.login_email_editText);
        passwordEditText = (EditText) findViewById(R.id.login_password_editText);

        if(emailEditText.getText().toString().matches("")){
            emailEditText.setError("Correo es requerido");
            return false;
        }
        if(passwordEditText.getText().toString().matches("")){
            passwordEditText.setError("Contraseña es requerida");
            return false;
        }
        return true;
    }

    public void toRegister(View view){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound1);
        mp.start();
        Intent toRegisterIntent = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterIntent);
    }

    public void saveUserIdSharedPreferences(String token){

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.putBoolean(constants.SHARED_PREFERENCES_NEW_USER_LOGIN, true);

        editor.apply();
    }
}
