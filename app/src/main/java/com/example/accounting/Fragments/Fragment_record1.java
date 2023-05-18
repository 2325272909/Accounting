package com.example.accounting.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.accounting.Activity.Activity_record_OCR;
import com.example.accounting.Activity.Activity_record_consume;

import com.example.accounting.Activity.Activity_record_income;
import com.example.accounting.Activity.LoginActivity;
import com.example.accounting.R;
import com.example.accounting.entity.User;


/**
 * "记一笔的fragment"
 */
public class Fragment_record1 extends Fragment {
    private Button consume,consume_OCR,income;
    private User user ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record1, container, false);
        consume= view.findViewById(R.id.consume);
        consume_OCR= view.findViewById(R.id.consume_OCR);
        income= view.findViewById(R.id.income);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.user =(User) getActivity().getIntent().getSerializableExtra("user");


        /**
         * 点击录入消费记录
         */
        consume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Activity_record_consume.class);
                intent.putExtra("user",user);  //将登录用户的ID传递给 分类Activity,方便查询与用户关联的分类
                startActivity(intent);
            }
        });

        /**
         * 点击OCR识别
         */
        consume_OCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Activity_record_OCR.class);
                intent.putExtra("user",user);  //将登录用户的ID传递给 分类Activity,方便查询与用户关联的分类
                startActivity(intent);
            }
        });

        /**
         * 点击录入收入记录
         */
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Activity_record_income.class);
                intent.putExtra("user",user);  //将登录用户的ID传递给 分类Activity,方便查询与用户关联的分类
                startActivity(intent);
            }
        });


    }

}
