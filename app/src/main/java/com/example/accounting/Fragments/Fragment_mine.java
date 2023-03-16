package com.example.accounting.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.accounting.Activity.LoginActivity;
import com.example.accounting.Activity.MainActivity;
import com.example.accounting.Activity.ModifyActivity;
import com.example.accounting.Activity.ModifyPasswordActivity;
import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_mine#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_mine extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_user = "userName";

    private User user;
    // TODO: Rename and change types of parameters
    private String mName;
    Button btn_category,btn_modify,btn_exit,btn_modifyPassword;
    TextView textView;


    public Fragment_mine() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Fragment_mine.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_mine newInstance(String userName) {
        Fragment_mine fragment = new Fragment_mine();
        Bundle args = new Bundle();
        args.putString(ARG_user,userName);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_user);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.user =(User) getActivity().getIntent().getSerializableExtra("user");
        btn_category=getActivity().findViewById(R.id.btn_category);
        btn_modify=getActivity().findViewById(R.id.btn_modify);
        btn_exit=getActivity().findViewById(R.id.btn_exit);
        btn_modifyPassword = getActivity().findViewById(R.id.btn_modifyPassword);
        textView=getActivity().findViewById(R.id.textView2);

        textView.setText(user.getUserName());   //当前登录用户名
        Log.i(TAG,"user里的内容："+user);

        //分类展示功能
        btn_category.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.i(TAG,"点击分类按钮");

                Toast.makeText(getActivity(), "分类信息", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 修改功能
         */
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ModifyActivity.class);
                intent.putExtra("user",user);  //传参，要改
                startActivity(intent);
            }

        });

        //退出功能
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退出操作
                String temp_url = URL.url();
                String url = temp_url+"/user/loginout";
                OkHttpClient httpClient = new OkHttpClient();
                MediaType type = MediaType.parse("application/json;charset=utf-8");

                Request getRequest = new Request.Builder()
                    .url(url)
                    .build();
                Call call = httpClient.newCall(getRequest);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        Log.i(MotionEffect.TAG, "post请求失败 \n" );
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        assert response.body() != null;
                        String R = response.body().string();
                        Log.i(MotionEffect.TAG, "okHttpPost enqueue: \n " + "body:" +R);
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), LoginActivity.class);
                                startActivity(intent);

                    }
                });
            }
        });

        btn_modifyPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ModifyPasswordActivity.class);
                intent.putExtra("user",user);  //传参，要改
                startActivity(intent);
            }
        });
    }

}