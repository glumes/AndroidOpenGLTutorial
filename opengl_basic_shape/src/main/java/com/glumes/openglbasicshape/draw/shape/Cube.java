package com.glumes.openglbasicshape.draw.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.glumes.comlib.LogUtil;
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
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private int uColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;
    private int aColorLocation;

    private ByteBuffer byteBuffer;

    float[] cubeVertex = {

            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,

            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
    };

    byte[] position = {
            // Front
            1, 3, 0,
            0, 3, 2,

            // Back
            4, 6, 5,
            5, 6, 7,

            // Left
            0, 2, 4,
            4, 2, 6,

            // Right
            5, 7, 1,
            1, 7, 3,

            // Top
            5, 1, 4,
            4, 1, 0,

            // Bottom
            6, 2, 7,
            7, 2, 3

            // not position just test
//            12,34,54,
//
//            34,45,56,
//
//            23,45,44

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

    final float[] cubeColor =
            {


                    // Front face (red)
//                    1.0f, 0.0f, 0.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f,
//                    1.0f, 0.0f, 0.0f, 1.0f,

                    // Right face (green)
//                    0.0f, 1.0f, 0.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f,
//                    0.0f, 1.0f, 0.0f, 1.0f,

                    // Back face (blue)
//                    0.0f, 0.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,

                    // Left face (yellow)
//                    1.0f, 1.0f, 0.0f, 1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f,
//                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,

                    // Top face (cyan)
//                    0.0f, 1.0f, 1.0f, 1.0f,
//                    0.0f, 1.0f, 1.0f, 1.0f,
//                    0.0f, 1.0f, 1.0f, 1.0f,
//                    0.0f, 1.0f, 1.0f, 1.0f,
//                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,

                    // Bottom face (magenta)
//                    1.0f, 0.0f, 1.0f, 1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f,
//                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f
            };


    public Cube(Context context) {
        super(context);


        POSITION_COMPONENT_COUNT = 3;

        vertexArray = new VertexArray(cubeVertex);

        indexArray = new VertexArray(cubeColor2);

        byteBuffer = ByteBuffer.allocateDirect(position.length * Constants.BYTES_PRE_BYTE)
                .put(position);

        byteBuffer.position(0);

        LogUtil.d("index length is" + position.length);


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);



        mProgram = ShaderHelper.buildProgram(mContext, R.raw.cube_vertex_shader, R.raw.cube_fragment_shader);

        glUseProgram(mProgram);


        aColorLocation = glGetAttribLocation(mProgram, A_COLOR);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        indexArray.setVertexAttribPointer(0, aColorLocation, POSITION_COMPONENT_COUNT + 1, 0);

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
        final float far = 10.0f;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);

    }


    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        long time  = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        Matrix.setIdentityM(modelMatrix,0);

        Matrix.rotateM(modelMatrix,0,angleInDegrees,1.0f,1.0f,0.0f);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);

        glDrawElements(GL_TRIANGLES, position.length, GL_UNSIGNED_BYTE, byteBuffer);

    }

    @Override
    public void onDrawFrame(GL10 gl, float[] mvpMatrix) {

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);

        glDrawElements(GL_TRIANGLES, position.length, GL_UNSIGNED_BYTE, byteBuffer);
    }
}
