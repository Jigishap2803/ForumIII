package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forum3.Adapter.HotelIfoAdapter;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;

public class HotelInfoActivity extends AppCompatActivity implements ApiCallbackCode {
    private RecyclerView hotel_gallery_rv;
    private ImageView back_iv, hotel_banner_image_iv, visit_hotel_web_iv, visit_phone_iv;
    private TextView hotel_name_tv, hotel_description_tv, hotel_address_tv, hotel_contact_tv, hotel_url_tv;
    private CardView hotel_content_cv;
    String hotelid= "1";
    private ArrayList<HotelGalleryModel> listitem = new ArrayList<>();
    private LinearLayout address_ll;
    RecyclerView.Adapter HotelInfoAdapter;
    private JSONArray Content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hotel_info);
//        info_tv = (TextView) findViewById(R.id.info_tv);

        id_calling();
        click_event();


//        String text = "ResponsibleSteel Forum III will take place at The Peabody Memphis. For further information about The Peabody Memphis – known as the ‘South’s Grand Hotel’ " +
//                "and listed on the National Register of Historic Places – please visit their website.";
//        SpannableString spannableString = new SpannableString(text);
//        ForegroundColorSpan green = new ForegroundColorSpan(getResources().getColor(R.color.lg_1));
//        spannableString.setSpan(green, 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        StyleSpan boldStyleSpan = new StyleSpan(Typeface.BOLD);
//        spannableString.setSpan(boldStyleSpan, 46, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        StyleSpan boldStyleSpan1 = new StyleSpan(Typeface.BOLD);
//        spannableString.setSpan(boldStyleSpan1, 97, 116, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        info_tv.setText(spannableString);


    hotel_details_gallery_api("hoteldetail", hotelid);




    }

    private void id_calling(){
        back_iv = (ImageView) findViewById(R.id.back_iv);
        hotel_gallery_rv = (RecyclerView) findViewById(R.id.hotel_gallery_rv);
        hotel_banner_image_iv = (ImageView) findViewById(R.id.hotel_banner_image_iv);
        hotel_name_tv = (TextView) findViewById(R.id.hotel_name_tv);
        hotel_description_tv = (TextView) findViewById(R.id.hotel_description_tv);
        hotel_address_tv = (TextView) findViewById(R.id.hotel_address_tv);
        hotel_contact_tv = (TextView) findViewById(R.id.hotel_contact_tv);
        hotel_url_tv = (TextView) findViewById(R.id.hotel_url_tv);
        visit_phone_iv = (ImageView) findViewById(R.id.visit_phone_iv);
        visit_hotel_web_iv = (ImageView) findViewById(R.id.visit_hotel_web_iv);
    }

    private void click_event(){
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void hotel_details_gallery_api(String reqAction, String hotelid){
        ApiUtility api = new ApiUtility(HotelInfoActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call<JsonObject> responseCall = apiRequest.hotel_details_gallery_api(reqAction, hotelid);
        api.postRequest(responseCall, this, 1);
    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if (request_code==1){
                try {
                    JSONObject Content = jsonObject.getJSONObject("Content");
                    JSONObject data = Content.getJSONObject( "0");
                    String hotel_id = data.getString("hotel_id");
                    String hotel_name = data.getString("hotel_name");
                    String hotel_contact = data.getString("hotel_contact");
                    String hotel_address = data.getString("hotel_address");
                    String hotel_map = data.getString("hotel_map");
                    String hotel_url = data.getString("hotel_url");
                    String hotel_description = data.getString("hotel_description");
                    String url = data.getString("http_url");
                    String hotel_image = ApiUrl.IMG_BASE_URL + data.getString("hotel_image");
                    String banner_img = ApiUrl.IMG_BASE_URL + data.getString("banner_img");

                    hotel_name_tv.setText(hotel_name);
                    hotel_contact_tv.setText(hotel_contact);
                    hotel_address_tv.setText(hotel_address);
                    hotel_url_tv.setText(hotel_url);
                    hotel_description_tv.setText(hotel_description);

                    visit_phone_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri u = Uri.parse("tel:" + hotel_contact.toString());
                            Intent i = new Intent(Intent.ACTION_DIAL, u);
                            try
                            {
                                startActivity(i);
                            }
                            catch (SecurityException s)
                            {
                                Toast.makeText(HotelInfoActivity.this, "An error occurred", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    visit_hotel_web_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           Intent intent = new Intent(HotelInfoActivity.this, HotelUrlActivity.class);
                           intent.putExtra("hotel_url", hotel_url);
                           startActivity(intent);
                        }
                    });
                    Picasso.get().load(hotel_image).into(hotel_banner_image_iv);


                    JSONArray hotelImages = Content.getJSONArray("hotelImages");

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    hotel_gallery_rv.setLayoutManager(linearLayoutManager);
                    HotelIfoAdapter hotelIfoAdapter = new HotelIfoAdapter(this, hotelImages);
                    hotel_gallery_rv.setAdapter(hotelIfoAdapter);


                } catch (JSONException e){
                    e.printStackTrace();

                }


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
                Toast.makeText(this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception){
                exception.printStackTrace();
            }
        }

    }
}