package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
//import android.widget.LinearLayout;
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

import retrofit2.Call;
import retrofit2.Retrofit;

public class ChangePassActivity extends AppCompatActivity implements ApiCallbackCode{
    private EditText change_pass_edt, confirm_password_edt;
    private TextView change_password_tv;
    private String userregid ="", change_password ="", confirm_password = "";
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_pass);
        preferenceManager = new PreferenceManager(ChangePassActivity.this);
        userregid = preferenceManager.getPreferenceValues(Preference_Constant.USER_ID);
        change_pass_edt =  findViewById(R.id.change_pass_edt);
        confirm_password_edt =  findViewById(R.id.confirm_password_edt);
        change_password_tv = findViewById(R.id.change_password_tv);

        change_password_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_password = change_pass_edt.getText().toString().trim();
                confirm_password = confirm_password_edt.getText().toString().trim();
                if(change_password.equalsIgnoreCase("")){
                    Toast.makeText(ChangePassActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else if(confirm_password.equalsIgnoreCase("")){
                    Toast.makeText(ChangePassActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
                }else if(!confirm_password.equalsIgnoreCase(change_password)){
                    Toast.makeText(ChangePassActivity.this, "Please check, entered password does not match", Toast.LENGTH_SHORT).show();
                } else {
                   set_password_api("changepwd", userregid, change_password, confirm_password);
                }
            }
        });

    }

    private void set_password_api(String reqAction, String userregid, String newpwd, String confirmpwd){
        ApiUtility api = new ApiUtility(ChangePassActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.set_password_api(reqAction, userregid, newpwd, confirmpwd);
        api.postRequest(responseCall, this, 1);
    }

    @Override
    public void onResponse (JSONObject jsonObject, int request_code, int status_code ) {
        if(jsonObject!=null){
            if(request_code == 1){
                String msg = "Password changed successfully!";
                Toast.makeText(ChangePassActivity.this,msg,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassActivity.this,ResponseActivity.class);
                intent.putExtra("msg",msg);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onFailure ( Object jsonObject, String error_msg ) {

        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(ChangePassActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(ChangePassActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }

}