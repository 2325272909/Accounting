package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accounting.R;
import com.example.accounting.utils.GenerateID;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_userName;
    private EditText edt_userPassword;
    private EditText edt_userPhone;
    private Button return_login,register;  //返回登录、注册按钮
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_userName = findViewById(R.id.edt_userName);
        edt_userPassword = findViewById(R.id.edt_userPassword);
        edt_userPhone = findViewById(R.id.edt_userPhone);
        register = findViewById(R.id.btn_register);
        return_login=findViewById(R.id.btn_returnLogin);


        /**
         * 返回登陆界面功能
         */
        return_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //实现页面跳转
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 注册功能
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edt_userName.getText().toString().trim();
                String userPassword = edt_userPassword.getText().toString().trim();
                String userPhone = edt_userPhone.getText().toString().trim();
                if(userName.equals("")||userPassword.equals("")||userPhone.equals("")){
                    Toast.makeText(RegisterActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject user = new JSONObject();
                    try {
                        String userID= GenerateID.generateID();
                        user.put("id",userID);
                        user.put("userName", userName);
                        user.put("userPassword", userPassword);
                        user.put("userPhone", userPhone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String temp_url = URL.url();
                    String url = temp_url+"/user/register";
                    Call call =  HttpUtil.postJsonObj(url,user);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            Log.i(TAG, "post请求失败 \n" +
                                "*********请求体，传送数据*********** \n"+
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

                            try {
                                JSONObject toJsonObj= new JSONObject(R);
                                if(response.code()==200 && toJsonObj.get("code").equals(1)){
                                    Intent intent = new Intent();
                                    intent.setClass(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
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

    }
}
