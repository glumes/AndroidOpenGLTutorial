package com.glumes.openglbasicshape.transition_banner;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Author glumes
 */
public class BannerGLSurfaceView extends GLSurfaceView {
    public BannerGLSurfaceView(Context context) {
        super(context);
    }

    public BannerGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }
}
