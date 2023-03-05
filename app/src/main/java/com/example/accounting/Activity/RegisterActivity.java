package com.example.accounting.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accounting.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_userName;
    private EditText edt_userPassword;
    private EditText edt_userPhone;
    Button return_login,register;  //返回登录、注册按钮
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.btn_register);
        return_login=findViewById(R.id.btn_returnLogin);



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
                String username = edt_userName.getText().toString().trim();
                String password = edt_userPassword.getText().toString().trim();
                String phone = edt_userPhone.getText().toString().trim();
                if(username.equals("")||password.equals("")||phone.equals("")){
                    Toast.makeText(RegisterActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", username);
                        jsonObject.put("password", password);
                        jsonObject.put("phone", phone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://10.131.93.59:8080/user/register";
//                    RequestQueue requestQueue=Volley.newRequestQueue(RegisterActivity.this);
//                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url,jsonObject, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject jsonObject) {
//                            try {
//                                Log.d("注册信息", jsonObject.toString());
//                                String msg = jsonObject.getString("msg");
//                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
//                                if(msg.equals("注册成功")){
//                                    JSONObject detail = jsonObject.getJSONObject("detail");
//                                    final String username_login = detail.getString("username");
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(RegisterActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    requestQueue.add(jsonObjectRequest);

                }
            }
        });

    }
}
