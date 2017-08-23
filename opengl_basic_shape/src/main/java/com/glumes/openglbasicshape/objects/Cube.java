package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.utils.Constant;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import java.nio.ByteBuffer;

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

        mProgram = ShaderHelper.buildProgram(context, R.raw.cube_vertex_shader, R.raw.cube_fragment_shader);

        glUseProgram(mProgram);

        POSITION_COMPONENT_COUNT = 3;

        vertexArray = new VertexArray(cubeVertex);

        indexArray = new VertexArray(cubeColor2);

        byteBuffer = ByteBuffer.allocateDirect(position.length * Constant.BYTES_PRE_BYTE)
                .put(position);


        byteBuffer.position(0);

        LogUtil.d("index length is" + position.length);

    }

    @Override
    public void bindData() {

//        uColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        aColorLocation = glGetAttribLocation(mProgram, A_COLOR);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        indexArray.setVertexAttribPointer(0, aColorLocation, POSITION_COMPONENT_COUNT + 1, 0);

        setIdentityM(mvpMatrix, 0);

    }

    @Override
    public void draw() {

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);

        glDrawElements(GL_TRIANGLES, position.length, GL_UNSIGNED_BYTE, byteBuffer);

    }

    @Override
    public void draw(float[] mvpMatrix) {

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);

        glDrawElements(GL_TRIANGLES, position.length, GL_UNSIGNED_BYTE, byteBuffer);
    }
}
