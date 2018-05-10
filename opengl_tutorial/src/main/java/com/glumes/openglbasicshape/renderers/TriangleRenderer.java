package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.draw.graph.Triangle;
import com.glumes.openglbasicshape.utils.DisplayManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;
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

    public int[] view_matrix = new int[4];


    public TriangleRenderer(Context mContext) {
        super(mContext);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mTriangle = new Triangle(mContext);
        mTriangle.onSurfaceCreated(gl,config );
        mRotateThread = new RotateThread();

        LogUtil.d("radian is " + radian);

        mRotateThread.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        // 设置成单位矩阵

        LogUtil.d("onSurfaceChanged width is " + width + " height is " + height);

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


        view_matrix[0] = 0;
        view_matrix[1] = 0;
        view_matrix[2] = width;
        view_matrix[3] = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        setIdentityM(modelMatrix, 0);
        setIdentityM(viewMatrix, 0);
        setIdentityM(projectionMatrix, 0);

        Matrix.rotateM(modelMatrix, 0, angleX, 0, 1, 0);

        positionX = (float) (radius * Math.sin(angle));
        positionZ = (float) (radius * Math.cos(angle));

        // 摄像机位置旋转
        Matrix.setLookAtM(viewMatrix, 0, positionX, 0, positionZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 5);

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, viewMatrix, 0, modelMatrix, 0);

        final float[] result = new float[16];
        Matrix.multiplyMM(result, 0, projectionMatrix, 0, temp, 0);

        System.arraycopy(result, 0, mvpMatrix, 0, result.length);

        mTriangle.onDrawFrame(gl, mvpMatrix);

//        mTriangle.draw();
    }


    public int isInCubeArea(float[] Win) {


        float[] winXY = new float[3];

        float[] xyz = new float[]{
                -1f, 1f, 0
        };

        LogUtil.d("screen width is " + DisplayManager.getInstance().getmScreenWidth());
        LogUtil.d("screen height is " + DisplayManager.getInstance().getmScreenHeight());

        Transform(modelMatrix, xyz);


        int result = GLU.gluProject(xyz[0], xyz[1], xyz[2], viewMatrix, 0, projectionMatrix, 0, view_matrix, 0, winXY, 0);

        LogUtil.d("gluProject result is " + result);
        LogUtil.d("winX is " + winXY[0]);
        LogUtil.d("winY is " + winXY[1]);
        LogUtil.d("winZ is " + winXY[2]);

        LogUtil.d("view port y is " + view_matrix[3]);
        LogUtil.d("view port x is " + view_matrix[2]);


        LogUtil.d("win y is " + (view_matrix[3] - Win[1]));

        return 0;
    }


    /**
     * matrix 两个投影矩阵相乘后的结果
     *
     * @param matrix
     * @param Point
     */
    private void Transform(float[] matrix, float[] Point) {
        float w = 1.f;

        float x, y, z, ww;

        x = matrix[0] * Point[0] + matrix[4] * Point[1] + matrix[8] * Point[2] + matrix[12] * w;
        y = matrix[1] * Point[0] + matrix[5] * Point[1] + matrix[9] * Point[2] + matrix[13] * w;
        z = matrix[2] * Point[0] + matrix[6] * Point[1] + matrix[10] * Point[2] + matrix[14] * w;
        ww = matrix[3] * Point[0] + matrix[7] * Point[1] + matrix[11] * Point[2] + matrix[15] * w;

        Point[0] = x / ww;
        Point[1] = y / ww;
        Point[2] = z / ww;

    }

    /**
     * 这个线程是用来改变三角形角度用的
     */
    public class RotateThread extends Thread {

        public boolean flag = false;

        @Override
        public void run() {
            while (flag) {
//                angle += radian;
                angleX = (angleX + 1) % 360;
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
