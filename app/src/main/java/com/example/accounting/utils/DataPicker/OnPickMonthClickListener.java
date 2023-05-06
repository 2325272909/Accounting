package com.example.accounting.utils.DataPicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class OnPickMonthClickListener implements View.OnClickListener {
    private Context context;
    private TextView editText;

    public OnPickMonthClickListener(Context context, TextView editText) {
        this.context = context;
        this.editText = editText;
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);

        setTextMonth(year, monthOfYear);
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, mDateSetListener, year, monthOfYear, dayOfMonth);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        //隐藏月份
        //((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
        //隐藏日
        ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = (view, year, monthOfYear, dayOfMonth) -> setTextMonth(year, monthOfYear);

    private void setTextMonth(int year, int monthOfYear) {
        editText.setText(new StringBuilder()
            .append(year).append("-")
            .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)));
    }

}
