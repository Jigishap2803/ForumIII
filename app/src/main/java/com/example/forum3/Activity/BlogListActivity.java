package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum3.Adapter.BlogAdapter;
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

public class BlogListActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView blog_list_rv;
    private ImageView back_iv;
    private JSONArray Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_blog_list);
        // = (RecyclerView)findViewById(R.id.blog_list_rv);

        back_iv = (ImageView)findViewById(R.id.back_iv);

        Content = new JSONArray();

        back_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                finish();
            }
        } );

        //blog_list_api("blogs");
        //click_event();
    }

    private void click_event(){



        /*blog_list_rv.addOnItemTouchListener(new BlogAdapter.RecyclerTouchListener(this, blog_list_rv, new BlogAdapter.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                try {
                    JSONObject jsonObject = Content.getJSONObject(position);
                    String blog_id = jsonObject.getString("blog_id");
                    Intent intent = new Intent(BlogListActivity.this,BlogDetailActivity.class);
                    intent.putExtra("blog_id",blog_id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

    }

    private void blog_list_api(String reqAction) {
        ApiUtility api = new ApiUtility(BlogListActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.blog_list_api(reqAction);
        api.postRequest(responseCall, this, 1);

    }


    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if(jsonObject!=null){
            if(request_code == 1){
                try {
                    Content = jsonObject.getJSONArray("Content");
                    BlogAdapter blogAdapter = new BlogAdapter(this,Content);
                    blog_list_rv.setLayoutManager(new LinearLayoutManager(this));
                    blog_list_rv.setAdapter(blogAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(BlogListActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(BlogListActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }

    }
}