package com.example.accounting.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.accounting.BlankFragment;
import com.example.accounting.R;
import com.example.accounting.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 统计fragment对应的控制类
 */
public class Fragment_total extends Fragment {

    private ViewPager viewPager;  //显示区
    private RadioGroup radioGroup;  //导航栏
    private static final String ARG_user = "userName";
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.user= (User) getActivity().getIntent().getSerializableExtra("user");  //获取当前登录用户
        return inflater.inflate(R.layout.fragment_total, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}

