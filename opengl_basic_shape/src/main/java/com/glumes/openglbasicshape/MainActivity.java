package com.glumes.openglbasicshape;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.glviews.BaseGLSurfaceView;
import com.glumes.openglbasicshape.renderers.BaseRenderer;
import com.glumes.openglbasicshape.renderers.CircleRenderer;
import com.glumes.openglbasicshape.renderers.CubeRender;
import com.glumes.openglbasicshape.renderers.CubeRender2;
import com.glumes.openglbasicshape.renderers.LineRenderer;
import com.glumes.openglbasicshape.renderers.PointRenderer;
import com.glumes.openglbasicshape.renderers.RectangleRenderer;
import com.glumes.openglbasicshape.renderers.SphereRenderer;
import com.glumes.openglbasicshape.renderers.TriangleRenderer;
import com.glumes.openglbasicshape.utils.Constant;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private BaseGLSurfaceView glSurfaceView;

    private int mType;

    private SparseArray<BaseRenderer> mRendererArray = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRendererMap();

//        glSurfaceView = new GLSurfaceView(this);
//
//        glSurfaceView.setEGLContextClientVersion(2);


        mType = getIntent().getIntExtra(Constant.RENDERER_TYPE, 0);

        final BaseRenderer renderer = mRendererArray.get(mType);

        glSurfaceView = new BaseGLSurfaceView(this, renderer);

//        glSurfaceView.setRenderer(renderer);

        // 两种绘图模式，第一种连续不断的画，适用于动画；第二种有需要时再画，通过 requestRender 调用
//        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

//        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x = 0.0f;
                float y = 0.0f;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX() - x;
                        LogUtil.e("dx = " + dx);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        setContentView(glSurfaceView);
    }


    public void initRendererMap() {
        mRendererArray.put(0, new PointRenderer(this));
        mRendererArray.put(1, new LineRenderer(this));
        mRendererArray.put(2, new TriangleRenderer(this));
        mRendererArray.put(3, new RectangleRenderer(this));
        mRendererArray.put(4, new CircleRenderer(this));
        mRendererArray.put(5, new CubeRender(this));
        mRendererArray.put(6, new CubeRender2(this));
        mRendererArray.put(7, new SphereRenderer(this));
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
