package com.example.accounting.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.accounting.Adapter.ListItemAdapter;
import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.DividerItemDecoration;
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

public class Activity_record_consume extends AppCompatActivity {

    private EditText spending_money,spending_stores,spending_times;
    private List<String> temp_items = new ArrayList<>();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private EditText spending_credential,spending_types;
    private Button btn_add,btn_return;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_record_consume.this);
                builder.setTitle("选择消费凭证");

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
                            temp_items = temp_items1;
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                String[] items = temp_items.toArray(new String[temp_items.size()]);  //list转数组
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      spending_credential.setText(items[i]);
                    }
                });
            }
        });

        /**
         * 选择消费类型
         */
        spending_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
                tv.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );
            }
        }
            // 设置初始日期
            , calendar.get(Calendar.YEAR)
            , calendar.get(Calendar.MONTH)
            , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }



}
