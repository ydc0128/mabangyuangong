package com.example.administrator.ydxcfwpt.Utils;

import android.app.Application;

/**
 * Created by Administrator on 2018/3/7.
 */

public class ContextUtil extends Application{
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
}
