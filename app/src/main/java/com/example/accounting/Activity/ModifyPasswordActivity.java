package com.example.accounting.Activity;

import static android.content.ContentValues.TAG;

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
import androidx.constraintlayout.helper.widget.MotionEffect;

import com.alibaba.fastjson.JSON;
import com.example.accounting.R;
import com.example.accounting.entity.User;
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

public class ModifyPasswordActivity extends AppCompatActivity {
    private EditText edt_password,edt_repeat;
    private Button btn_modify;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypassword);
        this.user = (User) getIntent().getSerializableExtra("user");

        btn_modify = findViewById(R.id.btn_modify);
        edt_password = findViewById(R.id.edt_newPassword);
        edt_repeat = findViewById(R.id.edt_repeatPassword);


        //响应修改按钮事件
        btn_modify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String temp_url = URL.url();
                String url = temp_url + "/user/update";
                Log.i(TAG, "拼接后的url地址：" + url);   //测试url拼接能否使用
                String password = edt_password.getText().toString().trim();
                String repeat = edt_repeat.getText().toString().trim();
                if (password.equals("")) {
                    Toast.makeText(ModifyPasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();

                } else if (repeat.equals("")) {
                    Toast.makeText(ModifyPasswordActivity.this, "请再输入一次新密码", Toast.LENGTH_SHORT).show();

                } else if(!password.equals(repeat)){
                    Toast.makeText(ModifyPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
//请求传入的参数
                    JSONObject user1 = new JSONObject();
                    try {
                        user1.put("userName",user.getUserName());
                        user1.put("userPhone", user.getUserPhone());
                        user1.put("id",user.getId());
                        user1.put("userPassword",password);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    OkHttpClient httpClient = new OkHttpClient();
                    MediaType type = MediaType.parse("application/json;charset=utf-8");
                    RequestBody requestBody = RequestBody.create(type, "" + user1);

                    Request getRequest = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                    Call call = httpClient.newCall(getRequest);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i(MotionEffect.TAG, "post请求失败 \n" );
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
                                    Intent intent = new Intent();
                                    intent.setClass(ModifyPasswordActivity.this, MainActivity.class);
                                    user.setUserPassword(password);
                                    intent.putExtra("user", user);  //传参，要改
                                    intent.putExtra("fragment_id",3);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(ModifyPasswordActivity.this, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
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
