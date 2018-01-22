package com.glumes.openglbasicshape.objects.graph;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.objects.BaseShape;
import com.glumes.openglbasicshape.utils.ShaderHelper;

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
            -0.5f, 0.5f,
            0.5f, -0.5f
    };


    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
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
    public void bindData() {
        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, 0.5f, 0, 0);
    }

    @Override
    public void draw() {
        super.draw();

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        // 使用矩阵平移，将坐标 x 轴平移 0.5 个单位
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);
        glDrawArrays(GL_LINES, 0, 2);
    }
}
