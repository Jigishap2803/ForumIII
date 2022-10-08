package com.example.forum3.ApiService;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Response;

public interface ApiArrayCallback {
    void onResponse(Call<JsonArray> var1, Response<JsonArray> var2, int var3);

    void onFailure(Call<JsonArray> var1, Throwable var2, int var3);
}
