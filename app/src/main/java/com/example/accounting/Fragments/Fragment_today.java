package com.example.accounting.Fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.accounting.Activity.Activity_total_consume;
import com.example.accounting.Activity.Activity_total_income;
import com.example.accounting.Adapter.Total_consume_Adapter;
import com.example.accounting.Adapter.Total_income_Adapter;
import com.example.accounting.Adapter.adapter;
import com.example.accounting.R;
import com.example.accounting.entity.Income;
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
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Fragment_today  extends Fragment {

    private List<String> mlist = new ArrayList<>();
    private User user;
    private RecyclerView recyclerView;
    private RadioButton btn_income,btn_consume;

    private EditText edt_income, edt_consume, edt_total_balance,total_calender;
    Button search;

    Total_consume_Adapter myadapter_consume ;
    Total_income_Adapter myadapter_income;
    private List<Spending> spendingList =new ArrayList<>();
    private List<Income> incomeList =new ArrayList<>();
    public Fragment_today() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.user = (User) getActivity().getIntent().getSerializableExtra("user");
        btn_income = getActivity().findViewById(R.id.btn_income);
        btn_consume = getActivity().findViewById(R.id.btn_consume);

        recyclerView = getActivity().findViewById(R.id.recycleView);
        myadapter_consume =  new Total_consume_Adapter(getActivity(),user);
        myadapter_income =new Total_income_Adapter(getActivity(),user);
        edt_income = getActivity().findViewById(R.id.edt_income);
        edt_consume = getActivity().findViewById(R.id.edt_consume);
        total_calender = getActivity().findViewById(R.id.total_calender);
        edt_total_balance = getActivity().findViewById(R.id.edt_total_balance);
        search = getActivity().findViewById(R.id.search);

        total_calender.setText(new SimpleDateFormat("yyyy-MM").format(new Date())); //设置日期
        getMonthMoney();   //提前请求

        getMonthSpendingList();
        getMonthSpendingList();   //获取两遍数据

        getMonthIncomeList();  //提前获取数据

        drawSpendingList();  //渲染消费列表

        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_consume.setChecked(false);
                getMonthIncomeList();
                drawIncomeList();
            }
        });

        btn_consume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btn_income.setChecked(false);
                getMonthSpendingList();
                drawSpendingList();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthMoney();
                if(btn_income.isChecked()){
                    getMonthIncomeList();
                    drawIncomeList();
                }else{
                    getMonthSpendingList();
                    drawSpendingList();
                }
            }
        });

        total_calender.setOnClickListener(new OnPickMonthClickListener(getActivity(),total_calender));  //日期监控

    }


    /**
     * 统计所有（按月）
     */
    public void getMonthMoney(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/user/countMonthMoney";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = total_calender.getText().toString().split("-");
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
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                edt_income.setText(income);
                                edt_consume.setText(map.get("spending").toString());
                                edt_total_balance.setText(map.get("balance").toString());
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


    }

    /**
     * 获取每月消费记录
     */
    public void getMonthSpendingList(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/spending/listYearMonth";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = total_calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];
        String url1 = url+"?userId="+userId+"&&year="+year+"&&month="+month;
        Call call =  HttpUtil.getJson(url1);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "post请求失败 \n");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String R = response.body().string();
                Log.i(TAG, "okHttpPost enqueue: \n " + "body:" +R);
                try {
                    JSONObject jsonObject= new JSONObject(R);
                    if( jsonObject.get("code").equals(1)){
                        String obj = jsonObject.getString("data");
                        Log.i(TAG,"消费信息:"+obj);
                        spendingList = JSON.parseArray(obj, Spending.class);
                    }
                    else if(jsonObject.get("code").equals(0)){
                        Looper.prepare();
                        Log.i(TAG,"spendingList无数据");
                        spendingList = new ArrayList<>();
                        Toast.makeText(getActivity(), jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    /**
     * 获取每月收入记录
     */
    public void getMonthIncomeList(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/income/listIncomeYearMonth";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = total_calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];
        String url1 = url+"?userId="+userId+"&&year="+year+"&&month="+month;
        Call call =  HttpUtil.getJson(url1);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "post请求失败 \n");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String R = response.body().string();
                Log.i(TAG, "okHttpPost enqueue: \n " + "body:" +R);
                try {
                    JSONObject jsonObject= new JSONObject(R);
                    if( jsonObject.get("code").equals(1)){
                        String obj = jsonObject.getString("data");
                        Log.i(TAG,"收入信息:"+obj);
                        incomeList = JSON.parseArray(obj, Income.class);
                    }
                    else if(jsonObject.get("code").equals(0)){
                        Looper.prepare();
                        Log.i(TAG,"spendingList无数据");
                        incomeList = new ArrayList<>();
                        Toast.makeText(getActivity(), jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 渲染消费recyclerView
     */
    public void drawSpendingList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myadapter_consume);
        myadapter_consume.setDataList(spendingList);
    }

    /**
     * 渲染收入recyclerView
     */
    public void drawIncomeList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myadapter_income);
        myadapter_income.setDataList(incomeList);
    }


}
