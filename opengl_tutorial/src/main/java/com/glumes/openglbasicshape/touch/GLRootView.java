package com.glumes.openglbasicshape.touch;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.glumes.openglbasicshape.draw.texture.RectangleTexture;
import com.glumes.openglbasicshape.renderers.BasicShapeRender;

/**
 * Created by glumes on 22/05/2018
 */
public class GLRootView extends GLSurfaceView {

    private BasicShapeRender renderer;

    public GLRootView(Context context) {
        super(context);
        init();
    }


    public GLRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setEGLContextClientVersion(2);

        renderer = new BasicShapeRender(getContext());

        renderer.setShape(RectangleTexture.class);

        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
