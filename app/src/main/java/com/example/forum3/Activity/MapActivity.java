package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum3.Adapter.MapEventAdapter;
import com.example.forum3.Adapter.ResourcesListAdapter;
import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.Api;
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

public class MapActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView event_list_rv;
    private ImageView back_iv;
    private String[] name = {"Main Stage","Smith Center","Illuminate, L5"};
    private JSONArray Content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);
        event_list_rv = (RecyclerView) findViewById(R.id.event_list_rv);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        Content = new JSONArray();
        agenda_list_api("agendacat","1");

        event_list_rv.addOnItemTouchListener(new MapEventAdapter.RecyclerTouchListener(this, event_list_rv, new MapEventAdapter.ClickListener() {
            @Override
            public View onClick(View view, int position) {
                try {
                    JSONObject jsonObject = Content.getJSONObject(position);
                    String agenda_id = jsonObject.getString("agenda_id");
                    Intent intent = new Intent(MapActivity.this, MapEventDetailActivity.class);
                    intent.putExtra("agenda_id", agenda_id);
                    startActivity(intent);

                } catch (JSONException e){
                    e.printStackTrace();

                }
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void agenda_list_api(String reqAction, String eventid){
        ApiUtility api = new ApiUtility(MapActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.agenda_list_api(reqAction, eventid);
        api.postRequest(responseCall, this, 1);
    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if(jsonObject!=null){
            if (request_code == 1){
                try {
                    Content = jsonObject.getJSONArray("Content");
                    MapEventAdapter mapEventAdapter = new MapEventAdapter(this, Content);
                    event_list_rv.setLayoutManager(new LinearLayoutManager(this));
                    event_list_rv.setAdapter(mapEventAdapter);

                } catch (JSONException e){
                    e.printStackTrace();

                }

            }
        }

    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if (!error_msg.equalsIgnoreCase("")){
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