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

import com.example.forum3.Adapter.VideoListAdapter;
import com.example.forum3.Adapter.VirtualEventList;
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

public class VideoListActivity extends AppCompatActivity implements ApiCallbackCode{
    private RecyclerView virtual_events_video_rv,session_video_rv,featured_video_rv,speaker_video_rv;

    private int[] image = {R.drawable.live_event};
    private String[] stream_type = {"Hydrogen for Life"};
    String [] name = {"Stream ONLINE"};

    private JSONArray data, features_video_data, session_video_data, speaker_video_data;
    private ImageView back_iv;
    private String video_url = "",userregid = "";
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_list);
        preferenceManager = new PreferenceManager(VideoListActivity.this);
        userregid = preferenceManager.getPreferenceValues(Preference_Constant.USER_ID);
        id_calling();
        click_event();
        data = new JSONArray();
        features_video_data = new JSONArray();
        session_video_data = new JSONArray();
        speaker_video_data = new JSONArray();
        //featured_video_list_api("videos","1");
        session_video_list_api("videos","2");
        //speaker_video_list_api("videos","3");
    }

    private void id_calling(){
        virtual_events_video_rv = (RecyclerView) findViewById(R.id.virtual_events_video_rv);
        session_video_rv = (RecyclerView) findViewById(R.id.session_video_rv);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        //featured_video_rv = (RecyclerView)findViewById(R.id.featured_video_rv);
        //speaker_video_rv = (RecyclerView)findViewById(R.id.speaker_video_rv);
    }

    private void click_event(){
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        virtual_events_video_rv.addOnItemTouchListener(new VirtualEventList.RecyclerTouchListener(this, virtual_events_video_rv, new VirtualEventList.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=TmSoop_1xUw"));
                startActivity(browserIntent);
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void featured_video_list_api(String reqAction, String videotypeid) {
        ApiUtility api = new ApiUtility(VideoListActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.video_list_api(reqAction, videotypeid);
        api.postRequest(responseCall, this, 1);

    }

    private void session_video_list_api(String reqAction, String videotypeid){
        ApiUtility api = new ApiUtility(VideoListActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.session_video_api(reqAction, videotypeid);
        api.postRequest(responseCall, (ApiCallbackCode) this, 2);
    }

    private void speaker_video_list_api(String reqAction, String videotypeid) {
        ApiUtility api = new ApiUtility(VideoListActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.speaker_video_api(reqAction, videotypeid);
        api.postRequest(responseCall, this, 3);

    }

    private void save_video_list_api(String reqAction, String userregid, String savedtypeflg, String savedid, String ip_address){
        ApiUtility api = new ApiUtility(VideoListActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.saved_data_api(reqAction, userregid, savedtypeflg,savedid,ip_address);
        api.postRequest(responseCall, (ApiCallbackCode) this, 4);

    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if(jsonObject!=null){
            if(request_code == 1){
                /* try {
                 *//*features_video_data = jsonObject.getJSONArray("Content");

                    VideoListAdapter videoListAdapter = new VideoListAdapter( this, features_video_data, new VideoListAdapter.MyAdapterListener() {
                        @Override
                        public void bannerOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject=features_video_data.getJSONObject(position);
                                String video_url = jsonObject.getString("video_url");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_url));
                                startActivity(browserIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void saveOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject=features_video_data.getJSONObject(position);
                                String video_id = jsonObject.getString("video_id");
                                save_video_list_api("saved",userregid,"1",video_id,"158.58.10.20");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } );
                    featured_video_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false) );
                    featured_video_rv.setAdapter(videoListAdapter);*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            if(request_code == 2){
                try {
                    session_video_data = jsonObject.getJSONArray("Content");
                    VideoListAdapter videoListAdapter = new VideoListAdapter( this, session_video_data, new VideoListAdapter.MyAdapterListener() {
                        @Override
                        public void bannerOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject = session_video_data.getJSONObject(position);
                                String video_url = jsonObject.getString("video_url");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_url));
                                startActivity(browserIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void saveOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject=session_video_data.getJSONObject(position);
                                String video_id = jsonObject.getString("video_id");
                                save_video_list_api("saved",userregid,"1",video_id,"158.58.10.20");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } );
                    session_video_rv.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
                    session_video_rv.setAdapter(videoListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(request_code == 3){
               /* try {
                    speaker_video_data = jsonObject.getJSONArray("Content");
                    VideoListAdapter videoListAdapter = new VideoListAdapter( this, speaker_video_data, new VideoListAdapter.MyAdapterListener() {
                        @Override
                        public void bannerOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject = speaker_video_data.getJSONObject(position);
                                String video_url = jsonObject.getString("video_url");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_url));
                                startActivity(browserIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void saveOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject=speaker_video_data.getJSONObject(position);
                                String video_id = jsonObject.getString("video_id");
                                save_video_list_api("saved",userregid,"1",video_id,"158.58.10.20");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } );
                    speaker_video_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false) );
                    speaker_video_rv.setAdapter(videoListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            if(request_code == 4){
                try {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(VideoListActivity.this,msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(VideoListActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(VideoListActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }
}