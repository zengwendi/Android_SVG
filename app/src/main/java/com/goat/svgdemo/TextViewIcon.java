package com.goat.svgdemo;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * Created by Goat on 2017/6/9.
 * iconfont的封装的TextView
 */

public class TextViewIcon extends AppCompatTextView {
    public TextViewIcon(Context context) {
        super(context);
        init(context);
    }

    public TextViewIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        setTypeface(MyApplication.getIconfont(context));
    }
}
