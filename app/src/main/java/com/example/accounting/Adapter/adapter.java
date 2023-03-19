package com.example.accounting.Adapter;


import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过对list列表数据封装，实现recyclerview中的数据显示
 * 主要作用于分类展示
 */
public class adapter extends RecyclerView.Adapter<adapter.myviewholder> {

    private Context context;
    private List<String> mlist = new ArrayList<>();
    private int expandPosition = -1;
    private myviewholder mViewholder;

    public adapter(Context context) {
        this.context  = context;
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
    public void onBindViewHolder(final adapter.myviewholder viewHolder, int position) {
        viewHolder.tvTeam.setText(mlist.get(position));
//        viewHolder.tvTeamChild.setText(mlist.get(position)+"的子内容");

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

        viewHolder.category_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i(MotionEffect.TAG,"点击按钮");
            }
        });


        /**
         * 给子类的RecyclerView设置菜品数据适配器
         *应该进行判断，读取不同的数据库
         * 例：List<DishViewDto> dishViewDtoList = mDatas.get(position).getDishViewDtoList();
         *         DishAdapter dishAdapter = new DishAdapter(dishViewDtoList);
         * mDatas是类的集合
         */
        List<String> itemNames = new ArrayList<>();
        itemNames.add("服装");
        ListItemAdapter listItemAdapter = new ListItemAdapter(itemNames);
        viewHolder.recyclerView.setAdapter(listItemAdapter);
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.recyclerView.getContext()));
    }


    /**
     * 重写方法3 getItemCount()
     * @return
     */
    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }


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

