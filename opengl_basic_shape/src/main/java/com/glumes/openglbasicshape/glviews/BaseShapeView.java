package com.glumes.openglbasicshape.glviews;

import android.content.Context;
import android.opengl.GLSurfaceView;

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

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

}
