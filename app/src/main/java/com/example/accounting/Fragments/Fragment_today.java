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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.accounting.Adapter.Total_consume_Adapter;
import com.example.accounting.Adapter.Total_income_Adapter;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_today  extends Fragment {

    private User user;
    private RecyclerView recyclerView;
    private RadioButton btn_income,btn_consume;

    private EditText edt_income, edt_consume, edt_total_balance,total_calender;
    private Button search;

    private Total_consume_Adapter myadapter_consume ;
    private Total_income_Adapter myadapter_income;
    private List<Spending> spendingList =new ArrayList<>();
    private List<Income> incomeList =new ArrayList<>();
    public Fragment_today() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        search = view.findViewById(R.id.search);
        btn_income = view.findViewById(R.id.btn_income);
        btn_consume = view.findViewById(R.id.btn_consume);
        recyclerView = view.findViewById(R.id.recycleView);
        edt_income = view.findViewById(R.id.edt_income);
        edt_consume = view.findViewById(R.id.edt_consume);
        total_calender = view.findViewById(R.id.total_calender);
        edt_total_balance = view.findViewById(R.id.edt_total_balance);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.user = (User) getActivity().getIntent().getSerializableExtra("user");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        myadapter_consume =  new Total_consume_Adapter(getActivity(),user);
        myadapter_income =new Total_income_Adapter(getActivity(),user);

        total_calender.setText(new SimpleDateFormat("yyyy-MM").format(new Date())); //设置日期

        getMonthMoney();   //请求数据，渲染页面
        getMonthSpendingList();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthMoney();
                if(btn_income.isChecked()){
                    getMonthIncomeList();
                }else{
                    getMonthSpendingList();
                }
            }
        });
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_consume.setChecked(false);
                getMonthIncomeList();
            }
        });

        btn_consume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                btn_income.setChecked(false);
                getMonthSpendingList();
            }
        });


        total_calender.setOnClickListener(new OnPickMonthClickListener(getActivity(),total_calender));  //日期监控

    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        int flag = getActivity().getIntent().getIntExtra("flag",0);
        if(flag==1){
            btn_consume.setChecked(false);
            btn_income.setChecked(true);
            getMonthIncomeList();
        }else if(flag==0){
            btn_consume.setChecked(true);
            btn_income.setChecked(false);
            getMonthSpendingList();
        }
        super.onResume();
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
     * 尝试使用同步请求获取数据
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
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                               drawSpendingList();
                            }
                        });
                    }else if(jsonObject.get("code").equals(0)){
                        Looper.prepare();
                        Log.i(TAG,"spendingList无数据");
                        spendingList = new ArrayList<>();
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                drawSpendingList();
                            }
                        });
                        Toast.makeText(getActivity(), jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }

                }catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url1).build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String R= response.body().string();
                    JSONObject jsonObject= new JSONObject(R);
                    if( jsonObject.get("code").equals(1)){
                        String obj = jsonObject.getString("data");
                        Log.i(TAG,"收入信息:"+obj);
                        incomeList = JSON.parseArray(obj, Income.class);
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                              drawIncomeList();
                            }
                        });
                    }
                    else if(jsonObject.get("code").equals(0)){
                        Looper.prepare();
                        Log.i(TAG,"spendingList无数据");
                        incomeList = new ArrayList<>();
                        requireActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                drawIncomeList();
                            }
                        });
                        Toast.makeText(getActivity(), jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * 渲染消费recyclerView
     */
    public void drawSpendingList(){
        myadapter_consume.setDataList(spendingList);
        recyclerView.setAdapter(myadapter_consume);

    }

    /**
     * 渲染收入recyclerView
     */
    public void drawIncomeList(){
        myadapter_income.setDataList(incomeList);
        recyclerView.setAdapter(myadapter_income);

    }


}
