package com.example.forum3.ApiService;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIServices {

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> reg_user_api(@Query("reqAction") String reqAction, @Query("name") String name,
                                  @Query("gender") String gender,@Query("mobile") String mobile,
                                  @Query("email_id") String email_id,@Query("address") String address,
                                  @Query("password") String password,@Query("ip_address") String ip_address);

    @POST(ApiUrl.BASE_URL)
    Call<JsonObject> send_otp_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid,
                                     @Query("otp") String otp);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> login_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid,
                                @Query("email") String email,@Query("pass") String pass);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> speaker_list_api( @Query("reqAction") String reqAction);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> video_list_api( @Query("reqAction") String reqAction, @Query("videotypeid") String videotypeid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> event_list_api( @Query("reqAction") String reqAction);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> forget_password_api( @Query("reqAction") String reqAction, @Query("email") String email);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> resources_list_api( @Query("reqAction") String reqAction);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> sponsor_list_api( @Query("reqAction") String reqAction);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> blog_list_api( @Query("reqAction") String reqAction);

    @POST(ApiUrl.BASE_URL)
    Call<JsonObject> set_password_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid,
                                          @Query("newpwd") String newpwd,@Query("confirmpwd") String confirmpwd);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> sponsor_details_api( @Query("reqAction") String reqAction, @Query("sponsorid") String sponsorid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> speaker_details_api( @Query("reqAction") String reqAction, @Query("speakerid") String speakerid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> blog_details_api( @Query("reqAction") String reqAction, @Query("blogid") String blogid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> agenda_list_api( @Query("reqAction") String reqAction, @Query("eventid") String eventid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> agenda_detail_api( @Query("reqAction") String reqAction, @Query("agendaid") String agendaid, @Query("eventid") String eventid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> map_event_detail_api( @Query("reqAction") String reqAction, @Query("agendaid") String agendaid, @Query("eventid") String eventid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> event_detail_api( @Query("reqAction") String reqAction, @Query("eventid") String eventid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> notification_list_api( @Query("reqAction") String reqAction);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> user_profile_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid);


    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> get_session_reminder_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> saved_data_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid, @Query("savedtypeflg") String savedtypeflg,
                                     @Query("savedid") String savedid, @Query("ip_address") String ip_address);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> get_saved_data_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> session_list_api( @Query("reqAction") String reqAction, @Query("agendaid") String agendaid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> set_session_reminder_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid,
                                               @Query("sessionid") String sessionid, @Query("speakerid") String speakerid,
                                               @Query("ip_address") String ip_address);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> delete_session_reminder_api( @Query("reqAction") String reqAction, @Query("userregid") String userregid,
                                                  @Query("sessionid") String sessionid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> session_speaker_list_api( @Query("reqAction") String reqAction, @Query("agendaid") String agendaid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> session_video_api(@Query("reqAction") String reqAction, @Query("videotypeid") String videotypeid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> speaker_video_api(@Query("reqAction") String reqAction, @Query("videotypeid") String videotypeid);


//    new apis

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> agenda_session_list_api(@Query("reqAction") String reqAction, @Query("agendasession") String agendasession,
                                             @Query("agendaid") String agendaid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> agenda_speaker_list_api(@Query("reqAction") String reqAction, @Query("agendaid") String agendaid);


    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> agenda_navigation_api(@Query("reqAction") String reqAction, @Query("agendaid") String agendaid, @Query("eventid") String eventid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> pages_api(@Query("reqAction") String reqAction, @Query("pageid") String pageid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> topic_master_api (@Query("reqAction") String reqAction);

    @POST(ApiUrl.BASE_URL)
    Call<JsonObject> ask_question_api(@Query("reqAction") String reqAction, @Query("userregid") String userregid,
                                      @Query("question")String question, @Query("topicid") String topicid,
                                      @Query("ip_address") String ip_address);


    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> question_list_api (@Query("reqAction") String reqAction);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> hotel_list_api (@Query("reqAction") String reqAction);


    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> hotel_details_gallery_api (@Query("reqAction") String reqAction, @Query("hotelid") String hotelid);

    @GET(ApiUrl.BASE_URL)
    Call<JsonObject> food_dining_session_list_api (@Query("reqAction") String reqAction, @Query("foodid") String foodid);

}
