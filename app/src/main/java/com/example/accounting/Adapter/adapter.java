package com.example.accounting.Adapter;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounting.R;
import com.example.accounting.entity.User;
import com.example.accounting.utils.DividerItemDecoration;
import com.example.accounting.utils.GenerateID;
import com.example.accounting.utils.HttpUtil;
import com.example.accounting.utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 通过对list列表数据封装，实现recyclerview中的数据显示
 * 主要作用于分类展示
 */
public class adapter extends RecyclerView.Adapter<adapter.myviewholder> {

    private Context context;
    private List<String> mlist = new ArrayList<>();
    private List<String> itemNames = new ArrayList<>();
    private int expandPosition = -1;
    private myviewholder mViewholder;
    private User user;

    public adapter(Context context,User user) {
        this.context  = context;
        this.user = user;
    }

    public void setExpandCollapseDataList(List<String> list) {
        mlist = list;  //设置展开折叠列表
        notifyDataSetChanged();
    }

    /**
     * 重写方法1，onCreateViewHolder(viewgroup,int)
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @Override
    public adapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new myviewholder(view);
    }


    /**
     * 重写方法2 onBindViewHolder(RecyclerView.ViewHolder,int)
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public  void onBindViewHolder(final adapter.myviewholder viewHolder, int position) {
        viewHolder.tvTeam.setText(mlist.get(position));
        String category = (mlist.get(position) ) ;   //标志
        String temp_url = URL.url();
        String url = temp_url+"/user/item/list";
        String url1 = url+"?category="+category;
        Call call = HttpUtil.getJson(url1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "post请求失败 \n" );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String R = response.body().string();
                Log.i(TAG, "okHttpPost enqueue: \n " +
                    "onResponse:"+ response.toString() +"\n " +
                    "body:" +R);

                try {
                    List<String> temp_items = new ArrayList<>();
                    JSONObject toJsonObj= new JSONObject(R);
                    JSONArray jsonArray = toJsonObj.getJSONArray("data");
                    for(int i = 0;i<jsonArray.length();i++){
                        temp_items.add((String)jsonArray.get(i));
                    }
                    itemNames = temp_items;
                    ListItemAdapter listItemAdapter = new ListItemAdapter(context,category,user);
                    viewHolder.recyclerView.post(new Runnable() {
                        @Override
                        public void run() {   //线程里修改UI
//                            ListItemAdapter listItemAdapter = new ListItemAdapter(context);
                            listItemAdapter.setDataList(itemNames);
                            viewHolder.recyclerView.setAdapter(listItemAdapter);
                            Log.i(TAG,"items数组"+itemNames);
                            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.recyclerView.getContext()));
                            viewHolder.recyclerView.setHasFixedSize(true);
                            viewHolder.recyclerView.setNestedScrollingEnabled(true);
                      //  viewHolder.recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                            viewHolder.recyclerView.addItemDecoration(new DividerItemDecoration(context));

                        }
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });


        final boolean isExpand = position == expandPosition;
        viewHolder.rlChild.setVisibility(isExpand ? View.VISIBLE : View.GONE);
        viewHolder.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewholder !=null){
                    mViewholder.rlChild.setVisibility(View.GONE);
                    notifyItemChanged(expandPosition);
                }
                expandPosition = isExpand ? -1 : viewHolder.getAdapterPosition();
                mViewholder = isExpand ? null : viewHolder;

                notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });

        /**
         * 添加分类
         */
        viewHolder.category_add.setOnClickListener(new View.OnClickListener(){




            @Override
            public void onClick(View view) {
                //需要参数：userId,category
                JSONObject bodyParams = new JSONObject();
                try {
                    bodyParams.put("userId",user.getId());
                    bodyParams.put("category",category);
                    bodyParams.put("id", GenerateID.generateID());
                    bodyParams.put("itemsName","");  //待定
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }


    /**
     * 重写方法3 getItemCount()
     * @return
     */
    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }


    /**
     * 继承recyclerView的父类
     */
    public static class myviewholder extends RecyclerView.ViewHolder {
        RelativeLayout rlParent,rlChild;

        RecyclerView recyclerView;
        TextView tvTeam;

        Button category_add;

        public myviewholder(View itemView) {
            super(itemView);
            rlParent = itemView.findViewById(R.id.rl_parent);
            rlChild = itemView.findViewById(R.id.rl_child);
            tvTeam = itemView.findViewById(R.id.tv_team);
            recyclerView=itemView.findViewById(R.id.item_recyclerView);

//            tvTeamChild = itemView.findViewById(R.id.tv_team_child);
            category_add = itemView.findViewById(R.id.category_add);  //添加子类按钮
        }


    }

}

