package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.objects.Triangle;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/7/22.
 */

public class TriangleRenderer extends BaseRenderer {

    private Triangle mTriangle;

    private static final int NUM = 360;

    private RotateThread mRotateThread;        //该线程用来改变图形角度

    // 分成 360 份，每一份的弧度
    public static final float radian = (float) (2 * Math.PI / NUM);

    float radius = 3;

    float angle = 0f;
    int angleX = 0;
    float positionX;
    float positionZ;

    float ratio;

    public TriangleRenderer(Context mContext) {
        super(mContext);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        mTriangle = new Triangle(mContext);
        mTriangle.bindData();
        mRotateThread = new RotateThread();

        LogUtil.d("radian is " + radian);

//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mRotateThread.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        // 设置成单位矩阵


        // 对模型矩阵进行一些变换
        // 移动
//        translateM(modelMatrix, 0, 0.3f, 0f, 0f);
////        // 旋转
//        rotateM(modelMatrix, 0, 45f, 0f, 1f, 0f);
////        // 缩放
//        scaleM(modelMatrix, 0, 0.5f, 1.5f, 0f);

//        float ratio = (float) width / height;
        ratio = (float) width / height;

//        创建正交投影的方法
//        Matrix.orthoM();

        // 两种创建透视投影的方法

//        Matrix.perspectiveM();

//        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);


    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL_COLOR_BUFFER_BIT);


        setIdentityM(modelMatrix, 0);

        Matrix.rotateM(modelMatrix, 0, angleX, 1, 0, 0);

        positionX = (float) (radius * Math.sin(angle));
        positionZ = (float) (radius * Math.cos(angle));

        LogUtil.d("position X is " + positionX);
        LogUtil.d("position Z is " + positionZ);

        Matrix.setLookAtM(viewMatrix, 0, positionX, 0, positionZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 5);

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, viewMatrix, 0, modelMatrix, 0);

        final float[] result = new float[16];
        Matrix.multiplyMM(result, 0, projectionMatrix, 0, temp, 0);

        System.arraycopy(result, 0, projectionMatrix, 0, result.length);

        mTriangle.draw(result);

//        mTriangle.draw();
    }

    /**
     * 这个线程是用来改变三角形角度用的
     */
    public class RotateThread extends Thread {

        public boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                angle += radian;
//                angleX++;
//                angleX = (angleX + 1) % 360;
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
