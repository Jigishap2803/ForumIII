package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum3.Adapter.ResourcesListAdapter;
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

public class ResourcesListActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView resources_list_rv;
    private JSONArray Content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_resources_list);
        resources_list_rv = (RecyclerView) findViewById(R.id.resources_list_rv);
        Content = new JSONArray();

        ImageView back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resources_list_api("resources");

        resources_list_rv.addOnItemTouchListener(new ResourcesListAdapter.RecyclerTouchListener(this, resources_list_rv, new ResourcesListAdapter.ClickListener() {
            @Override
            public View onClick(View view, int position) {
                try {
                    JSONObject jsonObject = Content.getJSONObject(position);
                    String url = jsonObject.getString("http_url") + jsonObject.getString("resources_pdf");
                    Intent intent = new Intent(ResourcesListActivity.this, WebViewActivity.class);
                    intent.putExtra("url", url);
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

    }

    private void resources_list_api(String reqAction){
        ApiUtility api = new ApiUtility(ResourcesListActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.resources_list_api(reqAction);
        api.postRequest(responseCall, this, 1);
    }



    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
       if (jsonObject!=null){
           if (request_code == 1){
               try {
                   Content = jsonObject.getJSONArray("Content");
                   ResourcesListAdapter resourcesListAdapter = new ResourcesListAdapter(this, Content);
                   resources_list_rv.setLayoutManager(new LinearLayoutManager(this));
                   resources_list_rv.setAdapter(resourcesListAdapter);

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