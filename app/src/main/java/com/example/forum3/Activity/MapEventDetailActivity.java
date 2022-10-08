package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MapEventDetailActivity extends AppCompatActivity implements ApiCallbackCode {
    private ImageView map_event_iv,back_iv,visit_web_iv;
    private TextView map_event_title_tv,map_event_address_tv,map_event_number_tv,map_event_web_tv;
    private RecyclerView gallery_rv;
    private CardView navigate_cv,direction_cv;
    private LinearLayout website_ll,phone_ll,address_ll;
    private String agenda_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map_event_detail);
        //gallery_rv = (RecyclerView) findViewById(R.id.gallery_rv);
        //navigate_cv = (CardView)findViewById(R.id.navigate_cv);
        //phone_ll = (LinearLayout)findViewById(R.id.phone_ll);
        map_event_iv = (ImageView) findViewById(R.id.map_event_iv);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        visit_web_iv = (ImageView) findViewById(R.id.visit_web_iv);
        map_event_title_tv = (TextView) findViewById(R.id.map_event_title_tv);
        map_event_address_tv = (TextView) findViewById(R.id.map_event_address_tv);
//        map_event_number_tv = (TextView) findViewById(R.id.map_event_number_tv);
        map_event_web_tv = (TextView) findViewById(R.id.map_event_web_tv);
        direction_cv = (CardView) findViewById(R.id.direction_cv);
        address_ll = (LinearLayout) findViewById(R.id.address_ll);
        website_ll = (LinearLayout) findViewById(R.id.website_ll);

        map_event_iv.setImageResource(R.drawable.sci_meuseum);
        map_event_title_tv.setText("London Science Museum");
        map_event_address_tv.setText("The London Science Museum, Exhibition Rd, South Kensington, London SW7 2DD");
        map_event_web_tv.setText("www.sciencemuseum.org.uk/");

        visit_web_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sciencemuseum.org.uk"));
                startActivity(browseIntent);
            }
        });

        direction_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/KJM4jW8J7dLorxPR6"));
                startActivity(browserIntent);
            }
        });

        Intent intent = getIntent();
        agenda_id = intent.getStringExtra("agenda_id");
        //agenda_detail_api("agendanavigation","3","1");


        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void agenda_detail_api(String reqAction, String agendaid, String eventid) {
        ApiUtility api = new ApiUtility(MapEventDetailActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.map_event_detail_api(reqAction,agendaid,eventid);
        api.postRequest(responseCall, this, 1);

    }


    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if(jsonObject!=null){
            if(request_code == 1){
                try {

                    JSONObject Content = jsonObject.getJSONObject("Content");
                    JSONObject data = Content.getJSONObject("0");
                    JSONArray galleryImages = Content.getJSONArray("galleryImages");
                    String banner = data.getString("http_url") + data.getString("banner_img");
                    String agenda_category = data.getString("agenda_category");
                    String agenda_address = data.getString("agenda_address");
                    String agenda_map = data.getString("agenda_map");
                    String agenda_phone = data.getString("agenda_phone");
                    String agenda_websiteurl = data.getString("agenda_websiteurl");
                    String agenda_description = data.getString("agenda_description");


/*                    ImageAdapter imageAdapter = new ImageAdapter(MapEventDetailActivity.this,galleryImages);
                    gallery_rv.setLayoutManager(new LinearLayoutManager(MapEventDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
                    gallery_rv.setAdapter(imageAdapter);*/

                    address_ll.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(agenda_map));
                            startActivity(browserIntent);
                        }
                    } );

                    /*phone_ll.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + agenda_phone));
                            startActivity(intent);
                        }
                    } );*/

                    website_ll.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(agenda_websiteurl));
                            startActivity(browserIntent);
                        }
                    } );

                   /* navigate_cv.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(agenda_map));
                            startActivity(browserIntent);
                        }
                    } );*/

                    direction_cv.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View view ) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(agenda_map));
                            startActivity(browserIntent);
                        }
                    } );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(MapEventDetailActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(MapEventDetailActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }

    }

    public void showMap( Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}