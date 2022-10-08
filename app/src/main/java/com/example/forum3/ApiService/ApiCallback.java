package com.example.forum3.ApiService;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public interface ApiCallback {
    void onResponse(Call<JsonObject> var1, Response<JsonObject> var2, int var3);

    void onFailure(Object var1, Throwable var2, int var3);
}
