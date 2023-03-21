package com.example.accounting.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accounting.Adapter.ListItemAdapter;
import com.example.accounting.R;

import java.util.Calendar;
import java.util.Locale;

public class Activity_record_consume extends AppCompatActivity {

    private EditText spending_money,spending_stores,spending_times;

    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private ListView spending_credential,spending_types;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_consume);
        spending_money=findViewById(R.id.spending_money);
        spending_stores=findViewById(R.id.spending_stores);
        spending_times=findViewById(R.id.spending_times);
        spending_credential=findViewById(R.id.spending_credential);
        spending_types=findViewById(R.id.spending_types);

        spending_times.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDatePickerDialog(Activity_record_consume.this,  2, spending_times, calendar);;
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
