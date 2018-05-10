package com.glumes.openglbasicshape.draw.graph;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.draw.BaseShape;
import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.VertexArray;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/7/30.
 */

public class Triangle extends BaseShape {


    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    public static final String  U_PRO_MATRIX = "u_ProMatrix";


    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    private int uProMatrixLocation;

    float[] triangleVertex = {
//            -1f, 1f,
////            -0.5f, 0f,
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f,

            0.5f, 0f,
            0f, 1.0f,
            1.0f, 1.0f
//            0f, 0f
    };

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

    };


    byte[] index = {
            0, 1, 2,
            3, 0, 2
    };


    private ByteBuffer byteBuffer;

    public Triangle(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.triangle_vertex_shader, R.raw.triangle_fragment_shader);

        glUseProgram(mProgram);

        vertexArray = new VertexArray(triangleVertex);

        byteBuffer = ByteBuffer.allocateDirect(position.length).put(position);

        byteBuffer.position(0);

        POSITION_COMPONENT_COUNT = 2;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        uProMatrixLocation = glGetUniformLocation(mProgram,U_PRO_MATRIX);


        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix,0,180f,1f,0f,0f);
        Matrix.translateM(modelMatrix,0,-0.5f,-0.5f,0f);

//        Matrix.translateM(modelMatrix, 0, 0.5f, 0, 0);
//        setIdentityM(projectionMatrix,0);

    }



    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;

        if (width > height){
            Matrix.orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,0f,10f);
        }else {
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,0f,10f);
        }
    }



    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        glUniform4f(aColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);

        glUniformMatrix4fv(uProMatrixLocation,1,false,projectionMatrix,0);

        // 使用 glDrawArrays方式绘图
        glDrawArrays(GL_TRIANGLES, 0, 3);
        // 使用 glDrawElements 方式绘图
//        glDrawElements(GL_TRIANGLES, position.length, GL_UNSIGNED_BYTE, byteBuffer);

    }

    @Override
    public void onDrawFrame(GL10 gl, float[] mvpMatrix) {
        super.onDrawFrame(gl, mvpMatrix);

        glUniform4f(aColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);

//        glDrawElements(GL_TRIANGLES, position.length, GL_UNSIGNED_BYTE, byteBuffer);

    }
}
