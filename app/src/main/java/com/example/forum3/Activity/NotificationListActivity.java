package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum3.Adapter.NotificationListAdapter;
import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class NotificationListActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView notification_list_rv;
    private String[] name = {"Streaming Live - Main Stage", "Streaming Live - Main Stage"};
    private String[] stage ={"Session 6", "Session 6"};
    private String[] time = {"1 hour ago", "1 hour ago"};
    private int[] icon = {R.drawable.dummy_person, R.drawable.dummy_person};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification_list);
        notification_list_rv = (RecyclerView) findViewById(R.id.notification_list_rv);
        ImageView back_iv = (ImageView) findViewById(R.id.back_iv);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        notification_list_api("notification");

    }

    private void notification_list_api(String reqAction){
        ApiUtility api = new ApiUtility(NotificationListActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.notification_list_api(reqAction);
        api.postRequest(responseCall, this, 1);

    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if(request_code==1){
                try {
                    JSONArray notification_data = jsonObject.getJSONArray("Content");
                    NotificationListAdapter notificationListAdapter = new NotificationListAdapter(this, notification_data);
                    notification_list_rv.setLayoutManager(new LinearLayoutManager(this));
                    notification_list_rv.setAdapter(notificationListAdapter);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(this, error_msg, Toast.LENGTH_LONG).show();
        } else {
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception){
                exception.printStackTrace();
            }
        }

    }
}