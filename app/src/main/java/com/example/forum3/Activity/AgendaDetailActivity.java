package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class AgendaDetailActivity extends AppCompatActivity implements ApiCallbackCode {
    private ImageView agenda_detail_iv,back_iv;
    private TextView agenda_cat_tv,agenda_address_tv,agenda_desc_tv;
    private CardView agenda_detail_session_cv,agenda_detail_speaker_cv;
    private String agenda_id = "", agenda_category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_agenda_detail);
        //agenda_address_tv = (TextView)findViewById(R.id.agenda_address_tv);
        agenda_detail_iv = (ImageView) findViewById(R.id.agenda_detail_iv);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        agenda_cat_tv = (TextView) findViewById(R.id.agenda_cat_tv);
        agenda_desc_tv = (TextView) findViewById(R.id.agenda_desc_tv);
        agenda_detail_session_cv = (CardView) findViewById(R.id.agenda_detail_session_cv);
        agenda_detail_speaker_cv = (CardView) findViewById(R.id.agenda_detail_speaker_cv);

        Intent intent = getIntent();
        agenda_id = intent.getStringExtra("agenda_id");
        agenda_detail_api("agendadetail",agenda_id,"1");

        agenda_detail_session_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AgendaDetailActivity.this, SessionsListActivity.class);
                i.putExtra("agenda_id", agenda_id);
                i.putExtra("agenda_cat", agenda_category);
                startActivity(i);
            }
        });

        agenda_detail_speaker_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AgendaDetailActivity.this, SessionSpeakerListActivity.class);
                i.putExtra("agenda_id", agenda_id);
                startActivity(i);

            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void agenda_detail_api(String reqAction, String agendaid, String eventid){
        ApiUtility api = new ApiUtility(AgendaDetailActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.agenda_detail_api(reqAction, agendaid, eventid);
        api.postRequest(responseCall, this, 1);

    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if (request_code == 1){
                try {
                    JSONArray Content = jsonObject.getJSONArray("Content");
                    JSONObject data = Content.getJSONObject(0);
                    String banner = data.getString("http_url") + data.getString("banner_img");
                    agenda_category = data.getString("agenda_category");
                    String agenda_address = data.getString("agenda_address");
                    String agenda_description = data.getString("agenda_description");
                    Picasso.get().load(banner).into(agenda_detail_iv);
                    agenda_cat_tv.setText(agenda_category);
                    agenda_desc_tv.setText(agenda_description);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if (!error_msg.equalsIgnoreCase("")){
            Toast.makeText(this, error_msg, Toast.LENGTH_SHORT).show();
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
}