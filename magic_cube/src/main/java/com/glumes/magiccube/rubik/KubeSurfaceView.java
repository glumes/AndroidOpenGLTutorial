package com.glumes.magiccube.rubik;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import lib.AppConfig;

public class KubeSurfaceView extends GLSurfaceView {
    private float mPreviousX;
    private float mPreviousY;
    private float mDownX;
    private float mDownY;

    private KubeRenderer mRenderer;

    public KubeSurfaceView(Context context, KubeRenderer renderer) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mRenderer = renderer;
        this.setRenderer(this.mRenderer);

        //设置渲染模式为主动渲染
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        AppConfig.setTouchPosition(x, y);

        if (!AppConfig.Turning) {
            AppConfig.gbNeedPick = true;
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRenderer.clearPickedCubes();
                mDownX = x;
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 手势距离
                //float d = (float) (Math.sqrt(dx * dx + dy * dy));
                //绕X轴旋转
                float dx = y - mPreviousY;
                //绕y轴旋转
                float dy = x - mPreviousX;

                mRenderer.offsetX = dx;
                mRenderer.offsetY = dy;
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                boolean direction = false;

                // x 方向上的移动距离 大于 y 方向上的移动距离
                if (Math.abs(x - mDownX) > Math.abs(y - mDownY)) {
                    if (x - mDownX > 0) { // 向右滑动了
                        direction = false;
                    } else {
                        direction = true; // 向左滑动了
                    }
                } else {
                    if (y - mDownY > 0) {
                        direction = true; // 向下滑动了
                    } else {
                        direction = false; // 向上滑动了
                    }
                }

                mRenderer.offsetX = 0;
                mRenderer.offsetY = 0;

                final boolean direct = direction;

                //先选中的方块，判断魔方转向
                //根据当前屏幕接触点做射线来判断接触面
                mRenderer.decideTurning(direct);

                mDownX = 0;
                mDownY = 0;
                AppConfig.setTouchPosition(0, 0);
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
