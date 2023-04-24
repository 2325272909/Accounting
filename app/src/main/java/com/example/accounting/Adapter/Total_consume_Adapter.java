package com.example.accounting.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounting.Activity.Activity_modify_consume;
import com.example.accounting.R;
import com.example.accounting.entity.Spending;
import com.example.accounting.entity.User;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Total_consume_Adapter extends RecyclerView.Adapter<Total_consume_Adapter.viewHolder>{
    private List<Spending> spendingList ;

    private Context context;
    private User user;

    private Long spendingId;

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
        spendingId = spendingItem.getId();
        Log.i(TAG,"消费时间:"+spendingItem.getSpendingTime());
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, Activity_modify_consume.class);
                intent.putExtra("user", user);  //传参，要改
                intent.putExtra("spending",spendingItem);
                context.startActivity(intent);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"spendingId:"+spendingId);
                try {
                    deleteSpending(spendingId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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

    public void deleteSpending(Long spendingId) throws JSONException {

        String temp_url = URL.url();
        JSONObject bodyParams =  new JSONObject();
        try {
            bodyParams.put("spendingId",""+spendingId);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String url = temp_url+"/spending/delete_spending";

        //   ..........待续（deletMapping对应什么请求？）
        Call call = HttpUtil.deleteJsonObj(url,bodyParams);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(MotionEffect.TAG, "delete请求失败 \n" +
                    "*****bodyParams里的数据***** \n"+bodyParams);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String R = response.body().string();
                try {
                    JSONObject toJsonObj = new JSONObject(R);

                    if(response.code()==200 ){
                        Looper.prepare();
                        Toast.makeText(context,"删除成功", Toast.LENGTH_SHORT).show();

                    } else {
                        Looper.prepare();
                        Toast.makeText(context, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
