package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.accounting.Activity.Activity_record_consume.showDatePickerDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.GenerateID;
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

public class Activity_record_income extends AppCompatActivity {
    private User user;  //用户
    private EditText income_money,income_types,income_times;
    private List<String> temp_incomeType = new ArrayList<>();

    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private Button btn_add,btn_return;
    AlertDialog.Builder builder_type=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user = (User) getIntent().getSerializableExtra("user");
        setContentView(R.layout.activity_record_income);
        income_money=findViewById(R.id.income_money);
        income_times=findViewById(R.id.income_times);
        income_types=findViewById(R.id.income_types);
        btn_add=findViewById(R.id.btn_add);
        btn_return=findViewById(R.id.btn_return);
        getIncomeType();
        /**
         * 时间选择器
         */
        income_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(Activity_record_income.this,  2, income_times, calendar);;
            }
        });

        /**
         * 返回主界面
         */
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_record_income.this, MainActivity.class);
                intent.putExtra("fragment_id",1);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        /**
         * 添加收入记录
         */
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = GenerateID.generateID();  //自生成id
                Long userId = user.getId();  //关联用户id
                BigDecimal incomeMoney = new BigDecimal(income_money.getText().toString().trim());  //消费金额
                String temp_incomeTime = income_times.getText().toString().trim();
                String incomeType = income_types.getText().toString().trim();  //消费类型
                Date incomeTime = Date.valueOf(temp_incomeTime);

                if(incomeMoney.equals("")||incomeType.equals("")|| incomeTime.equals("")){
                    Toast.makeText(Activity_record_income.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject income = new JSONObject();
                    try {
                        income.put("id",id);
                        income.put("userId", userId);
                        income.put("incomeMoney", incomeMoney);
                        income.put("incomeTypeName", incomeType);
                        income.put("incomeTime",incomeTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String temp_url = URL.url();
                    String url = temp_url+"/income/add";
                    Call call =  HttpUtil.postJsonObj(url,income);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i(TAG, "post请求失败 \n" + "*****spending里的数据***** \n"+ income);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            assert response.body() != null;
                            String R = response.body().string();
                            Log.i(TAG, "添加消费信息函数: \n " + "body:" +R);

                            try {
                                JSONObject toJsonObj= new JSONObject(R);
                                if(response.code()==200 && toJsonObj.get("code").equals(1)){
                                    Looper.prepare();
                                    Toast.makeText(Activity_record_income.this,"添加成功", Toast.LENGTH_SHORT).show();
//                                    Looper.loop();

                                    Intent intent = new Intent();
                                    intent.setClass(Activity_record_income.this, MainActivity.class);
                                    intent.putExtra("user",user);
                                    intent.putExtra("fragment_id",1);
                                    startActivity(intent);
                                }
                                else if(response.code()==200&&toJsonObj.get("code").equals("0")){  //请求成功，但结果失败
                                    Looper.prepare();
                                    Toast.makeText(Activity_record_income.this, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }else{
                                    Looper.prepare();  //后端出错
                                    Toast.makeText(Activity_record_income.this, toJsonObj.get("error").toString(), Toast.LENGTH_SHORT).show();
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

        /**
         * 选择消费类型
         */
        income_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIncomeType();
                if(builder_type==null){
                    builder_type = new AlertDialog.Builder(Activity_record_income.this);
                    builder_type.setTitle("选择收入类型");
                }

                String[] items = temp_incomeType.toArray(new String[temp_incomeType.size()]);  //list转数组
                builder_type.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        income_types.setText(items[i]);
                    }
                });
                builder_type.create().show();
            }
        });


    }

    public void getIncomeType(){
        String temp_url = URL.url();
        String url = temp_url+"/income/typeList";
        String url1 = url+"?userid="+ user.getId();
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
                    temp_incomeType = temp_items1;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }


}
