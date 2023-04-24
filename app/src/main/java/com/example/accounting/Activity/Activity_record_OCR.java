package com.example.accounting.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.accounting.R;

import com.example.accounting.entity.User;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class Activity_record_OCR extends AppCompatActivity {
    private ImageView imgv;
    private TextView tvshow;
    private Button btnocr,btn_back;
    private Bitmap bmp;
    private User user;

    //如果Android的版本大于23，路径取根目录下的tesserart，小于的话是
    //在mnt/sdcard下面
    private String DATAPATH=Environment.getExternalStorageDirectory().getAbsolutePath() +
        File.separator + "tesserart";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_ocr);
//        requestPermission();
        this.user =(User) getIntent().getSerializableExtra("user");
        imgv=findViewById(R.id.imgv);
        btn_back=findViewById(R.id.btn_back);
        //获取自己的图片，这里是自己放在项目里面的。
        bmp= BitmapFactory.decodeResource(this.getResources(), R.drawable.card);
        imgv.setImageBitmap(bmp);
        imgv.setScaleType(ImageView.ScaleType.FIT_XY);


        tvshow=findViewById(R.id.tvshow);
        btnocr=findViewById(R.id.btnOcr);

       btn_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent();
               intent.setClass(Activity_record_OCR.this,MainActivity.class);
               intent.putExtra("user",user);
               intent.putExtra("fragment_id",1);
               startActivity(intent);
           }
       });
        btnocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    TessBaseAPI mTess=new TessBaseAPI();
                    //存放tessdata的文件路径 就是chi_sim.traineddata文件的位置chi_sim.traineddata
                    //选择语言 chi_sim 简体中文  eng 英文
                    String language="chi_sim";


                    File dirdatapath=new File(DATAPATH);
                    if (!dirdatapath.exists()) {
                        dirdatapath.mkdirs();
                    }
                    File dir=new File(DATAPATH,"tessdata");
                    if (!dir.exists())
                        dir.mkdirs();
                    tvshow.append(DATAPATH+"\r\n");
                    mTess.init(DATAPATH, language);
                    //将图片设置到mTess进行识别
                    mTess.setImage(bmp);
                    //获取识别的文字（这里会等一段时间，这里的代码是在主线程的，建议将这部分代码放到子线程）
                    String result=mTess.getUTF8Text();
                    tvshow.append("结果为:" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                    tvshow.append(e.getMessage());
                }
            }
        });


    }


    void requestPermission() {
        final int REQUEST_CODE=1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);
        }
    }

}
