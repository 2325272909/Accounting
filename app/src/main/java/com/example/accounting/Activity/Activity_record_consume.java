package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Activity_record_consume extends AppCompatActivity {

    private EditText spending_money,spending_stores,spending_times;
    private List<String> temp_spendingCredentials = new ArrayList<>();
    private List<String> temp_spendingtypes = new ArrayList<>();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private EditText spending_credential,spending_types;
    private Button btn_add,btn_return;
    AlertDialog.Builder builder_type=null;
    AlertDialog.Builder builder_credential=null;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user =(User) getIntent().getSerializableExtra("user");
        setContentView(R.layout.activity_record_consume);
        spending_money=findViewById(R.id.spending_money);
        spending_stores=findViewById(R.id.spending_stores);
        spending_times=findViewById(R.id.spending_times);
        spending_credential=findViewById(R.id.spending_credential);
        spending_types=findViewById(R.id.spending_types);
        btn_add=findViewById(R.id.btn_add);
        btn_return=findViewById(R.id.btn_return);

        /**
         * 时间选择器
         */
        spending_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(Activity_record_consume.this,  2, spending_times, calendar);;
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
                Call call = HttpUtil.getJson(url,"消费凭证");
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
                    builder_credential= new AlertDialog.Builder(Activity_record_consume.this);
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
         * 选择消费类型
         */
        spending_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_url = URL.url();
                String url = temp_url+"/user/item/list";
                Call call = HttpUtil.getJson(url,"消费类型");
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
                    builder_type = new AlertDialog.Builder(Activity_record_consume.this);
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
         * 返回主界面
         */
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_record_consume.this, MainActivity.class);
                intent.putExtra("fragment_id",1);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        /**
         * 添加消费记录
         */
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = GenerateID.generateID();  //自生成id
                Long userId = user.getId();  //关联用户id
                BigDecimal spendingMoney = new BigDecimal(spending_money.getText().toString().trim());  //消费金额
                String spendingStore = spending_stores.getText().toString().trim(); //消费商家，可为空
                String temp_spendingTime = spending_times.getText().toString().trim();
                String spendingCredential = spending_credential.getText().toString().trim();  //消费凭证
                String spendingType = spending_types.getText().toString().trim();  //消费类型
                Date spendingTime = Date.valueOf(temp_spendingTime);

                if(spendingMoney.equals("")||spendingType.equals("")||spendingStore.equals("")||spendingTime.equals("")||spendingCredential.equals("")){
                    Toast.makeText(Activity_record_consume.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject spending = new JSONObject();
                    try {
                        spending.put("id",id);
                        spending.put("userId", userId);
                        spending.put("spendingMoney", spendingMoney);
                        spending.put("spendingTypeName", spendingType);
                        spending.put("spendingCredentialName",spendingCredential);
                        spending.put("spendingTime",spendingTime);
                        spending.put("spendingStores",spendingStore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String temp_url = URL.url();
                    String url = temp_url+"/spending/add";
                    Call call =  HttpUtil.postJsonObj(url,spending);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i(TAG, "post请求失败 \n" + "*****spending里的数据***** \n"+ spending);
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
                                    Toast.makeText(Activity_record_consume.this,"添加成功", Toast.LENGTH_SHORT).show();
//                                    Looper.loop();

                                    Intent intent = new Intent();
                                    intent.setClass(Activity_record_consume.this, MainActivity.class);
                                    intent.putExtra("user",user);
                                    intent.putExtra("fragment_id",1);
                                    startActivity(intent);
                                }
                                else if(response.code()==200&&toJsonObj.get("code").equals("0")){  //请求成功，但结果失败
                                    Looper.prepare();
                                    Toast.makeText(Activity_record_consume.this, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }else{
                                    Looper.prepare();  //后端出错
                                    Toast.makeText(Activity_record_consume.this, toJsonObj.get("error").toString(), Toast.LENGTH_SHORT).show();
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

    /**
     * 日期选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showDatePickerDialog(Activity activity, int themeResId, final EditText tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                String data = year+"-";
                if(monthOfYear+1<10){
                    data+="0"+(monthOfYear+1)+"-";
                }else{
                    data+=""+monthOfYear+1+"-";
                }
                if(dayOfMonth<10){
                    data+="0"+dayOfMonth;
                }else{
                    data+=""+dayOfMonth;
                }
//                tv.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );
                tv.setText(data);
            }

        }
            // 设置初始日期
            , calendar.get(Calendar.YEAR)
            , calendar.get(Calendar.MONTH)
            , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }



}
