package com.example.accounting.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accounting.R;

public class Activity_record_OCR extends AppCompatActivity {
    private static final String SD_PATH= Environment.getExternalStorageDirectory().getPath();

    private Button button;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_ocr);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                TessBaseAPI baseApi = new TessBaseAPI();
//                // 指定语言集，sd卡根目录下放置Tesseract的tessdata文件夹
//                baseApi.init(SD_PATH, "eng");
//                // 设置psm模式
//                baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
//
//                editText = (EditText) findViewById(R.id.editText);
//                String img = editText.getText().toString();
//                // 设置图片
//                baseApi.setImage(new File(SD_PATH + img));
//                // 获取结果
//                final String result = baseApi.getUTF8Text();
//                textView.setText(result);
//                // 释放内存
//                baseApi.clear();
//                baseApi.end();

            }
        });
    }
}
