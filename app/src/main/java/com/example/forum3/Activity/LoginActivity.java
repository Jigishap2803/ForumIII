package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements ApiCallbackCode {
    private EditText login_email_id_edt,login_password_edt;
    private TextView forget_password_tv,login_tv;
    private String userregid = "", email = "", pass = "";
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        preferenceManager = new PreferenceManager(LoginActivity.this);
        userregid = preferenceManager.getPreferenceValues( Preference_Constant.USER_ID);
        idCalling();
        click_event();
    }
    private void idCalling(){
        login_email_id_edt =  findViewById(R.id.login_email_id_edt);
        login_password_edt =  findViewById(R.id.login_password_edt);
        forget_password_tv =  findViewById(R.id.forget_password_tv);
        login_tv =  findViewById(R.id.login_tv);
    }

    private void click_event(){
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = login_email_id_edt.getText().toString().trim();
                pass = login_password_edt.getText().toString().trim();
                if (email.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this, "Please enter valid email-id", Toast.LENGTH_SHORT).show();
                } else if (pass.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();

                } else {
                    login_api("login", userregid, email, pass);

                }

            }
        });

        forget_password_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });

    }

    private void login_api(String reqAction, String userregid, String email, String pass){
        ApiUtility api = new ApiUtility(LoginActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.login_api(reqAction, userregid, email, pass);
        api.postRequest(responseCall, this, 1);

    }

    @Override
    public void onResponse (JSONObject jsonObject, int request_code, int status_code ) {
        if(jsonObject!=null){
            if(request_code == 1){
                try {
                    String requestStatus = jsonObject.getString("requestStatus");
                    if(requestStatus.equalsIgnoreCase("Success")){
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
                        preferenceManager.putPreferenceValues(Preference_Constant.IS_LOGGED_IN,"1");
                        JSONArray data = jsonObject.getJSONArray("Content");
                        JSONObject object = data.getJSONObject(0);
                        String userregid = object.getString("userregid");
                        preferenceManager.putPreferenceValues(Preference_Constant.USER_ID,userregid);
                        Intent intent = new Intent(LoginActivity.this,SampleActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(requestStatus.equalsIgnoreCase("Failed")){
                        String Error = jsonObject.getString("Error");
                        Toast.makeText(LoginActivity.this,Error,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure ( Object jsonObject, String error_msg ) {

        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(LoginActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }

}