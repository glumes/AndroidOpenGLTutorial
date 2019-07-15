package com.glumes.openglbasicshape.bezier.curve;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.bezier.curve.BezierCurve;
import com.glumes.openglbasicshape.bezier.drawer.NormalSizeHelper;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;

public class BezierRenderer implements GLSurfaceView.Renderer {

    private final Context mContext;
    private BezierCurve mBezierCurve;
    private Random mRandom = new Random();


    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mTemporaryMatrix = new float[16];


    public BezierRenderer(Context context) {
        mContext = context;
    }

    private float num = 0f;
    private int delta = 200;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mBezierCurve = new BezierCurve(mContext);
        Matrix.setIdentityM(mModelMatrix, 0);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
        NormalSizeHelper.setAspectRatio(aspectRatio);
        NormalSizeHelper.setSurfaceViewInfo(width, height);
        if (width > height) {
            NormalSizeHelper.setIsVertical(false);
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 3, 7);
        } else {
            NormalSizeHelper.setIsVertical(true);
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 3, 7);
        }
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        num++;

        mBezierCurve.setAmp((float) (3.0 * (num % delta) / delta));


//        mBezierCurve.draw();

        float[] resultMatrix = new float[16];
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Matrix.multiplyMM(resultMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);

        mBezierCurve.draw(resultMatrix);
    }
}
