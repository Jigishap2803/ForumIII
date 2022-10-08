package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.example.forum3.Adapter.FoodDetailAdapter;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class FoodAndDiningActivity extends AppCompatActivity implements ApiCallbackCode {
    ImageView back_iv;
    RecyclerView food_detail_rv;
    String foodid="1";
    RecyclerView.Adapter FoodDetailAdapter;
    String[] data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_food_and_dining);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        food_detail_rv = (RecyclerView) findViewById(R.id.food_detail_rv);

        click_event();
        food_dining_session_list_api("foods", foodid);



    }

    private void food_dining_session_list_api(String reqAction, String foodid){
        ApiUtility api = new ApiUtility(FoodAndDiningActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.food_dining_session_list_api(reqAction,foodid);
        api.postRequest(responseCall, (ApiCallbackCode) this, 1);
    }

    private void click_event(){
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if (request_code==1){
                try {
                    JSONArray data = jsonObject.getJSONArray("Content");
                    FoodDetailAdapter foodDetailAdapter = new FoodDetailAdapter(getApplicationContext(), data);
                    food_detail_rv.setLayoutManager(new LinearLayoutManager(this));
                    food_detail_rv.setAdapter(foodDetailAdapter);

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