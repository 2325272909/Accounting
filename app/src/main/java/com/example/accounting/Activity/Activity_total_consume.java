package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.accounting.Adapter.Total_consume_Adapter;
import com.example.accounting.R;
import com.example.accounting.entity.Spending;
import com.example.accounting.entity.User;
import com.example.accounting.utils.DataPicker.OnPickMonthClickListener;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_total_consume extends AppCompatActivity {

    private User user;  //存储用户信息
    EditText calender,edt_spending;
    Button search,btn_income,btn_spendingChart;
    TextView btn_back;
    private List<Spending> spendingList =new ArrayList<>();
    private RecyclerView recyclerView;
    private Total_consume_Adapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_consume);
        this.user = (User) getIntent().getSerializableExtra("user");
        calender=findViewById(R.id.calender);
        edt_spending = findViewById(R.id.edt_spending);
        search = findViewById(R.id.search);
        btn_income=findViewById(R.id.btn_income);
        btn_spendingChart = findViewById(R.id.btn_spendingChart);
        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.consume_recycleView);
        myadapter = new Total_consume_Adapter(Activity_total_consume.this,user);
        calender.setText(new SimpleDateFormat("yyyy-MM").format(new Date()));

        getMonthSpending();
        getMonthSpendingList();

        calender.setOnClickListener(new OnPickMonthClickListener(Activity_total_consume.this,calender));  //日期监控
        /**
         * 查询实现
         */
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthSpending();
                getMonthSpendingList();
            }
        });




        /**
         * 返回主界面
         */
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_total_consume.this, MainActivity.class);
                intent.putExtra("user",user);  //传参，要改
                intent.putExtra("fragment_id",2);
                startActivity(intent);
            }
        });

        /**
         * 进入收入界面
         */
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_total_consume.this, Activity_total_income.class);
                intent.putExtra("user",user);  //传参，要改
                startActivity(intent);
            }
        });


    }

    /**
     * 获取每月消费金额
     */
    public void getMonthSpending(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/spending/countSpendingYearMonthMoney";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];

        String url1 = url+"?userId="+userId+"&&year="+year+"&&month="+month;
        Call call =  HttpUtil.getJson(url1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "post请求失败 \n" );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String R = response.body().string();
                Log.i(TAG, "okHttpPost enqueue: \n " + "body:" +R);

                try {
                    JSONObject toJsonObj= new JSONObject(R);
                    if( toJsonObj.get("code").equals(1)){
                        Object obj = toJsonObj.get("data");
                        String money = obj.toString();
                        Activity_total_consume.this.runOnUiThread(new Runnable() {
                            public void run() {
                                edt_spending.setText(money);
                            }
                        });

                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(Activity_total_consume.this,toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });




    }

    /**
     * 尝试使用同步请求获取数据
     */
    public void getMonthSpendingList(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/spending/listYearMonth";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];
        String url1 = url+"?userId="+userId+"&&year="+year+"&&month="+month;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url1).build();
                Call call = okHttpClient.newCall(request);
                try{
                    //同步请求要创建子线程,是因为execute()方法，会阻塞后面代码的执行
                    //只有执行了execute方法之后,得到了服务器的响应response之后，才会执行后面的代码
                    //所以同步请求要在子线程中完成
                    Response response = call.execute();
                    String R= response.body().string();

                    Log.i(TAG,"response:"+R);
                    JSONObject jsonObject= new JSONObject(R);
                    if( jsonObject.get("code").equals(1)) {
                        String obj = jsonObject.getString("data");
                        Log.i(TAG, "消费信息:" + obj);
                        spendingList = JSON.parseArray(obj, Spending.class);
                        Activity_total_consume.this.runOnUiThread(new Runnable() {
                            public void run() {
                                drawSpendingList();
                            }
                        });

                    }else if(jsonObject.get("code").equals(0)){
                        Looper.prepare();
                        Log.i(TAG,"spendingList无数据");
                        spendingList = new ArrayList<>();
                        Activity_total_consume.this.runOnUiThread(new Runnable() {
                            public void run() {
                                drawSpendingList();
                            }
                        });
                        Toast.makeText(Activity_total_consume.this, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }

                }catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    public void drawSpendingList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_total_consume.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(Activity_total_consume.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myadapter);
        myadapter.setDataList(spendingList);
    }



}
