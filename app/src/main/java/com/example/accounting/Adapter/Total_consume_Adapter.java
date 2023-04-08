package com.example.accounting.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounting.R;
import com.example.accounting.entity.Spending;
import com.example.accounting.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Total_consume_Adapter extends RecyclerView.Adapter<Total_consume_Adapter.viewHolder>{
    private List<Spending> spendingList ;

    private Context context;
    private User user;

    public Total_consume_Adapter(Context context,User user){
        this.context = context;
        this.user = user;
    }
    public void setDataList(List<Spending> list) {
        spendingList = list;  //设置展开折叠列表
    }

    @NonNull
    @Override
    public Total_consume_Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_total_consume,null);
        return  new Total_consume_Adapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
       Spending spendingItem = spendingList.get(position);  //获取当前位置的消费记录
        holder.edt_spendingMoney.setText(String.valueOf(spendingItem.getSpendingMoney()));  //设置Money
        holder.edt_spendingType.setText(spendingItem.getSpendingTypeName());  //设置消费类型
        holder.edt_spendingTime.setText(spendingItem.getSpendingTime());  //设置消费时间

        Log.i(TAG,"消费时间:"+spendingItem.getSpendingTime());
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"编辑", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"删除", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return spendingList.size()==0 ? 0:spendingList.size();
    }

    public static class  viewHolder extends RecyclerView.ViewHolder {
        Button  btn_edit,btn_delete;
        EditText edt_spendingMoney,edt_spendingType,edt_spendingTime;
        public viewHolder(View itemView) {
            super(itemView);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            edt_spendingMoney = itemView.findViewById(R.id.edt_spendingMoney);
            edt_spendingType = itemView.findViewById(R.id.edt_spendingType);
            edt_spendingTime = itemView.findViewById(R.id.edt_spendingTime);

        }
    }


}
