package com.example.accounting.utils.DataPicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class OnPickYearClickListener implements View.OnClickListener {
    private Context context;
    private EditText editText;

    public OnPickYearClickListener(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        setTextYear(year);
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
        ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
        //隐藏日
        ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);

        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setTextYear(year);
        }
    };

    private void setTextYear(int year) {
        editText.setText(new StringBuilder().append(year));
    }

}
