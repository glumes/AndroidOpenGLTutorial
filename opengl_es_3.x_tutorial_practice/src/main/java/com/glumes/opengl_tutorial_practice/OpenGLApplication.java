package com.glumes.opengl_tutorial_practice;

import android.annotation.SuppressLint;
import android.content.Context;

import com.glumes.opengl_tutorial_practice.base.BaseApplication;


/**
 * Created by zhaoying on 2017/9/18.
 */

public class OpenGLApplication extends BaseApplication {


    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
