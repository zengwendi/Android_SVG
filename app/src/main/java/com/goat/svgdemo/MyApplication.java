package com.goat.svgdemo;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by zengwendi on 2017/6/9.
 */

public class MyApplication extends Application {

    public static Typeface iconfont;
    
    public static Typeface getIconfont(Context context) {
        if (iconfont != null) {
            return iconfont;
        } else {
            iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        }
        return iconfont;
    }
}
