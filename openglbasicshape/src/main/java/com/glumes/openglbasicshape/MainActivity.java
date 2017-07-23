package com.glumes.openglbasicshape;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;

import com.glumes.openglbasicshape.renderers.BaseRenderer;
import com.glumes.openglbasicshape.renderers.LineRenderer;
import com.glumes.openglbasicshape.renderers.PointRenderer;
import com.glumes.openglbasicshape.renderers.TriangleRenderer;
import com.glumes.openglbasicshape.utils.Constant;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private GLSurfaceView glSurfaceView;

    private int mType;

    private SparseArray<BaseRenderer> mRendererArray = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRendererMap();

        glSurfaceView = new GLSurfaceView(this);

        glSurfaceView.setEGLContextClientVersion(2);

        mType = getIntent().getIntExtra(Constant.RENDERER_TYPE, 0);

        glSurfaceView.setRenderer(mRendererArray.get(mType));

        setContentView(glSurfaceView);
    }


    public void initRendererMap() {
        mRendererArray.put(0, new PointRenderer(this));
        mRendererArray.put(1, new LineRenderer(this));
        mRendererArray.put(2, new TriangleRenderer(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
