package com.glumes.openglbasicshape.bezier.curve;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.bezier.curve.BezierRenderer;

public class BezierActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;
    private BezierRenderer mBezierRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        mBezierRenderer = new BezierRenderer(this);
        mGLSurfaceView = findViewById(R.id.glsurfaceview);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(mBezierRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
