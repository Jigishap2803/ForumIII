package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.R;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SponsorDetailActivity extends AppCompatActivity implements ApiCallbackCode {
    private ImageView back_iv, sponsor_icon_iv, linkdin_icon_iv, twitter_icon_iv, fb_icon_iv, web_icon_iv;
    private TextView sponsor_name_tv, sponsor_designation_tv, sponsor_desc_tv;
    String sponsor_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sponsor_detail);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        sponsor_icon_iv = (ImageView) findViewById(R.id.sponsor_icon_iv);
        linkdin_icon_iv = (ImageView) findViewById(R.id.linkdin_icon_iv);
        twitter_icon_iv = (ImageView) findViewById(R.id.twitter_icon_iv);
        fb_icon_iv = (ImageView) findViewById(R.id.fb_icon_iv);
        web_icon_iv = (ImageView) findViewById(R.id.web_icon_iv);
        sponsor_name_tv = (TextView) findViewById(R.id.sponsor_name_tv);
        sponsor_designation_tv = (TextView) findViewById(R.id.sponsor_designation_tv);
        sponsor_desc_tv = (TextView) findViewById(R.id.sponsor_desc_tv);
        Intent intent = getIntent();
        sponsor_id = intent.getStringExtra("sponsor_id");
        sponsor_detail_api( "sponsordetail",sponsor_id);
        click_event();
    }

    private void click_event(){
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sponsor_detail_api(String reqAction, String sponsor_id){
        ApiUtility api = new ApiUtility(SponsorDetailActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.sponsor_details_api(reqAction, sponsor_id);
        api.postRequest(responseCall, this, 1);
    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if (request_code==1){
                try {
                    JSONArray Content = jsonObject.getJSONArray("Content");
                    JSONObject data = Content.getJSONObject(0);
                    String company_name = data.getString("company_name");
                    String sponsor_profession = data.getString("sponsor_profession");
                    String sponsor_linkedin = data.getString("sponsor_linkedin");
                    String sponsor_facebook = data.getString("sponsor_facebook");
                    String sponsor_twitter = data.getString("sponsor_twitter");
                    String sponsor_websitelink = data.getString("websitelink");
                    String pic = data.getString("http_url") + data.getString("sponsor_logo");
                    String sponsor_description = data.getString("sponsor_description");

                    sponsor_name_tv.setText(company_name);
                    sponsor_designation_tv.setText(sponsor_profession);
                    sponsor_desc_tv.setText(sponsor_description);

                    linkdin_icon_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sponsor_linkedin));
                            startActivity(browserIntent);
                        }
                    });

                    twitter_icon_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sponsor_twitter));
                            startActivity(browserIntent);
                        }
                    });

                    fb_icon_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sponsor_facebook));
                            startActivity(browserIntent);
                        }
                    });

                    web_icon_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sponsor_websitelink));
                            startActivity(browserIntent);
                        }
                    });
                    Picasso.get().load(pic).into(sponsor_icon_iv);

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