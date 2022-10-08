package com.example.forum3.ApiService;

import org.json.JSONObject;

public interface ApiCallbackCode {
    void onResponse(JSONObject jsonObject, int request_code, int status_code);

    void onFailure(Object jsonObject, String error_msg);
}
