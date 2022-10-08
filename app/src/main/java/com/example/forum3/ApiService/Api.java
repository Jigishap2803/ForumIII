package com.example.forum3.ApiService;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {
    @GET
    Call<JsonObject> getCommonRequestDataApi(@Url String var1);

    @GET
    Call<ResponseBody> getCommonRequestStringDataApi(@Url String var1);

    @GET
    Call<JsonArray> getCommonRequestJSONArrayDataApi(@Url String var1);
}
