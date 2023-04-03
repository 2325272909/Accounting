package com.example.accounting.utils.DataPicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class OnPickDateClickListener implements View.OnClickListener {
    private Context context;
    private EditText editText;

    public OnPickDateClickListener(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        setTextDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String dateStr = editText.getText().toString().trim();
        int monthOfYeartemp = monthOfYear + 1;
        String monthOfYeartempStr = String.valueOf(monthOfYeartemp);
        if (monthOfYeartempStr.length() == 1) {
            monthOfYeartempStr = "0" + monthOfYeartempStr;
        }
        String dayOfMonthtempStr = String.valueOf(dayOfMonth);
        if (dayOfMonthtempStr.length() == 1) {
            dayOfMonthtempStr = "0" + dayOfMonth;
        }
        String dateStrCur = year + "-" + monthOfYeartempStr + "-" + dayOfMonthtempStr;
        if (dateStr.equals(dateStrCur) || null == dateStr) {
            setTextDate(year, monthOfYear, dayOfMonth);
            new DatePickerDialog(context, mDateSetListener, year, monthOfYear, dayOfMonth).show();
        } else {
            int year1 = Integer.parseInt(dateStr.substring(0, 4));
            String monthStr = dateStr.substring(5, 7).toString().trim();
            int monthOfYear1 = -1;
            if (monthStr.startsWith("0")) {
                monthStr = monthStr.substring(1, 2);
                monthOfYear1 = Integer.parseInt(monthStr);
                monthOfYear1 = monthOfYear1 - 1;
            } else {
                monthOfYear1 = Integer.parseInt(monthStr);
                monthOfYear1 = monthOfYear1 - 1;
            }
            String dayStr = dateStr.substring(8, 10);
            int dayOfMonth1 = -1;
            if (dayStr.startsWith("0")) {
                dayStr = dayStr.substring(1, 2);
                dayOfMonth1 = Integer.parseInt(dayStr);
            } else {
                dayOfMonth1 = Integer.parseInt(dayStr);
            }

            new DatePickerDialog(context, mDateSetListener, year1, monthOfYear1, dayOfMonth1).show();
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setTextDate(year, monthOfYear, dayOfMonth);
        }
    };

    private void setTextDate(int year, int monthOfYear, int dayOfMonth) {
        editText.setText(new StringBuilder()
            .append(year).append("-")
            .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)).append("-")
            .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
    }

}
