package com.gluems.magiccube

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * @Author glumes
 */
class CubeApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        mContext = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext:Context

        fun getContext(): Context {
            return mContext
        }
    }
}