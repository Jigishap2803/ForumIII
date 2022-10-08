package com.example.forum3.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseModel {
    private boolean is_success;
    private String msg;
    private JSONObject jsonObject;


    public ResponseModel(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }


    public JSONArray getData() {
        try {
            return this.jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isStatus() {
        JSONObject jsonObject = null;
        jsonObject = this.jsonObject;
        if (ApiUtility.getInstance().sanitizeJSONObj(jsonObject, "status").equalsIgnoreCase("200")) {
            is_success = true;
        } else {
            is_success = false;
        }

        return is_success;
    }

    public String getMsg() {
        JSONObject jsonObject = null;
        jsonObject = this.jsonObject;
        msg = ApiUtility.getInstance().sanitizeJSONObj(jsonObject, "response");

        return msg;
    }

}
