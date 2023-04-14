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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.accounting.Activity.Activity_total_consume;
import com.example.accounting.Activity.Activity_total_income;
import com.example.accounting.Adapter.adapter;
import com.example.accounting.R;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment_today  extends Fragment {

    private List<String> mlist = new ArrayList<>();
    private User user;

    EditText edt_income, edt_consume, total_calender, edt_total_balance;
    Button search;

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
        edt_income = getActivity().findViewById(R.id.edt_income);
        edt_consume = getActivity().findViewById(R.id.edt_consume);
        total_calender = getActivity().findViewById(R.id.total_calender);
        edt_total_balance = getActivity().findViewById(R.id.edt_total_balance);
        search = getActivity().findViewById(R.id.search);

        total_calender.setText(new SimpleDateFormat("yyyy-MM").format(new Date())); //设置日期
//        getMonthMoney();

        total_calender.setOnClickListener(new OnPickMonthClickListener(getActivity(),total_calender));  //日期监控

    }

    /**
     * 获取每月消费金额
     */
    public void getMonthSpending() {
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url + "/spending/countSpendingYearMonthMoney";
        Log.i(TAG, "拼接后的url地址：" + url);
        String[] temp = total_calender.getText().toString().split("-");
        String year = temp[0];
        String month = temp[1];

        String url1 = url + "?userId=" + userId + "&&year=" + year + "&&month=" + month;
        Call call = HttpUtil.getJson(url1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "post请求失败 \n");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String R = response.body().string();
                Log.i(TAG, "okHttpPost enqueue: \n " + "body:" + R);

                try {
                    JSONObject toJsonObj = new JSONObject(R);
                    if (toJsonObj.get("code").equals(1)) {
                        Object obj = toJsonObj.get("data");
                        String money = obj.toString();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                edt_consume.setText(money);
                            }
                        });

                    } else {
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
     */
    public void getMonthIncome(){
        Long userId = user.getId();
        String temp_url = URL.url();
        String url = temp_url+"/income/countIncomeYearMonthMoney";
        Log.i(TAG,"拼接后的url地址："+url);
        String[] temp = total_calender.getText().toString().split("-");
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
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                edt_income.setText(money);
                            }
                        });

                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(getActivity(),toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
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
                        edt_income.setText(income);
                        edt_consume.setText(map.get("spending").toString());
                        edt_total_balance.setText(map.get("balance").toString());

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
