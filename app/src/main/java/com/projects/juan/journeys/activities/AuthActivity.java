package com.projects.juan.journeys.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.juan.journeys.BuildConfig;
import com.projects.juan.journeys.R;
import com.projects.juan.journeys.modules.HttpRequests;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    private static boolean logout;
    private Toolbar toolbar;

    @Override
    protected void onStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE);
        if(logout) getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE).edit().clear().commit();
        String token = sharedPreferences.getString("token", null);
        if(token != null && !token.isEmpty()){
            Intent authSuccessful = new Intent(getBaseContext(), MainActivity.class);
            startActivity(authSuccessful);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        toolbar = findViewById(R.id.journey_toolbar);
        setSupportActionBar(toolbar);

        final TextInputLayout email = findViewById(R.id.txtEmail);
        final TextInputLayout pass = findViewById(R.id.txtPass);

        final Button loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(validate(email, pass)){
                loginButton.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(AuthActivity.this,
                        R.style.dialog_light);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.Authenticating));
                progressDialog.show();

                JSONObject request_params = new JSONObject();

                try {
                    request_params.put("email", email.getEditText().getText().toString());
                    request_params.put("password", pass.getEditText().getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpRequests.postRequest(getBaseContext(), null, BuildConfig.LOGIN, request_params, getResources().getString(R.string.network_error), new HttpRequests.CallBack(){
                    @Override
                    public void sendResponse(String response) {
                        loginButton.setEnabled(true);
                        progressDialog.cancel();
                        // Mejorar esto, verificar con codigo de la respuesta
                        if(response.contains("err")){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
                        }else{
                            SharedPreferences sharedPreferences = getSharedPreferences("user_pref", getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor writer = sharedPreferences.edit();
                            try {
                                writer.putString("token", new JSONObject(response).getString("token"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            writer.commit();

                            logout = false;

                            Intent authSuccessful = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(authSuccessful);

                            finish();

                        }
                    }

                    @Override
                    public void sendFailure(String response) {

                    }
                });
            }

            }
        });

        findViewById(R.id.btnSignUpActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent authSuccessful = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(authSuccessful);
            }
        });
    }

    public static void logout(){
        logout = true;
    }

    public boolean validate(TextInputLayout edit_email, TextInputLayout edit_pass) {
        boolean valid = true;

        String email = edit_email.getEditText().getText().toString();
        String password = edit_pass.getEditText().getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Enter a valid email address");
            valid = false;
        }

        if (password.isEmpty()) {
            edit_pass.setError("Enter a valid password");
            valid = false;
        }

        return valid;
    }

    public void signUpHandler(View view){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }
}
