package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.forum3.Adapter.SessionReminderAdapter;
import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.Preference.PreferenceManager;
import com.example.forum3.Preference.Preference_Constant;
import com.example.forum3.R;
//import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity implements ApiCallbackCode {
    private ImageView back_iv;
    private RecyclerView session_rv;
//            event_rv, featured_res_rv, saved_video_rv;
    TextView name_tv, email_id_tv, number_tv,session_reminder_tv;
//    private String[] number = {"Session 01","Session 02"};
//    private String[] title = {"“Investing in Hydrogen”","“Investing in Hydrogen”"};
//    private String[] time = {"15:40 pm","15:40 pm"};
//
//    private int[] video_icon = {R.drawable.dummy_video_image,R.drawable.dummy_video_image};
//    private String[] video_name = {"Conversations on Hydrogen","Conversations on Hydrogen"};
//    private String[] video_topic = {"Helen Neal & Mark Selby","Helen Neal & Mark Selby"};
//
//    private String[] name ={"EU Code of Conduct Asahi","Hydrogen4Life Impact"};
//    private String[] my_event_name ={"Event 1","Event 2"};
    private PreferenceManager preferenceManager;
    private String userregid = "";
    JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        preferenceManager = new PreferenceManager(ProfileActivity.this);
        userregid = preferenceManager.getPreferenceValues( Preference_Constant.USER_ID);
        session_rv =  findViewById(R.id.session_rv);
         /*event_rv = (RecyclerView)findViewById(R.id.event_rv);
        featured_res_rv = (RecyclerView)findViewById(R.id.featured_res_rv);
        saved_video_rv = (RecyclerView)findViewById(R.id.saved_video_rv);*/

        back_iv = (ImageView) findViewById(R.id.back_iv);
        name_tv = (TextView)findViewById(R.id.name_tv);
        session_reminder_tv = findViewById(R.id.session_reminder_tv);
        email_id_tv = findViewById(R.id.email_id_tv);
        number_tv = findViewById(R.id.number_tv);
        data = new JSONArray();

        back_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                finish();
            }
        } );


        /*EventAdapter eventAdapter = new EventAdapter(this,number,title,time);
        session_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        session_rv.setAdapter(eventAdapter);*/

        /*VideoListAdapter videoListAdapter = new VideoListAdapter(this,video_name,video_topic,video_icon);
        saved_video_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false) );
        saved_video_rv.setAdapter(videoListAdapter);

        ResourcesLisAdapter resourcesLisAdapter = new ResourcesLisAdapter(this,name);
        featured_res_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        featured_res_rv.setAdapter(resourcesLisAdapter);

        MyEventsAdapter myEventsAdapter = new MyEventsAdapter(this,my_event_name);
        event_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        event_rv.setAdapter(myEventsAdapter);*/

        user_profile_api("userprofile", userregid);
        get_session_reminder_api("getreminder",userregid);

        session_rv.addOnItemTouchListener(new SessionReminderAdapter.RecyclerTouchListener(this, session_rv, new SessionReminderAdapter.ClickListener() {
            @Override
            public View onClick(View view, int position) {
                try {
                    JSONObject jsonObject = data.getJSONObject(position);
                    String session_id = jsonObject.getString("sessionid");
                    delete_pop_dialog(session_id);
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void user_profile_api(String reqAction, String userregid){
        ApiUtility api = new ApiUtility(ProfileActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.user_profile_api(reqAction, userregid);
        api.postRequest(responseCall, this, 1);
    }

    private void get_session_reminder_api(String reqAction, String userregid){
        ApiUtility api = new ApiUtility(ProfileActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.get_session_reminder_api(reqAction,userregid);
        api.postRequest(responseCall, this, 2);
    }

    private void remove_session_reminder_api(String reqAction, String userregid, String sessionid){
        ApiUtility api = new ApiUtility(ProfileActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.delete_session_reminder_api(reqAction, userregid, sessionid);
        api.postRequest(responseCall, this, 3);

    }


    /*private void saved_video_list_api(String reqAction, String userregid) {
        ApiUtility api = new ApiUtility(ProfileActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.get_saved_data_api(reqAction, userregid);
        api.postRequest(responseCall, this, 2);
    }*/

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if(request_code == 1){
                try {
                    JSONArray Content = jsonObject.getJSONArray("Content");
                    JSONObject data = Content.getJSONObject(0);
                    String name = data.getString("fullname");
                    String email_id = data.getString("email_id");
                    String mobile = data.getString("mobile");
                    name_tv.setText(name);
                    email_id_tv.setText(email_id);
                    number_tv.setText(mobile);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            if(request_code == 2){
                try {
                    data = jsonObject.getJSONArray("Content");
                    session_reminder_tv.setVisibility(View.VISIBLE);
                    SessionReminderAdapter sessionReminderAdapter = new SessionReminderAdapter(this, data);
                    session_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    session_rv.setAdapter(sessionReminderAdapter);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            if (request_code == 3){
                try {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(ProfileActivity.this, error_msg, Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception){
                exception.printStackTrace();
            }
        }

    }

    private void delete_pop_dialog(String session_id){
        Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.exit_pop_up_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LottieAnimationView lav_success =  dialog.findViewById(R.id.lav_success);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        tv_msg.setText("Do you want to remove this session reminder");
        TextView cancel_tv = dialog.findViewById(R.id.cancel_tv);
        TextView confirm_tv = dialog.findViewById(R.id.confirm_tv);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                remove_session_reminder_api("sessionreminderdel", userregid, session_id);
            }
        });
        lav_success.playAnimation();
        dialog.show();
    }
}