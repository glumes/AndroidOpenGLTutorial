package com.glumes.openglbasicshape.transition_banner;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.glumes.openglbasicshape.base.LogUtil;

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

    private float mLastX;
    private float mLastY;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        float x = event.getX();
//        float y = event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float distance = y - mLastX;
//                mLastY = y;
//
//                mRender.setProgress(distance);
//                LogUtil.d("distance is " + distance);
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    public void setRender(TransitionBannerRender render) {
        this.mRender = render;
        setRenderer(render);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
