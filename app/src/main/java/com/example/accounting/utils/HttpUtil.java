package com.example.accounting.utils;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

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
    private static HashMap<String,List<Cookie>> cookieStore=new HashMap<>();
    private HttpUtil() {
    }

    /**
     * 发送get请求
     *
     * @param url    地址
     * @param params 参数
     * @return 请求结果
     */
    public static String get(String url, JSONObject params) {
        return request("get", url, params);
    }

    /**
     * 发送post请求
     *
     * @param url    地址
     * @param params 参数
     * @return 请求结果
     */
    public static String post(String url, JSONObject params) {
        return request("post", url, params);
    }

    /**
     * 发送http请求
     *
     * @param method 请求方法
     * @param url    地址
     * @param params 参数
     * @return 请求结果
     */
    public static String request(String method, String url, JSONObject params) {

        if (method == null) {
            throw new RuntimeException("请求方法不能为空");
        }

        if (url == null) {
            throw new RuntimeException("url不能为空");
        }

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

        if (params != null) {
//            for (Map.Entry<String, String> param : params.entrySet()) {
//                httpBuilder.addQueryParameter(param.getKey(), param.getValue());
//            }
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = RequestBody.create(type,""+ params);

        }

        Request request = new Request.Builder()
            .url(httpBuilder.build())
            .method(method, new FormBody.Builder().build())
            .build();

        try {
            OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
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

        try {
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

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getJson(String url, String json) {

        String url1 = url+"?category="+json;
        Request request = new Request.Builder()
            .url(url1)
            .get()
            .build();

        OkHttpClient client = new OkHttpClient();

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

}
