package com.glumes.comlib;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by glumes on 2017/7/20.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initTimeber();
    }

    void initTimeber() {
        Timber.plant(new Timber.DebugTree());
    }
}
