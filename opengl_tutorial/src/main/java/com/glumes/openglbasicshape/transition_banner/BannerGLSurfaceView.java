package com.glumes.openglbasicshape.transition_banner;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Author glumes
 */
public class BannerGLSurfaceView extends GLSurfaceView {

    private TransitionBannerRender mRender;

    public BannerGLSurfaceView(Context context) {
        super(context);
    }

    public BannerGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setRender(TransitionBannerRender render) {
        this.mRender = render;
        setRenderer(render);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
