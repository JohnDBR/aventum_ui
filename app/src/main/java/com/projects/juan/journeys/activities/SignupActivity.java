package com.projects.juan.journeys.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.projects.juan.journeys.BuildConfig;
import com.projects.juan.journeys.R;
import com.projects.juan.journeys.modules.HttpRequests;
import com.projects.juan.journeys.modules.SmsBroadcastReceiver;
import com.projects.juan.journeys.modules.SmsCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class SignupActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 0;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    de.hdodenhof.circleimageview.CircleImageView img_profile_pic;
    File img_profile_file;
    TextInputLayout first_name;
    TextInputLayout last_name;
    TextInputLayout cc;
    TextInputLayout email;
    TextInputLayout phone;
    TextInputLayout password;
    Button loginButton;

    String tempImg;
    private Toolbar toolbar;

    @Override
    protected void onDestroy() {
        unregisterReceiver(smsBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_signup);
        if (!hasReadSmsPermission()) {
            requestReadAndSendSmsPermission();
        }
        img_profile_pic = findViewById(R.id.img_profile_pic);
        first_name = findViewById(R.id.txtFirstNameLayout);
        last_name = findViewById(R.id.txtLastNameLayout);
        cc = findViewById(R.id.txtCCLayout);
        email = findViewById(R.id.txtEmailLayout);
        phone = findViewById(R.id.txtPhoneLayout);
        password = findViewById(R.id.txtPasswordLayout);

        loginButton = findViewById(R.id.btnLogin);
        tempImg = "";
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }


    public void galleryHandler(View view){
        EasyImage.openChooserWithGallery(this, getResources().getString(R.string.select_a_picture), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {

            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                try {
                    File file = new Compressor(SignupActivity.this).compressToFile(imageFiles.get(0));
                    Bitmap image = BitmapFactory.decodeFile(new Compressor(SignupActivity.this).compressToFile(imageFiles.get(0)).getPath());
                    Bitmap img = Bitmap.createScaledBitmap(image, 400, 400, true);
                    img_profile_file = file;
                    img_profile_pic.setImageBitmap(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    public void handleSignUp(View view){
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.dialog_light);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if(validate(first_name, last_name, cc, email, phone, password)){
            numberVerification(phone.getEditText().getText().toString(), new CallBack() {
                @Override
                public void messageSent(final String code) {
                    smsBroadcastReceiver.getSmsCode().setListener(new SmsCode.ChangeListener() {
                        @Override
                        public void onChange(String sms_code) {
                            if(code.equals(sms_code)) {
                                signUpRequets(progressDialog);
                            }else{
                                progressDialog.cancel();
                                loginButton.setEnabled(true);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.number_verification_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    public boolean validate(TextInputLayout first_name, TextInputLayout last_name, TextInputLayout cc, TextInputLayout email, TextInputLayout phone, TextInputLayout password) {
        boolean valid = true;

        if (first_name.getEditText().getText().toString().isEmpty()) {
            first_name.setError(getResources().getString(R.string.first_name_validation));
            valid = false;
        }

        if (last_name.getEditText().getText().toString().isEmpty()) {
            last_name.setError(getResources().getString(R.string.last_name_validation));
            valid = false;
        }

        if (cc.getEditText().getText().toString().isEmpty()) {
            cc.setError(getResources().getString(R.string.cc_validation));
            valid = false;
        }

        if (email.getEditText().getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.email_address_validation));
            valid = false;
        }

        if (phone.getEditText().getText().toString().isEmpty()) {
            phone.setError(getResources().getString(R.string.phone_number_validation));
            valid = false;
        }

        if (password.getEditText().getText().toString().isEmpty()) {
            password.setError(getResources().getString(R.string.password_validation));
            valid = false;
        }

        return valid;
    }
    private void signUpRequets(final ProgressDialog progressDialog){
        RequestParams rp = new RequestParams();
        rp.put("first_name", first_name.getEditText().getText().toString());
        rp.put("last_name", last_name.getEditText().getText().toString());
        rp.put("cc", cc.getEditText().getText().toString());
        rp.put("email", email.getEditText().getText().toString());
        rp.put("phone", phone.getEditText().getText().toString());
        rp.put("password", password.getEditText().getText().toString());
        try {
            rp.put("profile_picture", img_profile_file, "image/jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpRequests.postMultipartRequest(SignupActivity.this, null, BuildConfig.GET_STUDENTS, rp, "Sign up error", new HttpRequests.CallBack() {
            @Override
            public void sendResponse(String response) {
                try {
                    JSONObject sign_up_response = new JSONObject(response);
                    Log.d("response", response);
                    if(response.contains("id")){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_created), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), sign_up_response.getString(getResources().getString(R.string.network_error)), Toast.LENGTH_LONG).show();
                    }
                    progressDialog.cancel();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void sendFailure(String response) {

            }
        });
    }

    private void numberVerification(String phone, final CallBack callBack){
        final String code = String.format("%04d", new Random().nextInt(10000));
        JSONObject params = new JSONObject();
        try {
            params.put("email", "nomitdevelopment@gmail.com");
            params.put("password", "n0m1td3v");
            params.put("device", "78344");
            params.put("number", phone);
            params.put("message", "Journeys code: " + code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequests.postRequest(getApplicationContext(), null, BuildConfig.SMS_GATEWAY, params, getResources().getString(R.string.sms_gateway_error), new HttpRequests.CallBack() {
            @Override
            public void sendResponse(String response) {
                callBack.messageSent(code);
            }

            @Override
            public void sendFailure(String response) {

            }
        });
    }

    public interface CallBack {
        void messageSent(String code);
    }

    /**
     * Runtime permission shenanigans
     */
    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(SignupActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(SignupActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.READ_SMS)) {
            Log.d("SmsPermission", "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_CODE);
    }
}
