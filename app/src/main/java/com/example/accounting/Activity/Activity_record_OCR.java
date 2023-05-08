package com.example.accounting.Activity;


import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.accounting.R;

import com.example.accounting.entity.User;
import com.example.accounting.utils.ContentHelper;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Activity_record_OCR extends AppCompatActivity {
    private static final int LOGO_IMAGE = 1;
    private static final int BKG_IMAGE = 0;
    private ImageView imgv;
    private TextView tvshow;
    private Button btnocr,btn_back,selectPicture;
    private Bitmap bmp;
    private User user;

    //如果Android的版本大于23，路径取根目录下的tesserart，小于的话是
    //在mnt/sdcard下面
    private String DATAPATH=Environment.getExternalStorageDirectory().getAbsolutePath() +
        File.separator + "tesserart";
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    // private ImageView welcomeImg = null;
    private static final int PERMISSION_REQUEST = 1;
  // 检查权限,先不使用
    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(Activity_record_OCR.this, permissions, PERMISSION_REQUEST);
        }
    }
    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        checkPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_ocr);


        this.user =(User) getIntent().getSerializableExtra("user");
        imgv=findViewById(R.id.imgv);
        btn_back=findViewById(R.id.btn_back);
        selectPicture = findViewById(R.id.selectPicture);

        //获取自己的图片，这里是自己放在项目里面的。
        bmp= BitmapFactory.decodeResource(this.getResources(), R.drawable.card);
        imgv.setImageBitmap(bmp);
        imgv.setScaleType(ImageView.ScaleType.FIT_XY);
        btnocr=findViewById(R.id.btnOcr);

        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               select_click(view);
            }
        });

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
               //请求后端ocr识别，获取数据
               Bitmap photo = Bitmap.createBitmap(imgv.getDrawingCache());
               if(photo==null){
                   Toast.makeText(Activity_record_OCR.this, "图片为空", Toast.LENGTH_SHORT).show();
               }else{
                   //图片二进制转换

               }

//                try {
//                    TessBaseAPI mTess=new TessBaseAPI();
//                    //存放tessdata的文件路径 就是chi_sim.traineddata文件的位置chi_sim.traineddata
//                    //选择语言 chi_sim 简体中文  eng 英文
//                    String language="chi_sim";
//
//
//                    File dirdatapath=new File(DATAPATH);
//                    if (!dirdatapath.exists()) {
//                        dirdatapath.mkdirs();
//                    }
//                    File dir=new File(DATAPATH,"tessdata");
//                    if (!dir.exists())
//                        dir.mkdirs();
//                    tvshow.append(DATAPATH+"\r\n");
//                    Log.i(TAG,"OCR识别中的Datapath:"+DATAPATH);
//                    mTess.init(DATAPATH, language);
//
//                    //将图片设置到mTess进行识别
//                    mTess.setImage(bmp);
//                    //获取识别的文字（这里会等一段时间，这里的代码是在主线程的，建议将这部分代码放到子线程）
//                    String result=mTess.getUTF8Text();
//                    tvshow.append("结果为:" + result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    tvshow.append(e.getMessage());
//                }
            }
        });

    }

    void requestPermission() {
        final int REQUEST_CODE=1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);
        }
    }

    public void select_click(View view) {
        //获取图片
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, LOGO_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.getData() != null) {
            Uri imageUri = data.getData();
            ContentResolver resolver = getContentResolver();
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,imageUri);
//                Bitmap  logoImage = BitmapFactory.decodeFile(ContentHelper.absolutePathFromUri(this, imageUri));
                if (photo != null) {
                    //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    imgv.setImageBitmap(photo);
                }
                else{
                    Toast.makeText(this, "图片为空.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to add the logo image.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
