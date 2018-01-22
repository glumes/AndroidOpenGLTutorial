package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by glumes on 2017/7/22.
 */

public abstract class BaseRenderer implements GLSurfaceView.Renderer {


    protected Context mContext;


    protected float[] modelMatrix = new float[16];

    protected float[] viewMatrix = new float[16];

    protected float[] projectionMatrix = new float[16];

    protected float[] mvpMatrix = new float[16];

    public BaseRenderer(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }


    /**
     * Surface 刚创建的时候，它的 size 是 0，也就是说在画第一次图之前它会被调用一次
     *
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }


    public void onSurfaceDestroyed(){

    }

}
