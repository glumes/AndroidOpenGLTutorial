package com.glumes.openglbasicshape.renderers;

import android.content.Context;

import com.glumes.openglbasicshape.objects.Point;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glViewport;

/**
 * Created by glumes on 2017/7/22.
 */

public class PointRenderer extends BaseRenderer {

    private Point mPoint;

    public PointRenderer(Context mContext) {
        super(mContext);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);


        // 在 onSurfaceCreated 里面初始化，否则会报线程错误
        mPoint = new Point(mContext);
        mPoint.bindData();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 确定视口大小
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清屏
        glClear(GL_COLOR_BUFFER_BIT);
        // 绘制
        mPoint.draw();
    }

}
