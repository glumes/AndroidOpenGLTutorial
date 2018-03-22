package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.draw.graph.Rectangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/7/23.
 */

public class RectangleRenderer extends BaseRenderer {

    private Rectangle mRectangle;

    public RectangleRenderer(Context mContext) {
        super(mContext);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);


        mRectangle = new Rectangle(mContext);

        mRectangle.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        // 设置成单位矩阵
        setIdentityM(modelMatrix, 0);

        // 对模型矩阵进行一些变换
        // 移动
//        translateM(modelMatrix, 0, 0.3f, 0f, 0f);
////        // 旋转
//        rotateM(modelMatrix, 0, 45f, 0f, 1f, 0f);
////        // 缩放
//        scaleM(modelMatrix, 0, 0.5f, 1.5f, 0f);

        float ratio = (float) width / height;

//        创建正交投影的方法
//        Matrix.orthoM();

        // 两种创建透视投影的方法
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

//        Matrix.perspectiveM();

//        Matrix.orthoM();
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, viewMatrix, 0, modelMatrix, 0);

        final float[] result = new float[16];
        Matrix.multiplyMM(result, 0, projectionMatrix, 0, temp, 0);

        System.arraycopy(result, 0, projectionMatrix, 0, result.length);


    }


    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

//        mRectangle.draw(modelMatrix);

        mRectangle.onDrawFrame(gl);
    }

}
