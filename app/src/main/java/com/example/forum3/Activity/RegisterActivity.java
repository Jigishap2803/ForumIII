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

public class RegisterActivity extends AppCompatActivity implements ApiCallbackCode{
    private EditText reg_first_name_edt,reg_email_id_edt,reg_address_edt,reg_password_edt;
//            reg_re_enter_pass_edt, ;
    private TextView reg_submit_tv;
    private String name = "", email_id = "", password = "", address = "";
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        preferenceManager = new PreferenceManager(RegisterActivity.this);
        id_calling();
        click_event();
    }

    private void id_calling(){
        reg_first_name_edt = findViewById(R.id.reg_first_name_edt);
//        reg_last_name_edt = (EditText) findViewById(R.id.reg_last_name_edt);
        reg_email_id_edt =  findViewById(R.id.reg_email_id_edt);
        reg_password_edt = findViewById(R.id.reg_password_edt);
//        reg_re_enter_pass_edt =  findViewById(R.id.reg_re_enter_pass_edt);
        reg_address_edt =  findViewById(R.id.reg_address_edt);
        reg_submit_tv =  findViewById(R.id.reg_submit_tv);
    }

    private void click_event(){
        reg_submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = reg_first_name_edt.getText().toString().trim();
                email_id = reg_email_id_edt.getText().toString().trim();
                address = reg_address_edt.getText().toString().trim();
                password = reg_password_edt.getText().toString().trim();

                if(name.equalsIgnoreCase("")){
                    Toast.makeText(RegisterActivity.this, "Enter full name", Toast.LENGTH_SHORT).show();
                } else if(email_id.equalsIgnoreCase("")){
                    Toast.makeText(RegisterActivity.this, "Enter valid email-id", Toast.LENGTH_SHORT).show();
                } else if(address.equalsIgnoreCase("")){
                    Toast.makeText(RegisterActivity.this, "Enter full address", Toast.LENGTH_SHORT).show();
                } else if(password.equalsIgnoreCase("")){
                    Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                } else {
                    reg_api("signup", name, "Male", "9899999999", email_id, address, password, "158.58.10.20");

                }
            }
        });
    }

    private void reg_api(String reqAction, String name, String gender, String mobile, String email_id, String address, String password, String ip_address){
        ApiUtility api = new ApiUtility(RegisterActivity.this, ApiUrl.BASE_URL, "", "Please wait", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.reg_user_api(reqAction, name, gender, mobile, email_id, address, password, ip_address);
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
                        JSONArray Content = jsonObject.getJSONArray("Content");
                        JSONObject data = Content.getJSONObject(0);
                        String userregid = data.getString("userregid");
                        preferenceManager.putPreferenceValues( Preference_Constant.USER_ID,userregid);
                        Toast.makeText(RegisterActivity.this,msg,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,OtpVerificationActivity.class);
                        intent.putExtra("email_id",email_id);
                        intent.putExtra("type","reg");
                        startActivity(intent);
                    }else if(requestStatus.equalsIgnoreCase("Failed")){
                        String Error = jsonObject.getString("Error");
                        Toast.makeText(RegisterActivity.this,Error,Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegisterActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }

}