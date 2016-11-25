package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    Constants constants = new Constants();

    public void login(View view) {

        EditText emailEditText = (EditText) findViewById(R.id.login_email_editText);
        EditText passwordEditText = (EditText) findViewById(R.id.login_password_editText);

        LoginData loginData = new LoginData(
                emailEditText.getText().toString() , passwordEditText.getText().toString());

        RestService restService = RestService.retrofit.create(RestService.class);

        Call<LoginData> call = restService.login(loginData);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {

                if (response.code() == 500 || response.body().isError()) {
                    // ERROR EN EL SERVIDOR //
                    Context context = getApplicationContext();
                    CharSequence text = "";
                    if (response.code() == 500) {
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

                    saveUserIdSharedPreferences(response.body().getUser_id());
                    Intent toMainMenuIntent = new Intent(LoginActivity.this , MainMenuActivity.class);
                    startActivity(toMainMenuIntent);
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {

                /*Log.d("LOGIN", t.getLocalizedMessage());*/

                Context context = getApplicationContext();
                CharSequence text = "Error al ingresar";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });
    }

    public void toRegister(View view){

        Intent toRegisterIntent = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterIntent);
    }

    public void saveUserIdSharedPreferences(int userID){

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(constants.SHARED_PREFERENCES_USER_ID, userID);
        editor.commit();
    }
}
