package com.example.accounting.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.RecyclerView;
import com.example.accounting.R;
import com.example.accounting.entity.IncomeType;
import com.example.accounting.entity.SpendingCredential;
import com.example.accounting.entity.SpendingType;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter  extends RecyclerView.Adapter<ListItemAdapter.viewHolder>{


    private List<SpendingCredential> spendingCredentialList;  //消费凭据列表
    private List<SpendingType> spendingTypeList;   //消费类型列表
    private List<IncomeType> incomeTypeList;   //收入类型列表

    private List<String> itemsName = new ArrayList<>();

    private Context context;


//    public ListItemAdapter(Context context) {
//        this.context  = context;
//    }
    public ListItemAdapter(List<String> list) {
    this.itemsName = list;
}

    @Override
    public ListItemAdapter.viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
       return  new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListItemAdapter.viewHolder holder, int position) {
        String name = itemsName.get(position);
        holder.category_item.setText(name);  //设置按钮名称

        holder.category_item.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //分类点击事件
                Log.i(MotionEffect.TAG,"点击按钮");

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsName.size();
    }

    public static class  viewHolder extends RecyclerView.ViewHolder {
       Button category_item,category_delete;
        public viewHolder(View itemView) {
            super(itemView);
            category_item = itemView.findViewById(R.id.category_item);
            category_delete = itemView.findViewById(R.id.category_delete);
        }
    }
}
