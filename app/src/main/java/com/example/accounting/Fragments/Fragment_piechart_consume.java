package com.example.accounting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.accounting.Chart.EChartView;
import com.example.accounting.R;
import com.example.accounting.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fragment_piechart_consume extends Fragment {

    private User user;
    List<Map<String, Object>> data = new ArrayList<>();
    private EChartView pieChart_consume;

    public static Fragment_piechart_consume newInstance( ){
        return new Fragment_piechart_consume();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_piechart_consume, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.user =(User) getActivity().getIntent().getSerializableExtra("user");
        pieChart_consume = getActivity().findViewById(R.id.pieChart_consume);

    }
}
