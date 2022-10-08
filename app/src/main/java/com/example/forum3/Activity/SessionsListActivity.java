package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.forum3.Adapter.AgendaListAdapter;
import com.example.forum3.Adapter.PanelListAdapter;
import com.example.forum3.Adapter.SessionAdapter;
import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.Preference.PreferenceManager;
import com.example.forum3.Preference.Preference_Constant;
import com.example.forum3.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SessionsListActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView agenda_list_rv,panel_list_rv;
    private ImageView speaker_image_iv;
    private TextView speaker_name_tv,speaker_designation_tv,speaker_organization_tv,title_tv,panel_title_tv;
    private JSONArray Content;
    private String user_id = "",agenda_id = "",agenda_cat = "";
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sessions_list);
        preferenceManager = new PreferenceManager(SessionsListActivity.this);
        user_id = preferenceManager.getPreferenceValues( Preference_Constant.USER_ID);
        Intent intent = getIntent();
        agenda_id = intent.getStringExtra("agenda_id");
        agenda_cat = intent.getStringExtra("agenda_cat");
        agenda_list_rv = (RecyclerView) findViewById( R.id.agenda_list_rv );
        title_tv = (TextView) findViewById( R.id.title_tv );
        title_tv.setText(agenda_cat);
        Content = new JSONArray();

        session_list_api("agendasession",agenda_id);

        ImageView back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        agenda_list_rv.addOnItemTouchListener(new AgendaListAdapter.RecyclerTouchListener(this, agenda_list_rv, new AgendaListAdapter.ClickListener() {
            @Override
            public View onClick(View view, int position) {
                try {
                    JSONObject jsonObject = Content.getJSONObject(position);
                    String agenda_id = jsonObject.getString("agenda_id");
                    String session_id = jsonObject.getString("session_id");
                    String session_title = jsonObject.getString("session_title");
                    String session_speakerid = jsonObject.getString("session_speakerid");
                    String speaker_name = jsonObject.getString("speaker_name");
                    String profile_pic = jsonObject.getString("http_url") + jsonObject.getString("profile_pic");
                    String speaker_designation = jsonObject.getString("speaker_designation");
                    String company_name = jsonObject.getString("company_name");
                    JSONArray panellistData = jsonObject.getJSONArray("panellistData");
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SessionsListActivity.this);
                    bottomSheetDialog.setContentView(R.layout.speaker_detail_bottomsheet_dialog);

                    RecyclerView panel_list_rv = bottomSheetDialog.findViewById(R.id.panel_list_rv);
                    speaker_image_iv = bottomSheetDialog.findViewById(R.id.speaker_image_iv);
                    speaker_name_tv = bottomSheetDialog.findViewById(R.id.speaker_name_tv);
                    speaker_organization_tv = bottomSheetDialog.findViewById(R.id.speaker_organization_tv);
                    speaker_designation_tv = bottomSheetDialog.findViewById(R.id.speaker_designation_tv);
                    panel_title_tv = bottomSheetDialog.findViewById(R.id.panel_title_tv);
                    speaker_name_tv.setText(session_title);

                    if (!speaker_name.equalsIgnoreCase("null")){
                        speaker_designation_tv.setText(speaker_name);
                    } else {
                        speaker_designation_tv.setText("None");
                    }

                    if (!company_name.equalsIgnoreCase("null")){
                        speaker_organization_tv.setText(company_name);
                    } else {
                        speaker_organization_tv.setText("None");
                    }

                    if (!profile_pic.equalsIgnoreCase("https://runtimeeventapp.com/hforlife/")){
                        Picasso.get().load(profile_pic).into(speaker_image_iv);
                    } else {
                        speaker_image_iv.setImageResource(R.drawable.person_img);
                    }

                    JSONObject data = panellistData.getJSONObject(0);
                    String panel_name = jsonObject.getString("panellist_name");

                    if (!panel_name.equalsIgnoreCase("null")){
                        panel_title_tv.setVisibility(View.VISIBLE);
                        PanelListAdapter panelListAdapter = new PanelListAdapter(SessionsListActivity.this, panellistData);
                        panel_list_rv.setLayoutManager(new LinearLayoutManager(SessionsListActivity.this));
                        panel_list_rv.setAdapter(panelListAdapter);
                    } else {
                        panel_title_tv.setVisibility(View.GONE);
                        panel_list_rv.setAdapter(null);
                    }

                    TextView cancel_tv = bottomSheetDialog.findViewById(R.id.cancel_tv);
                    TextView session_set_reminder_tv = bottomSheetDialog.findViewById(R.id.session_set_reminder_tv);
                    cancel_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.dismiss();
                        }
                    });

                    session_set_reminder_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            set_session_reminder_api("sessionreminder",user_id,session_id,session_speakerid,"158.58.10.20");
                        }
                    });


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

    private void session_list_api(String reqAction, String agendaid) {
        ApiUtility api = new ApiUtility(SessionsListActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call< JsonObject > responseCall = apiRequest.session_list_api(reqAction, agendaid);
        api.postRequest(responseCall, this, 1);
    }

    private void set_session_reminder_api(String reqAction, String userregid, String sessionid, String speakerid, String ip_address){
        ApiUtility api = new ApiUtility(SessionsListActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call< JsonObject > responseCall = apiRequest.set_session_reminder_api(reqAction, userregid, sessionid, speakerid, ip_address);
        api.postRequest(responseCall, this, 2);

    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if (request_code==1){
                try {
                    Content = jsonObject.getJSONArray("Content");
                    SessionAdapter sessionAdapter = new SessionAdapter(this, Content);
                    agenda_list_rv.setLayoutManager(new LinearLayoutManager(this));
                    agenda_list_rv.setAdapter(sessionAdapter);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

        if (request_code == 2){
            try {
                String msg = jsonObject.getString("msg");
                success_pop_dialog(msg);
            } catch (JSONException e){
                e.printStackTrace();
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
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            } catch (JSONException exception){
                exception.printStackTrace();
            }
        }

    }
    private void success_pop_dialog(String msg){
        /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this,R.style.MyRounded_MaterialComponents_MaterialAlertDialog);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.succcess_popup_dialog, null);
        dialogBuilder.setView(dialogView);*/
        Dialog dialog = new Dialog(SessionsListActivity.this);
        dialog.setContentView(R.layout.succcess_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

        LottieAnimationView lav_success = (LottieAnimationView) dialog.findViewById(R.id.lav_success);
        TextView cancel_tv = (TextView)dialog.findViewById(R.id.cancel_tv);
        TextView msg_tv = (TextView)dialog.findViewById(R.id.msg_tv);
        msg_tv.setText(msg);
        cancel_tv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                dialog.dismiss();
            }
        } );
        lav_success.playAnimation();
        dialog.show();
    }
}