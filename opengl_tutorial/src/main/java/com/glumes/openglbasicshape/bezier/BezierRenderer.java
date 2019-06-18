package com.glumes.openglbasicshape.bezier;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BezierRenderer implements GLSurfaceView.Renderer {

    private final Context mContext;
    private BezierCurve mBezierCurve;
    private Random mRandom = new Random();

    public BezierRenderer(Context context) {
        mContext = context;
    }

    private float num = 0f;
    private int delta = 200;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mBezierCurve = new BezierCurve(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        num++;

        mBezierCurve.setAmp((float) (3.0 * (num % delta) / delta));


        mBezierCurve.draw();

    }
}
