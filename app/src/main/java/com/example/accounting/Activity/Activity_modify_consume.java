package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.accounting.Activity.Activity_record_consume.showDatePickerDialog;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.accounting.entity.Spending;
import com.example.accounting.entity.User;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Activity_modify_consume extends AppCompatActivity {

    private EditText spending_money,spending_stores,spending_times;
    private List<String> temp_spendingCredentials = new ArrayList<>();
    private List<String> temp_spendingtypes = new ArrayList<>();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private EditText spending_credential,spending_types;
    private Button btn_modify,btn_return;
    AlertDialog.Builder builder_type=null;
    AlertDialog.Builder builder_credential=null;
    private User user;
    private Spending spending;  //消费信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_consume);
        this.user =(User) getIntent().getSerializableExtra("user");
        this.spending = (Spending)getIntent().getSerializableExtra("spending");

        spending_money=findViewById(R.id.modify_spending_money);
        spending_stores=findViewById(R.id.modify_spending_stores);
        spending_times=findViewById(R.id.modify_spending_times);
        spending_credential=findViewById(R.id.modify_spending_credential);
        spending_types=findViewById(R.id.modify_spending_types);
        btn_modify=findViewById(R.id.btn_modify_add);
        btn_return=findViewById(R.id.btn_modify_return);

        drawSpending(spending);

        /**
         * 返回消费列表展示界面
         */
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_modify_consume.this, Activity_total_consume.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });


        /**
         * 选择消费类型
         */
        spending_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_url = URL.url();
                String url = temp_url+"/user/item/list";
                String url1 = url+"?category="+"消费类型";
                Call call = HttpUtil.getJson(url1);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "post请求失败 \n" );
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        assert response.body() != null;
                        String R = response.body().string();
                        Log.i(TAG, "okHttpPost enqueue: \n " +
                            "onResponse:"+ response.toString() +"\n " +
                            "body:" +R);

                        try {
                            List<String> temp_items1 = new ArrayList<>();
                            JSONObject toJsonObj= new JSONObject(R);
                            JSONArray jsonArray = toJsonObj.getJSONArray("data");
                            for(int i = 0;i<jsonArray.length();i++){
                                temp_items1.add((String)jsonArray.get(i));
                            }
                            temp_spendingtypes = temp_items1;


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                if(builder_type==null){
                    builder_type = new AlertDialog.Builder(Activity_modify_consume.this);
                    builder_type.setTitle("选择消费类型");
                }

                String[] items = temp_spendingtypes.toArray(new String[temp_spendingtypes.size()]);  //list转数组
                builder_type.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        spending_types.setText(items[i]);
                    }
                });
                builder_type.create().show();
            }
        });

        /**
         * 选择消费凭证
         */
        spending_credential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp_url = URL.url();
                String url = temp_url+"/user/item/list";
                String url1 = url+"?category="+"消费凭证";
                Call call = HttpUtil.getJson(url1);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "post请求失败 \n" );
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        assert response.body() != null;
                        String R = response.body().string();
                        Log.i(TAG, "okHttpPost enqueue: \n " +
                            "onResponse:"+ response.toString() +"\n " +
                            "body:" +R);

                        try {
                            List<String> temp_items1 = new ArrayList<>();
                            JSONObject toJsonObj= new JSONObject(R);
                            JSONArray jsonArray = toJsonObj.getJSONArray("data");
                            for(int i = 0;i<jsonArray.length();i++){
                                temp_items1.add((String)jsonArray.get(i));
                            }
                            temp_spendingCredentials = temp_items1;

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                if(builder_credential==null){
                    builder_credential= new AlertDialog.Builder(Activity_modify_consume.this);
                    builder_credential.setTitle("选择消费凭证");
                }
                String[] items = temp_spendingCredentials.toArray(new String[temp_spendingCredentials.size()]);  //list转数组
                builder_credential.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        spending_credential.setText(items[i]);
                    }
                });
                builder_credential.show();

            }
        });

        /**
         * 时间选择器
         */
        spending_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(Activity_modify_consume.this,  2, spending_times, calendar);;
            }
        });

        /**
         * 修改消费记录
         */
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateConsume(spending);
            }
        });



    }

    public void updateConsume(Spending s){
        Long id = s.getId();
        Long userId = user.getId();  //关联用户id
        String spendingMoney = spending_money.getText().toString().trim();  //消费金额
        String spendingStore = spending_stores.getText().toString().trim(); //消费商家，可为空
        String temp_spendingTime = spending_times.getText().toString().trim();
        String spendingCredential = spending_credential.getText().toString().trim();  //消费凭证
        String spendingType = spending_types.getText().toString().trim();  //消费类型

        if(spendingMoney.equals("")||spendingType.equals("")||spendingStore.equals("")|| temp_spendingTime.equals("")||spendingCredential.equals("")){
            Toast.makeText(Activity_modify_consume.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject spending = new JSONObject();
            try {
                spending.put("id",id);
                spending.put("userId", userId);
                spending.put("spendingMoney", spendingMoney);
                spending.put("spendingTypeName", spendingType);
                spending.put("spendingCredentialName",spendingCredential);
                spending.put("spendingTime", temp_spendingTime);
                spending.put("spendingStores",spendingStore);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String temp_url = URL.url();
            String url = temp_url + "/spending/updateSpending";
            Log.i(ContentValues.TAG, "拼接后的url地址：" + url);   //测试url拼接能否使用

            Call call =  HttpUtil.postJsonObj(url,spending);
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
                                Intent intent = new Intent();
                                intent.setClass(Activity_modify_consume.this, MainActivity.class);
                                intent.putExtra("user", user);  //传参，要改
                                intent.putExtra("fragment_id",0);
                                intent.putExtra("flag",0);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Activity_modify_consume.this, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            }
        }


    public void drawSpending(Spending s){
        spending_money.setText(String.valueOf(s.getSpendingMoney()));
        spending_stores.setText(s.getSpendingStores());
        spending_times.setText(s.getSpendingTime());
        spending_credential.setText(s.getSpendingCredentialName());
        spending_types.setText(s.getSpendingTypeName());
    }
}


