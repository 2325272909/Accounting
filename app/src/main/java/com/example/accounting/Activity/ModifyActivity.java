package com.example.accounting.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;

import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import com.alibaba.fastjson.JSON; //导入方法依赖的package包/类
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyActivity extends AppCompatActivity {

    private EditText edt_userName,edt_userPhone;
    private Button btn_modify;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        this.user = (User) getIntent().getSerializableExtra("user");

        edt_userName = findViewById(R.id.edt_userName);
        edt_userPhone = findViewById(R.id.edt_userPhone);
        btn_modify = findViewById(R.id.btn_modify);

        //将用户信息显示到屏幕上
        edt_userName.setText(user.getUserName());
        edt_userPhone.setText(user.getUserPhone());

        //响应修改按钮事件
        btn_modify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String temp_url = URL.url();
                String url = temp_url + "/user/update";
                Log.i(TAG, "拼接后的url地址：" + url);   //测试url拼接能否使用
                String username = edt_userName.getText().toString().trim();
                String userPhone = edt_userPhone.getText().toString().trim();
                Long id = user.getId();
                if (username.equals("")) {
                    Toast.makeText(ModifyActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();

                } else if (userPhone.equals("")) {
                    Toast.makeText(ModifyActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();

                } else {
//请求传入的参数
                    JSONObject user = new JSONObject();
                    try {
                        user.put("userName", username);
                        user.put("userPhone", userPhone);
                        user.put("id",id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Call call =  HttpUtil.postJsonObj(url,user);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            Log.i(MotionEffect.TAG, "post请求失败 \n");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            assert response.body() != null;
                            String R = response.body().string();
                            Log.i(MotionEffect.TAG, "okHttpPost enqueue: \n " +
                                "onResponse:" + response.toString() + "\n " +
                                "body:" + R);
                            //将resoust转换成jsonPath 格式
//                            io.restassured.path.json.JsonPath jsonPath =io.restassured.path.json.JsonPath.from(R);
                            try {
                                JSONObject toJsonObj = new JSONObject(R);
                                if (response.code() == 200 && toJsonObj.get("code").equals(1)) {
                                    Object obj = toJsonObj.get("data");
                                    User user = JSON.parseObject(obj.toString(), User.class);  //后端返回来修改后的user
                                    Intent intent = new Intent();
                                    intent.setClass(ModifyActivity.this, MainActivity.class);
                                    intent.putExtra("user", user);  //传参，要改
                                    intent.putExtra("fragment_id",3);
                                    startActivity(intent);
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(ModifyActivity.this, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });

                }
            }


        });

    }

}