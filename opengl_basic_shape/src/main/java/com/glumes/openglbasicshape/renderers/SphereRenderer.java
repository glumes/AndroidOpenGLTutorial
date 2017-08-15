package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.objects.Sphere;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;

/**
 * Created by glumes on 2017/8/9.
 */

public class SphereRenderer extends BaseRenderer {


    Sphere sphere;

    public SphereRenderer(Context mContext) {
        super(mContext);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        sphere = new Sphere(mContext);

        sphere.bindData();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(viewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        sphere.draw(mvpMatrix);
//        sphere.draw();
    }
}
