package com.glumes.openglbasicshape.draw.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.draw.BaseShape;
import com.glumes.openglbasicshape.utils.Constants;
import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.VertexArray;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/8/8.
 */

public class Cube extends BaseShape {


    private static final String U_COLOR = "u_Color";


    private static final String A_POSITION = "a_Position";

    private static final String U_MODEL_MATRIX = "u_ModelMatrix";
    private static final String U_VIEW_MATRIX = "u_ViewMatrix";
    private static final String U_PROJECTION_MATRIX = "u_ProjectionMatrix";

    private int uColorLocation;
    private int aPositionLocation;
    private int aColorLocation;

    private int uModelMatrixLocation;
    private int uViewMatrixLocation;
    private int uProjectionMatrixLocation;

    private ByteBuffer byteBuffer;

    private float[] cubeVertex = {
            //立方体前面的四个点
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            // 立方体后面的四个点
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
    };

    // 立方体索引,采用 GL_TRIANGLES 的方式绘制
    private byte[] indices = {
            // 前面索引
            0, 1, 2,
            3, 2, 1,
//            // 后面索引
            4, 5, 6,
            7, 6, 5,
//            // 上面索引
            0, 1, 4,
            5, 4, 1,
//            // 下面索引
            2, 3, 6,
            7, 6, 3,
//            // 左面索引
            0, 4, 2,
            6, 2, 4,
//            // 右侧索引
            1, 5, 3,
            7, 3, 5
    };


    final float[] cubeColor2 = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };


    public Cube(Context context) {
        super(context);

        POSITION_COMPONENT_COUNT = 3;

        vertexArray = new VertexArray(cubeVertex);

        indexArray = new VertexArray(cubeColor2);

        byteBuffer = ByteBuffer.allocateDirect(indices.length * Constants.BYTES_PRE_BYTE)
                .put(indices);

        byteBuffer.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

//        GLES20.glEnable(GLES20.GL_CULL_FACE);
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 2.0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = 0.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        mProgram = ShaderHelper.buildProgram(mContext, R.raw.cube_vertex_shader, R.raw.cube_fragment_shader);

        glUseProgram(mProgram);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);

        uColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        uModelMatrixLocation = glGetUniformLocation(mProgram, U_MODEL_MATRIX);

        uViewMatrixLocation = glGetUniformLocation(mProgram, U_VIEW_MATRIX);

        uProjectionMatrixLocation = glGetUniformLocation(mProgram, U_PROJECTION_MATRIX);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

//        indexArray.setVertexAttribPointer(0, aColorLocation, POSITION_COMPONENT_COUNT + 1, 0);

        setIdentityM(modelMatrix, 0);

    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        GLES20.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 6.0f;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);

    }


    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0f, 1.0f, 0.0f);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        glUniformMatrix4fv(uModelMatrixLocation, 1, false, modelMatrix, 0);

        glUniformMatrix4fv(uViewMatrixLocation, 1, false, viewMatrix, 0);

        glUniformMatrix4fv(uProjectionMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_BYTE, byteBuffer);

    }

}
