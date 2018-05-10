package com.glumes.openglbasicshape;

import android.annotation.SuppressLint;
import android.content.Context;

import com.glumes.comlib.BaseApplication;
import com.glumes.openglbasicshape.utils.DisplayManager;

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
        DisplayManager.getInstance().init(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
