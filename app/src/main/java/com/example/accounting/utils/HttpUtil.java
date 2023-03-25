package com.example.accounting.utils;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;



import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * okhttp3 请求工具类
 */
import okhttp3.*;

public class HttpUtil {
    private static String R;
    public static HashMap<String,List<Cookie>> cookieStore=new HashMap<>();
    private HttpUtil() {
    }


    /**
     * Post请求被
     * @param url
     * @param params
     * @return
     */
    public static Call postJsonObj(String url, JSONObject params) {

        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type,""+ params);

        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();

        OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(),cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null?cookies: new ArrayList<>();
                }
            }).build();
        Call call = client.newCall(request);
        return call;
    }

    /**
     * 发送post请求（json格式）
     *
     * @param url  url
     * @param json json字符串
     * @return 请求结果
     */
    public static String postJson(String url, String json) {
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, json);

        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();

        OkHttpClient client = new OkHttpClient.Builder().
            cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(),cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null?cookies:new ArrayList<Cookie>();
                }
            }).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "post请求失败 \n" );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                R = response.body().string();
                Log.i(TAG, "okHttpPost enqueue: \n " +
                    "onResponse:"+ response.toString() +"\n " +
                    "body:" +R);

            }
        });
        return R;
    }

    public static Call getJson(String url, String json) {

        String url1 = url+"?category="+json;
        Request request = new Request.Builder()
            .url(url1)
            .get()
            .build();

        OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(),cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null?cookies: new ArrayList<>();
                }
            }).build();

        Call call = client.newCall(request);

        return call;
    }

}
