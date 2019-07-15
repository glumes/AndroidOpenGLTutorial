package com.glumes.openglbasicshape.bezier.drawer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BezierDrawView extends GLSurfaceView {

    private BezierDrawRenderer mBezierDrawRenderer;


    public BezierDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mBezierDrawRenderer = new BezierDrawRenderer(context);
        setRenderer(mBezierDrawRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private float currentX;
    private float currentY;

}
