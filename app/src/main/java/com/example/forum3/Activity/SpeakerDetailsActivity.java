package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.forum3.Adapter.EventAdapter;
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

public class SpeakerDetailsActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView event_rv,featured_video_rv;
    private TextView speaker_desc_tv,speaker_name_tv,speaker_designation_tv,speaker_organization_tv,panel_title_tv;
    private ImageView speaker_image_iv,back_iv,linkdin_icon_iv,twitter_icon_iv,fb_icon_iv,web_icon_iv;
    private String speaker_id = "", name = "", speaker_designation = "";
    private JSONArray sessionspeakerData;
    private String user_id;
    private PreferenceManager preferenceManager;
    private JSONArray Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_speaker_details);
        preferenceManager = new PreferenceManager(SpeakerDetailsActivity.this);
        user_id = preferenceManager.getPreferenceValues( Preference_Constant.USER_ID);
        event_rv = (RecyclerView)findViewById(R.id.event_rv);
        speaker_name_tv = (TextView)findViewById( R.id.speaker_name_tv );
        speaker_designation_tv = (TextView)findViewById( R.id.speaker_designation_tv );
        speaker_organization_tv = (TextView)findViewById( R.id.speaker_organization_tv );
        /*speaker_desc_tv = (TextView)findViewById( R.id.speaker_desc_tv );*/
        speaker_image_iv = (ImageView) findViewById( R.id.speaker_image_iv );
            /*linkdin_icon_iv = (ImageView)findViewById( R.id.linkdin_icon_iv );
            twitter_icon_iv = (ImageView)findViewById( R.id.twitter_icon_iv );
            fb_icon_iv = (ImageView)findViewById( R.id.fb_icon_iv );
            web_icon_iv = (ImageView)findViewById( R.id.web_icon_iv );*/
        back_iv = (ImageView)findViewById(R.id.back_iv);
        //featured_video_rv = (RecyclerView)findViewById(R.id.featured_video_rv);

        sessionspeakerData = new JSONArray();
        Content = new JSONArray();

        Intent intent = getIntent();
        speaker_id = intent.getStringExtra("speaker_id");

        speaker_detail_api("speakerdetail",speaker_id);
        click_event();

        /*VideoListAdapter videoListAdapter = new VideoListAdapter(this,video_name,video_topic,video_icon);
        featured_video_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false) );
        featured_video_rv.setAdapter(videoListAdapter);*/

    }
    private void click_event(){
        back_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                finish();
            }
        } );

        event_rv.addOnItemTouchListener(new EventAdapter.RecyclerTouchListener(this, event_rv, new EventAdapter.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                try {
                    JSONObject jsonObject = sessionspeakerData.getJSONObject(position);
                    String agenda_id = jsonObject.getString("session_agendaid");
                    String session_id = jsonObject.getString("session_id");
                    String session_title = jsonObject.getString("session_title");
                    String session_speakerid = jsonObject.getString("session_speakerid");
                   /* String speaker_name = jsonObject.getString("speaker_name");
                    String profile_pic = jsonObject.getString("http_url") + jsonObject.getString("profile_pic");*/
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SpeakerDetailsActivity.this);
                    bottomSheetDialog.setContentView(R.layout.speaker_session_bottomsheet_dialog);

                    TextView session_name_tv = bottomSheetDialog.findViewById(R.id.session_name_tv);
                    session_name_tv.setText(session_title);
                    TextView session_cancel_tv = bottomSheetDialog.findViewById(R.id.session_cancel_tv);
                    TextView session_set_reminder_tv = bottomSheetDialog.findViewById(R.id.session_set_reminder_tv);
                    session_cancel_tv.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            bottomSheetDialog.dismiss();
                        }
                    } );

                    session_set_reminder_tv.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            set_session_reminder_api("sessionreminder",user_id,session_id,session_speakerid,"158.58.10.20");
                        }
                    } );

                    bottomSheetDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void speaker_detail_api(String reqAction, String  speakerid) {
        ApiUtility api = new ApiUtility(SpeakerDetailsActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.speaker_details_api(reqAction,speakerid);
        api.postRequest(responseCall, this, 1);

    }

    /*private void session_list_api(String reqAction, String agendaid) {
        ApiUtility api = new ApiUtility(SpeakerDetailsActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.session_list_api(reqAction,agendaid);
        api.postRequest(responseCall, this, 3);

    }*/


    private void set_session_reminder_api(String reqAction, String userregid, String sessionid, String speakerid, String ip_address) {
        ApiUtility api = new ApiUtility(SpeakerDetailsActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.set_session_reminder_api(reqAction,userregid,sessionid,speakerid,ip_address);
        api.postRequest(responseCall, this, 2);

    }

    @Override
    public void onResponse ( JSONObject jsonObject, int request_code, int status_code ) {
        if(jsonObject!=null){
            if(request_code == 1){
                try {
                    JSONObject Content = jsonObject.getJSONObject("Content");
                    JSONObject data = Content.getJSONObject( "0");
                    name = data.getString( "name");
                    speaker_designation = data.getString( "speaker_profession" );
                    String speaker_linkedin = data.getString( "speaker_linkedin" );
                    String speaker_facebook = data.getString( "speaker_facebook" );
                    String speaker_twitter = data.getString( "speaker_twitter" );
                    String speaker_websitelink = data.getString( "speaker_websitelink" );
                    String pic = ApiUrl.IMG_BASE_URL + data.getString("profile_pic");
                    String speaker_description = data.getString( "speaker_description" );
                    String company_name = data.getString( "company_name" );

                    sessionspeakerData = Content.getJSONArray("sessionspeakerData");

                    speaker_name_tv.setText( name );
                    speaker_designation_tv.setText(speaker_designation );
                    speaker_organization_tv.setText(company_name );
                    /*speaker_desc_tv.setText( speaker_description );
                    linkdin_icon_iv.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(speaker_linkedin));
                            startActivity(browserIntent);
                        }
                    } );

                    fb_icon_iv.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(speaker_facebook));
                            startActivity(browserIntent);
                        }
                    } );

                    twitter_icon_iv.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(speaker_twitter));
                            startActivity(browserIntent);
                        }
                    } );

                    web_icon_iv .setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(speaker_websitelink));
                            startActivity(browserIntent);
                        }
                    } );*/
                    Picasso.get().load( pic ).into( speaker_image_iv );

                    EventAdapter eventAdapter = new EventAdapter(this,sessionspeakerData);
                    event_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                    event_rv.setAdapter(eventAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(request_code == 2){
                try {
                    String msg = jsonObject.getString("msg");
                    success_pop_dialog(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /*if(request_code == 3){
                try {
                    Content = jsonObject.getJSONArray("Content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

    @Override
    public void onFailure ( Object jsonObject, String error_msg ) {

        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(SpeakerDetailsActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(SpeakerDetailsActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void success_pop_dialog(String msg){
        Dialog dialog = new Dialog(SpeakerDetailsActivity.this);
        dialog.setContentView(R.layout.succcess_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

        LottieAnimationView lav_success = (LottieAnimationView) dialog.findViewById(R.id.lav_success);
        TextView cancel_tv = (TextView)dialog.findViewById(R.id.cancel_tv);
        TextView msg_tv = (TextView)dialog.findViewById(R.id.msg_tv);
        msg_tv.setText(msg);
        //AlertDialog alertDialog = dialogBuilder.create();
        cancel_tv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                dialog.dismiss();
                finish();
            }
        } );
        lav_success.playAnimation();
        dialog.show();
    }

}