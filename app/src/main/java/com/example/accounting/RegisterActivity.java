package com.example.accounting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    Button return_login,register;  //返回登录、注册按钮
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register);
        return_login=findViewById(R.id.return_login);

        return_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //实现页面跳转
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }
}
