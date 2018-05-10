package com.glumes.openglbasicshape.draw;

import android.content.Context;

import com.glumes.openglbasicshape.utils.VertexArray;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by glumes on 2017/7/29.
 */

public abstract class BaseShape {


    protected Context mContext;

    protected VertexArray vertexArray;

    protected VertexArray indexArray;

    protected int mProgram;

    protected float[] modelMatrix = new float[16];
    protected float[] viewMatrix = new float[16];
    protected float[] projectionMatrix = new float[16];
    protected float[] mvpMatrix = new float[16];


    protected int POSITION_COMPONENT_COUNT;

    protected int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

    protected int STRIDE;

    public BaseShape(Context context) {
        mContext = context;

    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    public void onDrawFrame(GL10 gl) {

    }


    public void onDrawFrame(GL10 gl, float[] mvpMatrix) {

    }


    public void onSurfaceDestroyed() {

    }

}
