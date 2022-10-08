package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forum3.Adapter.EventGalleryAdapter;
import com.example.forum3.Adapter.EventTabAdapter;
import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class EventDetailActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView event_tab_rv,event_detail_gallery_rv;
    private CardView get_involved_cv;
    private JSONArray eventtabsData, eventtabImages,image_Data_1,image_Data_2,image_Data_3;
    private ImageView back_iv,event_pic_iv;
    private TextView event_detail_tv,event_detail_getinvolved_tv;
    private LinearLayout event_detail_direction_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_detail);
        back_iv = findViewById(R.id.back_iv);
        event_tab_rv = findViewById(R.id.event_tab_rv);
        event_detail_gallery_rv = findViewById(R.id.event_detail_gallery_rv);
        event_pic_iv = findViewById(R.id.event_pic_iv);
        event_detail_tv = findViewById(R.id.event_detail_tv);
        event_detail_getinvolved_tv = findViewById(R.id.event_detail_getinvolved_tv);
        get_involved_cv = findViewById(R.id.get_involved_cv);
        event_detail_direction_ll = findViewById(R.id.event_detail_direction_ll);
        eventtabsData = new JSONArray();
        eventtabImages = new JSONArray();
        image_Data_1 = new JSONArray();
        //image_Data_2 = new JSONArray();
        image_Data_3 = new JSONArray();

        //event_detail_tv.setMovementMethod(new ScrollingMovementMethod());

        back_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                finish();
            }
        } );

        event_detail_tv.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String event_id = intent.getStringExtra("event_id");

        event_detail_api("eventdetail",event_id);
        click_event();

    }

    private void click_event(){
        event_tab_rv.addOnItemTouchListener(new EventTabAdapter.RecyclerTouchListener(this, event_tab_rv, new EventTabAdapter.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                try {
                    JSONObject jsonObject = eventtabsData.getJSONObject(position);

                    String eventtabs_description = jsonObject.getString("eventtabs_description");
                    String eventtabs_getinvolved = jsonObject.getString("eventtabs_getinvolved");
                    String eventtabs_pic = ApiUrl.IMG_BASE_URL + jsonObject.getString("eventtabs_pic");

                    Picasso.get().load(eventtabs_pic).into(event_pic_iv);
                    event_detail_tv.setText(eventtabs_description);
                    event_detail_getinvolved_tv.setText(eventtabs_getinvolved);

                    if(position==0){
                        event_detail_gallery_rv.setLayoutManager(new LinearLayoutManager(EventDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        EventGalleryAdapter eventGalleryAdapter = new EventGalleryAdapter(EventDetailActivity.this,image_Data_1);
                        event_detail_gallery_rv.setAdapter(eventGalleryAdapter);
                        get_involved_cv.setVisibility(View.GONE);
                        event_detail_direction_ll.setVisibility(View.GONE);
                        eventGalleryAdapter.notifyDataSetChanged();
                        event_detail_gallery_rv.addOnItemTouchListener(new EventGalleryAdapter.RecyclerTouchListener(EventDetailActivity.this, event_tab_rv, new EventGalleryAdapter.ClickListener() {
                            @Override
                            public View onClick( View view, int position) {
                                try {
                                    JSONArray img_data_one = image_Data_1;
                                    JSONObject jsonObject = img_data_one.getJSONObject(position);
                                    String pic = ApiUrl.IMG_BASE_URL + jsonObject.getString("eventgallery_pic");
                                    image_pop_dialog(pic);

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

                    if(position==1){
                        event_detail_gallery_rv.setLayoutManager(new LinearLayoutManager(EventDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        EventGalleryAdapter eventGalleryAdapter = new EventGalleryAdapter(EventDetailActivity.this,image_Data_3);
                        event_detail_gallery_rv.setAdapter(eventGalleryAdapter);
                        eventGalleryAdapter.notifyDataSetChanged();

                        get_involved_cv.setVisibility(View.GONE);
                        event_detail_direction_ll.setVisibility(View.VISIBLE);

                        event_detail_direction_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/KJM4jW8J7dLorxPR6"));
                                startActivity(browserIntent);
                            }
                        });

                        event_detail_gallery_rv.addOnItemTouchListener(new EventGalleryAdapter.RecyclerTouchListener(EventDetailActivity.this, event_tab_rv, new EventGalleryAdapter.ClickListener() {
                            @Override
                            public View onClick( View view, int position) {
                                try {
                                    JSONArray img_data_three = image_Data_3;
                                    JSONObject jsonObject = img_data_three.getJSONObject(position);
                                    String pic = "https://runtimeeventapp.com/hforlife/" + jsonObject.getString("eventgallery_pic");
                                    image_pop_dialog(pic);

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

    private void image_pop_dialog(String url){

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.full_screen_image_dialog);

        ImageView full_screen_iv = (ImageView)bottomSheetDialog.findViewById(R.id.full_screen_iv);
        Picasso.get().load(url).into(full_screen_iv);

        bottomSheetDialog.show();

        bottomSheetDialog.show();

    }

    private void event_detail_api(String reqAction, String eventid) {
        ApiUtility api = new ApiUtility(EventDetailActivity.this, ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.event_detail_api(reqAction,eventid);
        api.postRequest(responseCall, this, 1);

    }


    @Override
    public void onResponse ( JSONObject jsonObject, int request_code, int status_code ) {
        if(jsonObject!=null){
            if(request_code == 1){
                try {

                    JSONObject data = jsonObject.getJSONObject("Content");
                    JSONObject o_data = data.getJSONObject("0");
                    eventtabsData = data.getJSONArray("eventtabsData");
                    JSONObject eventtabImages = data.getJSONObject("eventtabImages");
                    image_Data_1 = eventtabImages.getJSONArray("1");
                    //image_Data_2 = eventtabImages.getJSONArray("2");
                    //image_Data_3 = eventtabImages.getJSONArray("3");
                    get_involved_cv.setVisibility(View.GONE);
                    event_detail_gallery_rv.setLayoutManager(new LinearLayoutManager(EventDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
                    EventGalleryAdapter eventGalleryAdapter = new EventGalleryAdapter(EventDetailActivity.this,image_Data_1);
                    event_detail_gallery_rv.setAdapter(eventGalleryAdapter);
                    event_detail_gallery_rv.addOnItemTouchListener(new EventGalleryAdapter.RecyclerTouchListener(EventDetailActivity.this, event_tab_rv, new EventGalleryAdapter.ClickListener() {
                        @Override
                        public View onClick( View view, int position) {
                            try {
                                JSONObject jsonObject = image_Data_1.getJSONObject(position);
                                String pic = "https://runtimeeventapp.com/hforlife/" + jsonObject.getString("eventgallery_pic");
                                image_pop_dialog(pic);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return view;
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                    event_tab(eventtabsData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure ( Object jsonObject, String error_msg ) {

        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(EventDetailActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(EventDetailActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void event_tab(JSONArray data){
        event_tab_rv.setLayoutManager(new LinearLayoutManager(EventDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
        EventTabAdapter eventTabAdapter = new EventTabAdapter(EventDetailActivity.this,data);
        event_tab_rv.setAdapter(eventTabAdapter);

        JSONObject jsonObject =null;
        try {
            jsonObject=eventtabsData.getJSONObject(0);
            String eventtabs_description = jsonObject.getString("eventtabs_description");
            String eventtabs_getinvolved = jsonObject.getString("eventtabs_getinvolved");
            String eventtabs_pic = ApiUrl.IMG_BASE_URL + jsonObject.getString("eventtabs_pic");
            Picasso.get().load(eventtabs_pic).into(event_pic_iv);
            event_detail_tv.setText(eventtabs_description);
            event_detail_getinvolved_tv.setText(eventtabs_getinvolved);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}