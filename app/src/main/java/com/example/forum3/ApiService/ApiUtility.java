package com.example.forum3.ApiService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.forum3.apputils.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtility {
    private ProgressDialog mProgressDialog = null;
    private String authToken;
    private Context mContext;
    private String baseUrl;
    private String mMessage;
    private boolean mShowProgressDialog;
    private static final ApiUtility ourInstance = new ApiUtility();

    public static final ApiUtility getInstance(){
        return ourInstance;
    }

    private ApiUtility(){

    }

    public ApiUtility(Context mContext, String baseURL, String authToken, String mMessage, boolean showProgressDialog) {
        this.mContext = mContext;
        this.baseUrl = baseURL;
        this.authToken = authToken;
        this.mMessage = mMessage;
        this.mShowProgressDialog = showProgressDialog;
        if (Utility.checkConnection(mContext) && this.mShowProgressDialog) {
            this.mProgressDialog = new ProgressDialog(mContext);
            if (this.mMessage != null && !this.mMessage.isEmpty()) {
                this.mProgressDialog.setMessage(this.mMessage);
            } else {
                this.mProgressDialog.setMessage("Please wait!");
            }

            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setCanceledOnTouchOutside(false);
            this.mProgressDialog.show();
        }
    }


    public Retrofit getRetrofitInstance(){
        OkHttpClient.Builder builder = (new OkHttpClient()).newBuilder();
        builder.readTimeout(200000L, TimeUnit.SECONDS);
        builder.connectTimeout(200000L, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                ongoing.addHeader("Accept", "application/json;versions=1");
                ongoing.addHeader("Content-Type", "application/json; charset=UTF-8");
                ongoing.addHeader("Content-Encoding", "gzip");
                ongoing.addHeader("Authorization", "Bearer " + authToken);
                return chain.proceed(ongoing.build());
            }
        });
        OkHttpClient client = builder.build();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        Retrofit retrofit = (new Retrofit.Builder()).baseUrl(ApiUrl.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }

    private void isInternetConnected(){
        if (!Utility.checkConnection(this.mContext)) {
            //UIToastMessage.show(this.mContext, "No Internet Connection");
        }
    }

    public void getRequestWithRetrofitData(String url, final ApiCallback apiCallback) {
        this.isInternetConnected();
        Retrofit retrofit = this.getRetrofitInstance();
        Api api = (Api)retrofit.create(Api.class);
        Call<JsonObject> responseCall = api.getCommonRequestDataApi(url);
        responseCall.enqueue(new Callback<JsonObject>() {
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    JsonObject loginResponse = (JsonObject)response.body();
                    apiCallback.onResponse(call, response, 0);
                } else {
                }

            }

            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                apiCallback.onFailure(call, t, 0);
            }
        });
    }

    public void getRequestData(String url, final ApiJSONObjCallback apiCallback, final int requestCode) {
        this.isInternetConnected();
        Retrofit retrofit = this.getRetrofitInstance();
        Api api = (Api)retrofit.create(Api.class);
        Call<JsonObject> responseCall = api.getCommonRequestDataApi(url);
        responseCall.enqueue(new Callback<JsonObject>() {
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    JsonObject loginResponse = (JsonObject)response.body();

                    try {
                        JSONObject jsonObject = new JSONObject(loginResponse.toString());
                        apiCallback.onResponse(jsonObject, requestCode);
                    } catch (JSONException var5) {
                        var5.printStackTrace();
                    }
                } else {
                }

            }

            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                apiCallback.onFailure(t, requestCode);
            }
        });
    }

    public void getRequestStringData(String url, final ApiStringCallback apiCallback, final int requestCode) {
        this.isInternetConnected();
        Retrofit retrofit = this.getRetrofitInstance();
        Api api = (Api)retrofit.create(Api.class);
        Call<ResponseBody> responseCall = api.getCommonRequestStringDataApi(url);
        responseCall.enqueue(new Callback<ResponseBody>() {
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    try {
                        String res = ((ResponseBody)response.body()).string();
                        apiCallback.onResponse(res, requestCode);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                } else {
                }

            }

            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                apiCallback.onFailure(t, requestCode);
            }
        });
    }

    public void getJSONArrayRequestData(String url, final ApiArrayCallback apiCallback) {
        this.isInternetConnected();
        Retrofit retrofit = this.getRetrofitInstance();
        Api api = (Api)retrofit.create(Api.class);
        Call<JsonArray> responseCall = api.getCommonRequestJSONArrayDataApi(url);
        responseCall.enqueue(new Callback<JsonArray>() {
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull retrofit2.Response<JsonArray> response) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    JsonArray loginResponse = (JsonArray)response.body();
                    apiCallback.onResponse(call, response, 0);
                } else {
                }

            }

            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                apiCallback.onFailure(call, t, 0);
            }
        });
    }

    public void getRequestDataWithCallback(String url, final ApiCallbackResponse apiCallback) {
        this.isInternetConnected();
        Retrofit retrofit = this.getRetrofitInstance();
        Api api = (Api)retrofit.create(Api.class);
        Call<JsonObject> responseCall = api.getCommonRequestDataApi(url);
        responseCall.enqueue(new Callback<JsonObject>() {
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                JSONObject jsonObject = null;
                if (response.isSuccessful()) {
                    JsonObject serverResponse = (JsonObject)response.body();

                    try {
                        jsonObject = new JSONObject(String.valueOf(serverResponse));
                        apiCallback.onResponse(jsonObject);
                    } catch (JSONException var6) {
                        var6.printStackTrace();
                    }
                } else {
                    apiCallback.onResponse((JSONObject)null);
                }

            }

            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                try {

                    apiCallback.onFailure(new JSONObject(String.valueOf(call)), t);
                } catch (JSONException var4) {
                    var4.printStackTrace();
                }

            }
        });
    }

    public void postRequestCallback(Call<JsonObject> responseCall, final ApiCallbackResponse callbackResponse) {
        this.isInternetConnected();
        responseCall.enqueue(new Callback<JsonObject>() {
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    JsonObject serverResponse = (JsonObject)response.body();

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(serverResponse));
                        callbackResponse.onResponse(jsonObject);
                    } catch (JSONException var5) {
                        var5.printStackTrace();
                    }
                } else {
                    callbackResponse.onResponse((JSONObject)null);
                }

            }

            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                try {
                    callbackResponse.onFailure(new JSONObject(String.valueOf(call)), t);
                } catch (JSONException var4) {
                    var4.printStackTrace();
                }

            }
        });
    }

    public void postRequest(Call<JsonObject> responseCall, final ApiCallbackCode apiCallback, final int requestCode) {
        this.isInternetConnected();
        responseCall.enqueue(new Callback<JsonObject>() {
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (ApiUtility.this.mProgressDialog != null && ApiUtility.this.mProgressDialog.isShowing()) {
                    ApiUtility.this.mProgressDialog.dismiss();
                }
                int code = response.code();
                switch (code){
                    case 200:
                        if (response.isSuccessful()||response.body()!=null) {
                            JsonObject serverResponse = (JsonObject)response.body();
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(serverResponse));
                                apiCallback.onResponse(jsonObject, requestCode, code);
                            } catch (JSONException var5) {
                                var5.printStackTrace();
                            }
                        } else {
                            apiCallback.onFailure(response.body(),"");
                        }
                        break;
                    case 201:
                        apiCallback.onFailure(response.body(),"");
                        break;
                    case 400:
                        apiCallback.onFailure(response.body(),"");
                        break;
                    case 401:
                        apiCallback.onFailure(response.body(),"");
                        break;
                    case 500:
                        apiCallback.onFailure(response.body(),"");
                        break;
                    default:
                        apiCallback.onFailure(response.body(),"");
                        break;
                }
            }

            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                if (ApiUtility.this.mProgressDialog != null && ApiUtility.this.mProgressDialog.isShowing()) {
                    ApiUtility.this.mProgressDialog.dismiss();
                }

                try {
                    apiCallback.onFailure(call, "");
                } catch (Exception var4) {
                    var4.printStackTrace();
                }

            }
        });
    }

    public void showListDialogIndex(JSONArray ja, final int requestCode, String title, String type, Activity act, final AlertEventListener callBackListener) {
        final CharSequence[] items = new CharSequence[ja.length()];
        final List<String> selectedIndexArray = new ArrayList();

        for(int i = 0; i < ja.length(); ++i) {
            try {
                items[i] = ja.getJSONObject(i).getString(type);
                selectedIndexArray.add(ja.getJSONObject(i).getString(type));
            } catch (JSONException var12) {
                var12.printStackTrace();
            }
        }

        AlertDialog.Builder listPicker = new AlertDialog.Builder(act);
        listPicker.setTitle(title);
        listPicker.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callBackListener.didSelectListItem(requestCode, items[which].toString(), (String)selectedIndexArray.get(which));
            }
        });
        listPicker.show();
    }

    public String sanitizeJSONObj(JSONObject jsonObject, String key) {
        String value = "";

        try {
            if (jsonObject.isNull(key)) {
                return "";
            }

            value = jsonObject.getString(key);
            if (TextUtils.isEmpty(value) || value.equals("null")) {
                return "";
            }
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

        return value;
    }


    public static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }
}

