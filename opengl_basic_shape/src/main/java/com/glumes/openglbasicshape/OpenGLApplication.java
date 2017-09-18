package com.glumes.openglbasicshape;

import com.glumes.comlib.BaseApplication;
import com.glumes.openglbasicshape.utils.DisplayManager;

/**
 * Created by zhaoying on 2017/9/18.
 */

public class OpenGLApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayManager.getInstance().init(this);
    }
}
