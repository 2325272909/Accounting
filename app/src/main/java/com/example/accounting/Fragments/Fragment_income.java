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

import com.example.accounting.utils.adapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_income extends Fragment {

    private List<String> mlist = new ArrayList<>();

    public Fragment_income(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_income, container, false);

        adapter myadapter = new adapter(getActivity());
        RecyclerView rcvExpandCollapse = view.findViewById(R.id.rcv_expandcollapse);

        rcvExpandCollapse.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvExpandCollapse.setHasFixedSize(true);
        rcvExpandCollapse.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rcvExpandCollapse.setAdapter(myadapter);

        myadapter.setExpandCollapseDataList(mlist);

        initview();
        return view;
    }

    private void initview(){
        mlist.add("收入类型");

    }
}
