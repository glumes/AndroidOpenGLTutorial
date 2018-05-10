package com.glumes.openglbasicshape.draw;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

import com.glumes.openglbasicshape.renderers.BaseRenderer;

/**
 * Created by glumes on 2017/8/16.
 */

public class BaseShapeView extends GLSurfaceView {

    private BaseRenderer baseRenderer;

    public BaseShapeView(Context context, BaseRenderer renderer) {
        super(context);
        setEGLContextClientVersion(2);

        baseRenderer = renderer;
        setRenderer(baseRenderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        baseRenderer.onSurfaceDestroyed();
    }
}
