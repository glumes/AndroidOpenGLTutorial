package com.glumes.openglbasicshape.base;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import timber.log.Timber;

/**
 * Created by glumes on 2017/7/20.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initTimeber();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    void initTimeber() {
        Timber.plant(new Timber.DebugTree());
    }
}
