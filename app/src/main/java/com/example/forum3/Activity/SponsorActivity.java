package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum3.Adapter.SponsorAdapter;
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

public class SponsorActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView sponsors_list_rv;
    private ImageView back_iv;
    JSONArray Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sponsor);
        sponsors_list_rv = (RecyclerView) findViewById(R.id.sponsors_list_rv);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        Content = new JSONArray();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sponsor_list_api("sponsors");
    }

    private void sponsor_list_api(String reqAction){
        ApiUtility api = new ApiUtility(SponsorActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call< JsonObject > responseCall = apiRequest.sponsor_list_api(reqAction);
        api.postRequest(responseCall, this, 1);
    }


    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject != null){
            if (request_code == 1){
                try {
                  Content = jsonObject.getJSONArray("Content");
                  SponsorAdapter sponsorAdapter = new SponsorAdapter(this, Content, new SponsorAdapter.MyAdapterListener() {
                      @Override
                      public void bannerOnClick(View v, int position) {
                           /*try {
                                JSONObject data = Content.getJSONObject(position);
                                String sponsor_id = data.getString("sponsor_id");
                                Intent intent = new Intent(SponsorActivity.this,SponsorDetailActivity.class);
                                intent.putExtra("sponsor_id",sponsor_id);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                      }

                      @Override
                      public void webOnClick(View v, int position) {
                          try {
                              JSONObject data = Content.getJSONObject(position);
                              String websitelink = data.getString("websitelink");
                              Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websitelink));
                              startActivity(browserIntent);
                          } catch (JSONException e){
                              e.printStackTrace();

                          }
                      }
                  });
                  sponsors_list_rv.setLayoutManager(new LinearLayoutManager(this));
                  sponsors_list_rv.setAdapter(sponsorAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {

        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(SponsorActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(SponsorActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }

    }
}