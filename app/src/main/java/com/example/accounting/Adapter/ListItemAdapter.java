package com.example.accounting.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounting.Activity.Activity_record_income;
import com.example.accounting.Activity.LoginActivity;
import com.example.accounting.Activity.MainActivity;
import com.example.accounting.R;
import com.example.accounting.entity.IncomeType;
import com.example.accounting.entity.SpendingCredential;
import com.example.accounting.entity.SpendingType;
import com.example.accounting.entity.User;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ListItemAdapter  extends RecyclerView.Adapter<ListItemAdapter.viewHolder>{

    private List<String> itemsName ;

    private Context context;
    private String category;
    private User user;

    public ListItemAdapter(Context context,String category,User user) {
        this.context  = context;
        this.category = category;
        this.user = user;
    }

    public void setDataList(List<String> list) {
        itemsName = list;  //设置展开折叠列表
        notifyDataSetChanged();
    }

    @Override
    public ListItemAdapter.viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
//       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
        View v = LayoutInflater.from(context).inflate(R.layout.item2,null);
        return  new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListItemAdapter.viewHolder holder, int position) {
        String name = itemsName.get(position);
        holder.category_item.setText(name);  //设置按钮名称

        holder.category_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //点击删除按钮,删除分类，需要传参：用户id，catrgoty,items_name
                String temp_url = URL.url();
                JSONObject bodyParams =  new JSONObject();
                try {
                    bodyParams.put("userId",""+user.getId());
                    bodyParams.put("category",category);
                    bodyParams.put("itemName",name);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String url = temp_url+"/user/deleteItem";

                //   ..........待续（deletMapping对应什么请求？）
                Call call = HttpUtil.deleteJsonObj(url,bodyParams);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "post请求失败 \n" +
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
//                                    Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(context, toJsonObj.get("msg").toString(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

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
