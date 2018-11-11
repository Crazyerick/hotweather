package com.erick.hotweather.util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by erickxhyang on 2018/10/16.
 */

public class HttpUtil {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String httpGet(String url) {
        String responseData = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                responseData = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    public static void httpGet(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String httpPost(String url, String json) {
        String responseData = "";
        OkHttpClient client = new OkHttpClient();
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                responseData = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    public static void httpPost(String url, String json, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
