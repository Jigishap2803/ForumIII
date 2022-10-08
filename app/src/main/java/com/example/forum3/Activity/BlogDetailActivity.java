package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class BlogDetailActivity extends AppCompatActivity implements ApiCallbackCode{
    private ImageView back_iv, blog_image_iv;
    private TextView blog_title_tv, blog_date_tv, blog_author_tv, blog_time_tv, blog_desc_tv;
    private String blog_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_blog_detail);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        blog_image_iv = (ImageView) findViewById(R.id.blog_image_iv);
        blog_title_tv = (TextView) findViewById(R.id.blog_title_tv);
        blog_date_tv = (TextView) findViewById(R.id.blog_date_tv);
        blog_author_tv = (TextView) findViewById(R.id.blog_author_tv);
        blog_time_tv = (TextView) findViewById(R.id.blog_time_tv);
        blog_desc_tv = (TextView) findViewById(R.id.blog_desc_tv);

        Intent intent = getIntent();
        blog_id = intent.getStringExtra("blog_id");

        blog_detail_api("blogdetail",blog_id);
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

    private void blog_detail_api(String reqAction, String blog_id){
        ApiUtility api = new ApiUtility(BlogDetailActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.blog_details_api(reqAction, blog_id);
        api.postRequest(responseCall, (ApiCallbackCode) this, 1);
    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if(request_code == 1){
                try {
                    JSONArray data_array = jsonObject.getJSONArray("Content");
                    JSONObject data = data_array.getJSONObject(0);
                    String blog_topic = data.getString("blog_topic");
                    String blog_author = data.getString("blog_author");
                    String blog_date = data.getString("blod_date");
                    String blog_time = data.getString("blog_time");
                    String blog_description = data.getString("blog_description");
                    String pic = data.getString("http_url") + data.getString("blog_image");
                    String banner_img = data.getString("http_url") + data.getString("banner_img");

                    blog_title_tv.setText(blog_topic);
                    blog_author_tv.setText(blog_author);
                    blog_date_tv.setText(blog_date);
                    blog_time_tv.setText(blog_time);
                    blog_desc_tv.setText(blog_description);
                    Picasso.get().load(banner_img).into(blog_image_iv);

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
        } else{
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