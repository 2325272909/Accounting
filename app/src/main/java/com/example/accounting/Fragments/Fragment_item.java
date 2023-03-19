package com.example.accounting.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounting.Adapter.ListItemAdapter;
import com.example.accounting.Adapter.adapter;
import com.example.accounting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类 子类尝试使用fragment加载 item2
 */
public class Fragment_item extends Fragment {
//    private List<String> mlist = new ArrayList<>();
    private List<String> itemsName = new ArrayList<>();

    public Fragment_item(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_item, container, false);

        ListItemAdapter myadapter = new ListItemAdapter(itemsName);
        RecyclerView rcvExpandCollapse = view.findViewById(R.id.rcv_item);  //展开折叠

        rcvExpandCollapse.setLayoutManager(new LinearLayoutManager(getActivity()));  //设置界面垂直|水平布局
        rcvExpandCollapse.setHasFixedSize(true);  //Item的改变不会影响RecyclerView的宽高
        //设置不同子类间的分割线
        rcvExpandCollapse.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rcvExpandCollapse.setAdapter(myadapter);

//        myadapter.setExpandCollapseDataList(itemsName);

        initview();
        return view;
    }

    private void initview(){
        itemsName.add("服装");
    }
}
