package com.example.accounting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                String username = name.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if(username.equals("admin") & userPassword.equals("123456")){
                    Toast.makeText(LoginActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();

                    //实现页面跳转
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);

                    //存储当前用户名，用于账号信息
                    intent.putExtra("username",name.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"登陆失败！",Toast.LENGTH_SHORT).show();
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
