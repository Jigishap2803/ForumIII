package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum3.Adapter.SessionSpeakerListAdapter;
import com.example.forum3.Adapter.SessionSpeakerPanelistAdapter;
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

public class SessionSpeakerListActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView session_speaker_list_rv,session_speaker_panel_list_rv;
    private String[] name1 ={"Roshan Chaturvedi","Tejas Gupta"};
    private String[] desi1 ={"Roshan Chaturvedi","Tejas Gupta"};
    private int[] icon ={R.drawable.dummy_person,R.drawable.dummy_person};
    private ImageView back_iv;
    private JSONArray speaker_data, panel_data;
    private String agenda_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_session_speaker_list);
        Intent intent = getIntent();
        agenda_id = intent.getStringExtra("agenda_id");
        id_Calling();
        click_event();
    }

    private void id_Calling(){
        session_speaker_list_rv = (RecyclerView) findViewById( R.id.session_speaker_list_rv);
        session_speaker_panel_list_rv = (RecyclerView) findViewById( R.id.session_speaker_panel_list_rv);
        back_iv = (ImageView)findViewById(R.id.back_iv);
        speaker_data = new JSONArray();
        panel_data = new JSONArray();
        speaker_list_api("sessionspeakerspanellist",agenda_id);
    }

    private void click_event(){
        back_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                finish();
            }
        } );

        /*speaker_list_rv.addOnItemTouchListener(new SpeakerListAdapter.RecyclerTouchListener(this, speaker_list_rv, new SpeakerListAdapter.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                try {
                    JSONObject jsonObject = Content.getJSONObject(position);
                    String speaker_id = jsonObject.getString("speaker_id");
                    Intent intent = new Intent(SessionSpeakerListActivity.this,SpeakerDetailsActivity.class);
                    intent.putExtra("speaker_id",speaker_id);
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

    private void speaker_list_api(String reqAction, String agendaid) {
        ApiUtility api = new ApiUtility(SessionSpeakerListActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.session_list_api(reqAction,agendaid);
        api.postRequest(responseCall, this, 1);

    }


    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if(jsonObject!=null){
            if(request_code == 1){
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    speakerAdapter(data);
                    //panellistAdapter(panel_data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(SessionSpeakerListActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(SessionSpeakerListActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void speakerAdapter(JSONArray speaker_data){
        SessionSpeakerListAdapter sessionSpeakerListAdapter = new SessionSpeakerListAdapter(this,speaker_data);
        session_speaker_list_rv.setLayoutManager(new LinearLayoutManager(this));
        session_speaker_list_rv.setAdapter(sessionSpeakerListAdapter);
        sessionSpeakerListAdapter.notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void panellistAdapter(JSONArray panel_data){
        SessionSpeakerPanelistAdapter sessionSpeakerPanelListAdapter = new SessionSpeakerPanelistAdapter(this,panel_data);
        session_speaker_panel_list_rv.setLayoutManager(new LinearLayoutManager(this));
        session_speaker_panel_list_rv.setAdapter(sessionSpeakerPanelListAdapter);
        sessionSpeakerPanelListAdapter.notifyDataSetChanged();
    }
}