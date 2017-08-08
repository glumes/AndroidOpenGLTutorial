package com.glumes.openglbasicshape.objects;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import static android.opengl.GLES20.GL_LINE_LOOP;
import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by glumes on 2017/7/30.
 */

public class Triangle extends BaseShape {


    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    float[] triangleVertex = {
            -0.5f, 0.5f,
//            -0.5f, 0f,
            -0.5f, -0.5f,
            0.5f, -0.5f
//            0f, 0f
    };


    public Triangle(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.triangle_vertex_shader, R.raw.triangle_fragment_shader);

        glUseProgram(mProgram);

        vertexArray = new VertexArray(triangleVertex);

        POSITION_COMPONENT_COUNT = 2;
    }

    @Override
    public void bindData() {
        super.bindData();

        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        setIdentityM(modelMatrix, 0);
//        Matrix.translateM(modelMatrix, 0, 0.5f, 0, 0);
    }

    @Override
    public void draw() {
        super.draw();

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    @Override
    public void draw(float[] mvpMatrix) {
        super.draw(mvpMatrix);

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
