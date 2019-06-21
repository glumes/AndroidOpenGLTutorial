package com.glumes.openglbasicshape.bezier;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BezierDrawView extends GLSurfaceView {

    public BezierDrawView(Context context) {
        super(context);
    }

    public BezierDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float currentX;
    private float currentY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        currentX = event.getX();
        currentY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}
