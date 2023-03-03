package com.example.accounting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accounting.utils.HttpPostRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText name;
    EditText password;
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
                String url = "http://192.168.56.1:8080/user/login";
                String username = name.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if(username.equals("")){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }else if(userPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else {

                    //请求传入的参数

                    RequestBody requestBody = new FormBody.Builder()
                            .add("name", name.getText().toString().trim())
                            .add("password", password.getText().toString().trim())
                            .build();

                    HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("**********" + response.body().string() + "***********");
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            Looper.loop();
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
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}
