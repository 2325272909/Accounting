package com.example.accounting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounting.R;
import com.example.accounting.Adapter.adapter;
import com.example.accounting.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Fragment_consume extends Fragment {

    private List<String> mlist = new ArrayList<>();
    private User user;

    public Fragment_consume(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_consume, container, false);
        this.user= (User) getActivity().getIntent().getSerializableExtra("user");  //获取当前登录用户

        adapter myadapter = new adapter(getActivity(),user);
        RecyclerView rcvExpandCollapse = view.findViewById(R.id.consume_recycleView);

        rcvExpandCollapse.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvExpandCollapse.setHasFixedSize(true);
        rcvExpandCollapse.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rcvExpandCollapse.setAdapter(myadapter);

        myadapter.setExpandCollapseDataList(mlist);

        initview();
        return view;
    }

    private void initview(){
        mlist.add("消费类型");
        mlist.add("消费凭证");

    }
}
