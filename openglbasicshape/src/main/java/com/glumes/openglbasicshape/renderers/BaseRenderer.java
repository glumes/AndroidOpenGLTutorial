package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by glumes on 2017/7/22.
 */

public abstract class BaseRenderer implements GLSurfaceView.Renderer {

    protected int BYTES_PRE_FLOAT = 4;


    protected Context mContext;

    public BaseRenderer(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
