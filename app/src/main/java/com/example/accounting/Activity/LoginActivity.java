package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
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
                String url = "http://10.131.93.59:8080/user/login";
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


                    OkHttpClient httpClient = new OkHttpClient();
                    MediaType type = MediaType.parse("application/json;charset=utf-8");
                    RequestBody requestBody = RequestBody.create(type,""+ user);

                    Request getRequest = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                    Call call = httpClient.newCall(getRequest);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            Log.i(TAG, "post请求失败 \n" +
                                "*********请求体，传送数据*********** \n"+
                                requestBody.toString() + "\n"+
                                "*****user里的数据***** \n"+
                                user);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            assert response.body() != null;
                            String R = response.body().string();
                            Log.i(TAG, "okHttpPost enqueue: \n " +
                                "onResponse:"+ response.toString() +"\n " +
                                "body:" +R);
                            //将resoust转换成jsonPath 格式
//                            io.restassured.path.json.JsonPath jsonPath =io.restassured.path.json.JsonPath.from(R);
                            try {
                                JSONObject toJsonObj= new JSONObject(R);
                                if(response.code()==200 && toJsonObj.get("code").equals(1)){
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, toJsonObj.get("code")+"****"+toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
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
