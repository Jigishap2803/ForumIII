package com.example.forum3.ApiService;

import org.json.JSONObject;

public interface ApiCallbackResponse {
    void onResponse(JSONObject var1);

    void onFailure(JSONObject var1, Throwable var2);
}
