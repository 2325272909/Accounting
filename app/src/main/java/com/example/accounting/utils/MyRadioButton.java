package com.example.accounting.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.accounting.R;

/**
 * 定义radioButton类，改变radioButton布局
 */

public class MyRadioButton extends AppCompatRadioButton {
    private Drawable drawable;
    private int mTopWith, mTopHeight;


    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);//获取我们定义的属性
        drawable = typedArray.getDrawable(R.styleable.MyRadioButton_drawableTop);
        drawable.setBounds(0, 0, 80, 80);//图片大小
        this.setCompoundDrawables(null, drawable, null, null);//设置位置

    }



    // 设置Drawable定义的大小
    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (top != null) {
            top.setBounds(0, 0, mTopWith <= 0 ? top.getIntrinsicWidth() : mTopWith, mTopHeight <= 0 ? top.getMinimumHeight() : mTopHeight);
        }

        setCompoundDrawables(left, top, right, bottom);
    }

}

