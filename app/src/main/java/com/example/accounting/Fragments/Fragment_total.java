package com.example.accounting.Fragments;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.example.accounting.Activity.Activity_total_consume;
import com.example.accounting.Activity.Activity_total_income;
import com.example.accounting.Activity.LoginActivity;
import com.example.accounting.Activity.MainActivity;
import com.example.accounting.BlankFragment;
import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.DataPicker.OnPickDateClickListener;
import com.example.accounting.utils.DataPicker.OnPickMonthClickListener;
import com.example.accounting.utils.DataPicker.OnPickYearClickListener;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 统计fragment对应的控制类
 */
public class Fragment_total extends Fragment {

    TextView  detail_spending,detail_income;
    EditText edt_spendingMoney,edt_incomeMoney,edt_balance,calender;
    private User user;
    BigDecimal pending,income,balance;
    Button search;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.user =(User) getActivity().getIntent().getSerializableExtra("user");
        calender = getActivity().findViewById(R.id.calender);
        detail_spending = getActivity().findViewById(R.id.detail_spending);
        detail_income = getActivity().findViewById(R.id.detail_income);
        edt_spendingMoney = getActivity().findViewById(R.id.edt_spendingMoney);
        edt_incomeMoney = getActivity().findViewById(R.id.edt_incomeMoney);
        edt_balance = getActivity().findViewById(R.id.edt_balance);
        search=getActivity().findViewById(R.id.search);

        calender.setText(new SimpleDateFormat("yyyy-MM").format(new Date()));
        getMonthMoney();
        calender.setOnClickListener(new OnPickMonthClickListener(getActivity(),calender));  //日期监控
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthMoney();   //根据日期统计
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
     * 获取每月消费金额
     * 目前需要改进：get请求传参问题
     */
    public void getMonthSpending(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/spending/countSpendingYearMonthMoney";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];
//        //请求传入的参数
//        JSONObject map = new JSONObject();
//        try{
//            map.put("userId",userId);
//            map.put("year",year);
//            map.put("month",month);
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
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
                        String money = obj.toString();
                        edt_spendingMoney.setText(money);
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




    }

    /**
     * 获取每月收入金额
     * 目前需要改进：get请求传参问题
     */
    public void getMonthIncome(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/income/countIncomeYearMonthMoney";
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
                        String money = obj.toString();
                        edt_incomeMoney.setText(money);
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
                        edt_incomeMoney.setText(income);
                        edt_spendingMoney.setText(map.get("spending").toString());
                        edt_balance.setText(map.get("balance").toString());

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


    }


}

