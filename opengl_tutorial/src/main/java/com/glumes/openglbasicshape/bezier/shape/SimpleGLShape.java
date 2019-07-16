package com.glumes.openglbasicshape.bezier.shape;

import android.graphics.Color;
import android.opengl.GLES20;


import com.glumes.openglbasicshape.utils.GLHelper;

import java.nio.FloatBuffer;



public abstract class SimpleGLShape extends BaseGLShape {
    protected int mMvpHandle;
    protected int mPositionHandle;
    protected int mColorHandle;

    protected FloatBuffer mVertexBuffer;

    protected static final int DEFAULT_SIZE = 10;
    //如果mData数组接近Integer - 8,就需要创建第二个GLShape了。
    protected float[] mData;

    public SimpleGLShape() {
        mData = new float[DEFAULT_SIZE];
        createBuffer();
    }
    
    public SimpleGLShape(int defaultSize){
        mData = new float[defaultSize];
        createBuffer();
    }
    
    @Override
    protected String getVertexShaderCode() {
        return "uniform mat4 uMVPMatrix;" +
                "attribute vec4 aPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * aPosition;" +
                "}";
    }

    @Override
    protected String getFragmentShaderCode() {
        return "precision mediump float;" +
                "uniform vec4 uColor;" +
                "void main() {" +
                "  gl_FragColor = uColor;" +
                "}";
    }

    @Override
    protected void findHandlersInGLSL() {
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandler, "aPosition");
        mColorHandle = GLES20.glGetUniformLocation(mProgramHandler, "uColor");
        mMvpHandle = GLES20.glGetUniformLocation(mProgramHandler, "uMVPMatrix");
    }

    @Override
    protected void createBuffer() {
        mVertexBuffer = GLHelper.initFloatBuffer(mData);
    }

    @Override
    protected void fillHandlersValue() {
        GLES20.glUniform4fv(mColorHandle, 1, getColor(), 0);
    }

    @Override
    protected void enableAttrArrays() {
        GLES20.glEnableVertexAttribArray(mPositionHandle);
    }

    @Override
    protected void disableAttrArrays() {
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public float[] getColor() {
        return GLHelper.toOpenGlColor(Color.RED);
    }
}
