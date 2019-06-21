package com.glumes.openglbasicshape.bezier;

import android.content.Context;
import android.opengl.GLES20;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;

public class BezierCurve {

    private final Context mContext;

    private float[] mStartEndPoints;
    private float[] mControlPoints;
    private float[] mDataPoints;

    private int mProgram;
    private int mStartEndHandle;
    private int mControlHandle;
    private int mDataHandle;
    private int mAmpsHandle;
    private int mMvpHandle;

    private float mAmps = 1.0f;

    private FloatBuffer mBuffer;

    private final int mBufferId;

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mTemporaryMatrix = new float[16];


    public BezierCurve(Context context) {
        mContext = context;

        mProgram = ShaderHelper.buildProgram(mContext, R.raw.bezier_vertex, R.raw.bezier_fragment);

        glUseProgram(mProgram);

        mStartEndHandle = glGetUniformLocation(mProgram, "uStartEndData");
        mControlHandle = glGetUniformLocation(mProgram, "uControlData");

        mAmpsHandle = glGetUniformLocation(mProgram, "u_Amp");

        mDataHandle = glGetAttribLocation(mProgram, "aData");
//
//        mMvpHandle = glGetUniformLocation(mProgram,"u_MVPMatrix");
//
//        mStartEndPoints = new float[]{
//                -1, 0,
//                0, 0.244f,
//        };
//
//        mControlPoints = new float[]{
//                -0.8f, 0.1f,
//                -0.24f, 0.244f
//        };

        mStartEndPoints = new float[]{
                -1, 0,
                1,0,
        };

        mControlPoints = new float[]{
                0,0.5f,
                1,0,
        };

        mDataPoints = genTData();

        mBuffer = Buffers.makeInterleavedBuffer(mDataPoints, Const.NUM_POINTS);

        final int buffers[] = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mBuffer.capacity() * Const.BYTES_PER_FLOAT,
                mBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        mBufferId = buffers[0];

        mBuffer = null;
    }


    public void draw() {
        GLES20.glClearColor(0.0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        glUniform4f(mStartEndHandle,
                mStartEndPoints[0],
                mStartEndPoints[1],
                mStartEndPoints[2],
                mStartEndPoints[3]);

        glUniform4f(mControlHandle,
                mControlPoints[0],
                mControlPoints[1],
                mControlPoints[2],
                mControlPoints[3]);

        glUniform1f(mAmpsHandle, mAmps);

        final int stride = Const.BYTES_PER_FLOAT * Const.T_DATA_SIZE;

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferId);
        GLES20.glEnableVertexAttribArray(mDataHandle);
        GLES20.glVertexAttribPointer(mDataHandle,
                Const.T_DATA_SIZE,
                GLES20.GL_FLOAT,
                false,
                stride,
                0);

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, Const.NUM_POINTS * Const.POINTS_PER_TRIANGLE);

    }


    @SuppressWarnings("UnnecessaryLocalVariable")
    private float[] genTData() {
        //  1---2
        //  | /
        //  3
        float[] tData = new float[Const.POINTS_PER_TRIANGLE * Const.T_DATA_SIZE * Const.NUM_POINTS];

        float step = 1f / (float) tData.length * 2f;

        for (int i = 0; i < tData.length; i += Const.POINTS_PER_TRIANGLE) {
            float t = (float) i / (float) tData.length;
            float t1 = (float) (i + 1) / (float) tData.length;
            float t2 = (float) (i + 2) / (float)tData.length;

            tData[i] = t;
            tData[i + 1] = t1;
            tData[i + 2] = t2;

        }

        return tData;
    }

    public void setAmp(float amp){
        mAmps = amp;
    }
}
