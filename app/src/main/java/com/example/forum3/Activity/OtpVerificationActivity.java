package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.Preference.PreferenceManager;
import com.example.forum3.Preference.Preference_Constant;
import com.example.forum3.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Retrofit;

public class OtpVerificationActivity extends AppCompatActivity implements ApiCallbackCode {
    private EditText otp_edt;
    private TextView timer_tv, resend_email_tv, otp_verify_tv;
    private LinearLayout resend_otp_ll;
    private String email_id = "", otp = "",userregid = "", type = "";
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_verification);
        preferenceManager = new PreferenceManager(OtpVerificationActivity.this);
        userregid = preferenceManager.getPreferenceValues(Preference_Constant.USER_ID);
        Intent intent = getIntent();
        email_id = intent.getStringExtra("email_id");
        type = intent.getStringExtra("type");
        idCalling();
        click_event();
    }
    private void idCalling(){
        otp_edt = (EditText) findViewById(R.id.otp_edt);
        timer_tv = (TextView) findViewById(R.id.timer_tv);
        resend_email_tv = (TextView) findViewById(R.id.resend_email_tv);
        otp_verify_tv = (TextView) findViewById(R.id.otp_verify_tv);
        resend_otp_ll = (LinearLayout) findViewById(R.id.resend_otp_ll);
    }

    private void click_event(){
        otp_verify_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = otp_edt.getText().toString().trim();
                if (otp.equalsIgnoreCase("")){
                    Toast.makeText(OtpVerificationActivity.this, "Please enter 6 digit OTP", Toast.LENGTH_SHORT).show();
                } else {
                    send_otp_api("userchkotp", userregid, otp);
                }
            }
        });

        resend_email_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resent_otp_api("forgotpwd", email_id);
            }
        });
    }

    private void send_otp_api(String reqAction, String userregid, String otp){
        ApiUtility api = new ApiUtility(OtpVerificationActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.send_otp_api(reqAction, userregid, otp);
        api.postRequest(responseCall, (ApiCallbackCode) this, 1);


    }

    private void resent_otp_api(String reqAction, String email){
        ApiUtility api = new ApiUtility(OtpVerificationActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.send_otp_api(reqAction, userregid, otp);
        api.postRequest(responseCall, (ApiCallbackCode) this, 2);
    }

    @Override
    public void onResponse (JSONObject jsonObject, int request_code, int status_code ) {
        if(jsonObject!=null){
            if(request_code == 1){
                try {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(OtpVerificationActivity.this,msg,Toast.LENGTH_SHORT).show();
                    if(type.equalsIgnoreCase("fp")){
                        Intent intent = new Intent(OtpVerificationActivity.this,ChangePassActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(type.equalsIgnoreCase("reg")){
                        Intent intent = new Intent(OtpVerificationActivity.this,ResponseActivity.class);
                        intent.putExtra("msg",msg);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(request_code == 2){
                try {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(OtpVerificationActivity.this,msg,Toast.LENGTH_SHORT).show();
                    setTimer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure ( Object jsonObject, String error_msg ) {

        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(OtpVerificationActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(OtpVerificationActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void setTimer(){
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timer_tv.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                timer_tv.setText("00:00:00");
                resend_otp_ll.setVisibility(View.VISIBLE);

            }
        }.start();
    }

}