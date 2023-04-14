package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.accounting.Activity.Activity_record_consume.showDatePickerDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private Button btn_add,btn_return;
    AlertDialog.Builder builder_type=null;
    AlertDialog.Builder builder_credential=null;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_consume);
        this.user =(User) getIntent().getSerializableExtra("user");
        spending_money=findViewById(R.id.modify_spending_money);
        spending_stores=findViewById(R.id.modify_spending_stores);
        spending_times=findViewById(R.id.modify_spending_times);
        spending_credential=findViewById(R.id.modify_spending_credential);
        spending_types=findViewById(R.id.modify_spending_types);
        btn_add=findViewById(R.id.btn_modify_add);
        btn_return=findViewById(R.id.btn_modify_return);

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



    }
}
