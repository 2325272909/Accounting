package com.example.accounting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.accounting.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_record extends Fragment {

    private ViewPager viewPager;  //显示区
    private RadioGroup radioGroup;  //底部导航栏
    private static final String ARG_user = "userName";

    private List<Fragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter adapter;

    public static Fragment_record newInstance(String userName) {
        Fragment_record fragment = new Fragment_record();
        Bundle args = new Bundle();
        args.putString(ARG_user,userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intitView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_record, container, false);
    }


    public void intitView( ) {
        // Inflate the layout for this fragment
        viewPager = getActivity().findViewById(R.id.fragment);  //显示区
        radioGroup = getActivity().findViewById(R.id.tabs_rg); //底部导航栏
        // init fragment
        fragments = new ArrayList<>();   //显示内容
        fragments.add(new Fragment_consume());
        fragments.add(new Fragment_income());

        // init view pager
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        // register listener
        viewPager.addOnPageChangeListener(mPageChangeListener);
        radioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    /**
     * 页面转换监听
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(position);  //选中事件
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
                    viewPager.setCurrentItem(i);
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
