package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by glumes on 2017/7/23.
 */

public class RectangleRenderer extends BaseRenderer {

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private int aColorLocation;
    private int aPositionLocation;

    private int uMatrixLocation;

    private float[] modelMatrix = new float[16];

    private float[] viewMatrix = new float[16];

    private float[] projectionMatrix = new float[16];

    float[] rectangleVertex = {

            0.5f, 0.5f,
            -0.5f, 0.5f,
            0.5f, -0.5f,

            -0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f

    };

//
//    float[] rectangleVertex = {
//            0f, 0f, 0f, 1.5f,
//            -0.5f, -0.8f, 0f, 1f,
//            0.5f, -0.8f, 0f, 1f,
//
//            0.5f, 0.8f, 0f, 2f,
//            -0.5f, 0.8f, 0f, 2f,
//            -0.5f, -0.8f, 0f, 1f
//    };


    public static final int POSITION_COMPONENT_COUNT = 2;
    private FloatBuffer vertexData;


    public RectangleRenderer(Context mContext) {
        super(mContext);
        vertexData = ByteBuffer.allocateDirect(rectangleVertex.length * BYTES_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(rectangleVertex);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        aColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        // 设置成单位矩阵
        setIdentityM(modelMatrix, 0);

        // 对模型矩阵进行一些变换
        // 移动
        translateM(modelMatrix, 0, 0.3f, 0f, 0f);
//        // 旋转
        rotateM(modelMatrix, 0, 45f, 0f, 1f, 0f);
//        // 缩放
        scaleM(modelMatrix, 0, 0.5f, 1.5f, 0f);

        float ratio = (float) width / height;

//        创建正交投影的方法
//        Matrix.orthoM();

        // 两种创建透视投影的方法
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

//        Matrix.perspectiveM();

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, viewMatrix, 0, modelMatrix, 0);

        final float[] result = new float[16];
        Matrix.multiplyMM(result, 0, projectionMatrix, 0, temp, 0);

        System.arraycopy(result, 0, projectionMatrix, 0, result.length);
    }


    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL_COLOR_BUFFER_BIT);


        float radius = 10.0f;

//        float camX = Math.sin(ge)

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

    }

    @Override
    public void readShaderSource() {
        vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.rectangle_vertex_shaper);
        fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.rectangle_fragment_shaper);
    }
}
