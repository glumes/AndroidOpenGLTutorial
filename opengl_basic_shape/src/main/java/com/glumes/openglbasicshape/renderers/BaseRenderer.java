package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * Created by glumes on 2017/7/22.
 */

public abstract class BaseRenderer implements GLSurfaceView.Renderer {


    protected Context mContext;

    private float angleX;
    private float angleY;
    private float angleZ;

    protected float[] modelMatrix = new float[16];

    protected float[] viewMatrix = new float[16];

    protected float[] projectionMatrix = new float[16];

    protected float[] mvpMatrix = new float[16];

    public BaseRenderer(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
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
        // 确定视口大小
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清屏
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }


    public void onSurfaceDestroyed(){

    }

}
