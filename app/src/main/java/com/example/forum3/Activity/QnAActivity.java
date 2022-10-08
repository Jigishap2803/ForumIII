

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.forum3.Adapter.TopicListAdapter;
import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.Preference.PreferenceManager;
import com.example.forum3.Preference.Preference_Constant;
import com.example.forum3.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class QnAActivity extends AppCompatActivity implements ApiCallbackCode{
   private ImageView back_iv;
   private TextView select_topic_tv,submit_tv;
   private EditText query_et;
    private RecyclerView topics_rv;
    private JSONArray Content;
    private String topic_id = "",userregid = "", message ="", question="", ip_address ="" ;
    private PreferenceManager preferenceManager;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qna);
        preferenceManager = new PreferenceManager(QnAActivity.this);
        userregid = preferenceManager.getPreferenceValues( Preference_Constant.USER_ID);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        select_topic_tv = (TextView) findViewById(R.id.select_topic_tv);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        query_et = (EditText) findViewById(R.id.query_et);
        Content = new JSONArray();
        click_event();
    }

    private void click_event(){
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select_topic_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(QnAActivity.this);
                bottomSheetDialog.setContentView(R.layout.query_topic_bottomsheet_dialog);
                topics_rv = bottomSheetDialog.findViewById(R.id.topics_rv);
                topic_master_api("questiontopic");
                bottomSheetDialog.show();
                bsd_click_event();
            }
        });

        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSubmit();
            }
        });


    }

    private boolean validateSubmit(){
        if(query_et.length() == 0){
            Toast.makeText(QnAActivity.this, "Please enter your Query here, this field cannot be empty.", Toast.LENGTH_LONG).show();
        } else{
            message = query_et.getText().toString();
            ask_question_api("userquestion",userregid, question, topic_id, "158.58.10.20");
        }
        return true;
    }

    private void bsd_click_event(){
        topics_rv.addOnItemTouchListener(new TopicListAdapter.RecyclerTouchListener(this, topics_rv, new TopicListAdapter.ClickListener() {
            @Override
            public View onClick(View view, int position) {
                try {
                    JSONObject jsonObject = Content.getJSONObject(position);
                    topic_id = jsonObject.getString("topic_id");
                    String topic_name = jsonObject.getString("topic_name");
                    select_topic_tv.setText(topic_name);
                    bottomSheetDialog.dismiss();
                } catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void topic_master_api(String reqAction){
        ApiUtility api = new ApiUtility(QnAActivity.this, ApiUrl.BASE_URL, "", "Please wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.topic_master_api(reqAction);
        api.postRequest(responseCall, this, 1);

    }

    private void ask_question_api(String reqAction, String userregid, String question, String topicid, String ip_address){
        ApiUtility api = new ApiUtility(QnAActivity.this, ApiUrl.BASE_URL, "", "Please wait", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call <JsonObject> responseCall = apiRequest.ask_question_api(reqAction, userregid, question, topicid, ip_address);
        api.postRequest(responseCall, this, 2);

    }

    @Override
    public void onResponse(JSONObject jsonObject, int request_code, int status_code) {
        if (jsonObject!=null){
            if (request_code==1){
                try {
                    Content = jsonObject.getJSONArray("Content");
                    TopicListAdapter topicListAdapter = new TopicListAdapter(this, Content);
                    topics_rv.setLayoutManager(new LinearLayoutManager(QnAActivity.this));
                    topics_rv.setAdapter(topicListAdapter);


                } catch (JSONException e){
                    e.printStackTrace();

                }

            }

        }

        if (request_code==2){
            try {

                String msg = jsonObject.getString("msg");
                success_popup_dialog(msg);

            } catch (JSONException exception){
                exception.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(Object jsonObject, String error_msg) {
        if(!error_msg.equalsIgnoreCase("")){
            Toast.makeText(QnAActivity.this, error_msg, Toast.LENGTH_LONG).show();
        }else{
            try {
                JSONObject jo = new JSONObject(jsonObject.toString());
                String response = jo.getString("Error");
                Toast.makeText(QnAActivity.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void success_popup_dialog(String msg){
        Dialog dialog = new Dialog(QnAActivity.this);
        dialog.setContentView(R.layout.succcess_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LottieAnimationView lav_success = (LottieAnimationView) dialog.findViewById(R.id.lav_success);
        TextView msg_tv = (TextView)dialog.findViewById(R.id.msg_tv);
        msg_tv.setText(msg);
        TextView cancel_tv = (TextView)dialog.findViewById(R.id.cancel_tv);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        lav_success.playAnimation();
        dialog.show();
    }
}