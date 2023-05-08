package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.accounting.utils.HttpUtil.cookieStore;

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

import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON; //导入方法依赖的package包/类


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText name;
    private EditText password;

    Button login,register;  //登录、注册按钮

    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.Username);  //获取输入的账号
        password = findViewById(R.id.password);  //获取输入的密码
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp_url = URL.url();
                String url = temp_url+"/user/login";
                Log.i(TAG,"拼接后的url地址："+url);   //测试url拼接能否使用
                String username = name.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if(username.equals("")){
                   Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();

                }else if(userPassword.equals("")){
                   Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();

                }else {

                    //请求传入的参数
                    JSONObject user = new JSONObject();
                    try{
                        user.put("userName",username);
                        user.put("userPassword",userPassword);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    Call call =  HttpUtil.postJsonObj(url,user);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            Log.i(TAG, "post请求失败 \n" +
                                "*****user里的数据***** \n"+
                                user);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            assert response.body() != null;
                            String R = response.body().string();
                            Log.i(TAG, "okHttpPost enqueue: \n " + "body:" +R);

                            try {
                                JSONObject toJsonObj= new JSONObject(R);
                                if( toJsonObj.get("code").equals(1)){
                                   Object obj = toJsonObj.get("data");
                                   User user1 =JSON.parseObject(obj.toString(),User.class);
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user",user1);  //传参，要改
                                    startActivity(intent);
                                }
                                else {
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //实现页面跳转
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}
