package com.glumes.opengl_tutorial_practice.utils;

import android.util.Log;


public class LogUtil {
    private static String DEFAULT_TAG = "OpenGLTutorial";
    private static String DEBUG = "mydebug";
    private static boolean mLogAll = true;
    private static final boolean LOGD_DEBUG = true;
    private static final boolean LOGI_DEBUG = true;
    private static final boolean LOGE_DEBUG = true;
    private static final boolean LOGDB_DEBUG = true;
    private static final int STACK_INDEX = 2;

    /**
     * Used for persistent log
     *
     * @param msg
     */
    public static void d(String msg) {
        if (LOGD_DEBUG && mLogAll) {
            Log.d(DEFAULT_TAG, getInformation(msg));
        }
    }

    /**
     * Used for temp log
     *
     * @param msg
     */
    public static void i(String msg) {
//        printToFile(msg);
        if (LOGI_DEBUG && mLogAll) {
            Log.i(DEFAULT_TAG, getInformation(msg));
        }
    }

    /**
     * Used for exception log
     *
     * @param msg
     */
    public static void e(String msg) {
//        printToFile(msg);
        if (LOGE_DEBUG && mLogAll) {
            Log.e(DEFAULT_TAG, getInformation(msg));
        }
    }

    /**
     * Used for sdk developers to debug
     *
     * @param msg
     * @return
     */
    public static void db(String msg) {
//        printToFile(msg);
        if (LOGDB_DEBUG && mLogAll) {
            Log.d(DEBUG, getInformation(msg));
        }
    }

    /**
     * Used for exception log
     *
     * @param msg
     * @param t
     */
    public static void e(String msg, Throwable t) {
        if (LOGE_DEBUG && mLogAll) {
            Log.e(DEFAULT_TAG, getInformation(msg), t);
        }
    }

    private static String getInformation(String msg) {
        Exception exception = new Exception();
        return exception.getStackTrace()[STACK_INDEX].getFileName() + "|"
                + exception.getStackTrace()[STACK_INDEX].getMethodName() + "|"
                + exception.getStackTrace()[STACK_INDEX].getLineNumber() + "|" + msg;
    }

    public static void setLog(boolean enable) {
        mLogAll = enable;
    }

    public static boolean isLog() {
        return mLogAll;
    }
}
