package com.goat.svgdemo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Goat on 2017/6/9.
 */

public class IconfontActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextIcon;
    private TextView mTextIcon1;
    private boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iconfont);
        mTextIcon = (TextView) findViewById(R.id.text_icon);
        mTextIcon1 = (TextView) findViewById(R.id.text_icon1);
        //为TextView设置指定ttf文字
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        mTextIcon.setTypeface(iconfont);

        mTextIcon1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mTextIcon1.setText(flag ? "\ue643" : "\ue655");
        flag = !flag;
    }
}
