package com.glumes.openglbasicshape.draw.graph;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.draw.BaseShape;
import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.VertexArray;
import com.orhanobut.logger.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_LINES;
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

public class Line extends BaseShape {


    float[] lineVertex = {
            -1f, 1f,
            1f, -1f
    };


    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MODEL_MATRIX = "u_ModelMatrix";
    private static final String U_PROJECTION_MATRIX = "u_ProjectionMatrix";
    private static final String U_VIEW_MATRIX = "u_ViewMatrix";

    private int uProjectionMatrixLocation;
    private int uViewMatrixLocation;
    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;


    public Line(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.line_vertex_shader
                , R.raw.line_fragment_shader);

        glUseProgram(mProgram);

        vertexArray = new VertexArray(lineVertex);

        POSITION_COMPONENT_COUNT = 2;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MODEL_MATRIX);

        uProjectionMatrixLocation = glGetUniformLocation(mProgram, U_PROJECTION_MATRIX);

        uViewMatrixLocation = glGetUniformLocation(mProgram, U_VIEW_MATRIX);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        setIdentityM(modelMatrix, 0);
        setIdentityM(viewMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.5f, 0, 0);

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 10f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);


        Logger.d("width is " + width + " height is " + height);


        Matrix.perspectiveM(projectionMatrix, 0, 5f, (float) width / (float) height, 9f, 20f);

//        float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
//
//        if (width > height){
//
//            Matrix.frustumM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,5f,20f);
//
//        }else {
//
//            Matrix.frustumM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,5f,20f);
//
//        }

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        // 使用矩阵平移，将坐标 x 轴平移 0.5 个单位
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);

        glUniformMatrix4fv(uProjectionMatrixLocation, 1, false, projectionMatrix, 0);

        glUniformMatrix4fv(uViewMatrixLocation, 1, false, viewMatrix, 0);

        glDrawArrays(GL_LINES, 0, 2);
    }
}
