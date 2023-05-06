package com.example.accounting.Fragments;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.example.accounting.Activity.Activity_total_consume;
import com.example.accounting.Activity.Activity_total_income;
import com.example.accounting.Chart.EChartOptionUtil;
import com.example.accounting.Chart.EChartView;
import com.example.accounting.R;
import com.example.accounting.entity.CategoryType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 统计fragment对应的控制类
 */
public class Fragment_total extends Fragment {

    private TextView  detail_spending,detail_income;
    private TextView edt_spendingMoney,edt_incomeMoney,edt_balance;
    private TextView calender;
    private User user;
    private Button search;
    private List<CategoryType> data = new ArrayList<>();
    private List<CategoryType> data_income = new ArrayList<>();
    private List< Map<String, Object> > data1 = new ArrayList<>();
    private List< Map<String, Object> > data1_income = new ArrayList<>();
    private EChartView pieChart_consume,pieChart_income;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total, container, false);
    }

    @Override
    public void onResume() {
        Log.i(TAG,"进入统计onResume函数");
        getMonthMoney();  //设置金额

        pieChart_consume.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshPieChart();
            }
        });
        pieChart_income.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshPieChart_income();
            }
        });
        super.onResume();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.user =(User) getActivity().getIntent().getSerializableExtra("user");

        //找组件
        pieChart_consume = getActivity().findViewById(R.id.pieChart_consume);
        pieChart_income = getActivity().findViewById(R.id.pieChart_income);
        calender = getActivity().findViewById(R.id.calender);
        detail_spending = getActivity().findViewById(R.id.detail_spending);
        detail_income = getActivity().findViewById(R.id.detail_income);
        edt_spendingMoney = getActivity().findViewById(R.id.edt_spendingMoney);
        edt_incomeMoney = getActivity().findViewById(R.id.edt_incomeMoney);
        edt_balance = getActivity().findViewById(R.id.edt_balance);
        search=getActivity().findViewById(R.id.search);

        //设置当前时间
        calender.setText(new SimpleDateFormat("yyyy-MM").format(new Date()));

        getMonthMoney();  //设置金额

        pieChart_consume.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshPieChart();
            }
        });
        pieChart_income.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshPieChart_income();
            }
        });

        calender.setOnClickListener(new OnPickMonthClickListener(getActivity(),calender));  //日期监控
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthMoney();   //根据日期统计
                refreshPieChart();
                refreshPieChart_income();
            }
        });

        detail_spending.setOnClickListener(new View.OnClickListener() {  //转到消费详细界面
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("user",user);
                intent.setClass(getActivity(), Activity_total_consume.class);
                startActivity(intent);
            }
        });

        detail_income.setOnClickListener(new View.OnClickListener() {  //转到收入详细界面
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("user",user);
                intent.setClass(getActivity(), Activity_total_income.class);
                startActivity(intent);
            }
        });

    }


    /**
     * 请求消费类型-饼图统计
     */
    private void refreshPieChart(){

        //按类别进行统计，请求每类花销
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/spending/countSpendingCategory";
        Log.i(TAG,"拼接后的url地址："+url);

        String[] temp = calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];

        String url1 = url+"?userId="+userId+"&&year="+year+"&&month="+month;
        Call call =  HttpUtil.getJson(url1);/////////
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
                        String obj = toJsonObj.getString("data");
                        Log.i(TAG,"消费信息:"+obj);
                        data = JSON.parseArray(obj, CategoryType.class);
                        data1.clear();
                        for(CategoryType categoryType:data){
                            HashMap hashMap = new HashMap();
                            hashMap.put("value",categoryType.getMoney());
                            hashMap.put("name",categoryType.getTypename());
                            data1.add(hashMap);
                        }
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                pieChart_consume.refreshEchartsWithOption(EChartOptionUtil.getPieChartOptions(data1,"消费"));
                            }
                        });
                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(getActivity(), toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

//        pieChart_consume.refreshEchartsWithOption(EChartOptionUtil.getPieChartOptions(data1,"消费"));
    }


    /**
     * 请求收入类型-饼图统计
     */
    private void refreshPieChart_income(){

        //按类别进行统计，请求每类花销
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/income/countIncomeCategory";
        Log.i(TAG,"拼接后的url地址："+url);

        String[] temp = calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];

        String url1 = url+"?userId="+userId+"&&year="+year+"&&month="+month;
        Call call =  HttpUtil.getJson(url1);/////////
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
                        String obj = toJsonObj.getString("data");
                        Log.i(TAG,"收入信息:"+obj);
                        data_income = JSON.parseArray(obj, CategoryType.class);
                        data1_income.clear();
                        for(CategoryType categoryType:data_income){
                            HashMap hashMap = new HashMap();
                            hashMap.put("value",categoryType.getMoney());
                            hashMap.put("name",categoryType.getTypename());
                            data1_income.add(hashMap);
                        }
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                pieChart_income.refreshEchartsWithOption(EChartOptionUtil.getPieChartOptions(data1_income,"收入"));
                            }
                        });
                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(getActivity(), toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
//        pieChart_income.refreshEchartsWithOption(EChartOptionUtil.getPieChartOptions(data1_income,"收入"));
    }


    /**
     * 统计所有（按月）
     */
    public void getMonthMoney(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/user/countMonthMoney";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];
        String url1 = url+"?userId="+userId+"&&year="+year+"&&month="+month;    //////////
        Call call =  HttpUtil.getJson(url1);/////////
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
                        Log.i(TAG,"obj:"+obj);
                        Map map = JSON.parseObject(obj.toString(),Map.class);
                        String income = map.get("income").toString();
                        String spending = map.get("spending").toString();
                        String balance = map.get("balance").toString();
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                edt_incomeMoney.setText(income);
                                edt_spendingMoney.setText(spending);
                                edt_balance.setText(balance);
                            }
                        });

                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(getActivity(), toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }


}

