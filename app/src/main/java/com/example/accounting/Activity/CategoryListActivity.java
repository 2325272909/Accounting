package com.example.accounting.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.accounting.Fragments.Fragment_consume;
import com.example.accounting.Fragments.Fragment_income;
import com.example.accounting.R;
import com.example.accounting.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * "我的"模块，分类功能展示界面
 */
public class CategoryListActivity extends AppCompatActivity {

    private ViewPager mViewPager;  //显示区
    private RadioGroup mTabRadioGroup;  //底部导航栏
    private Button exit;
    private User user;
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);
        initView();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CategoryListActivity.this,MainActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("fragment_id",3);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        // find view
        this.user =(User) getIntent().getSerializableExtra("user");
        mViewPager = findViewById(R.id.fragment);  //显示区
        mTabRadioGroup = findViewById(R.id.contains); //底部导航栏
        exit = findViewById(R.id.exit);
        // init fragment
        mFragments = new ArrayList<>(2);   //显示内容
        mFragments.add(new Fragment_consume());
        mFragments.add(new Fragment_income());

        // init view pager
        mAdapter = new CategoryListActivity.MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    @Override
    protected void onResume(){
        int fragment_id = getIntent().getIntExtra("fragment_id",0);

        mViewPager.setCurrentItem(fragment_id);//

        super.onResume();
    }
    /**
     * 页面转换监听
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);  //选中事件
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    };

    /**
     * mViewPager(显示栏)，设置选中fragment的id
     */
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        /**
         * 初始化函数-fragmentPagerAdapter
         * @param fm  -fragmentManager
         * @param list List<Fragment>
         */
        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        /**
         * 获取当前的Viewpager里的fragment
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            //return this.mList == null ? null : this.mList.get(position);
            Fragment fragment = null;
            fragment = this.mList.get(position);  //获取相同位置的fragment
            Bundle bundle = new Bundle();
            bundle.putString("id",""+position);
            fragment.setArguments(bundle);
            return fragment;

        }

        /**
         * fragment数组的大小
         * @return
         */
        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }

}
