package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
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

public class Rectangle extends BaseShape {


    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;


    float[] rectangleVertex = {
            0f, 0f,
            -0.5f, -0.8f,
            0.5f, -0.8f,

            0.5f, 0.8f,
            -0.5f, 0.8f,
            -0.5f, -0.8f
    };

    public Rectangle(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.rectangle_vertex_shaper, R.raw.triangle_fragment_shader);

        glUseProgram(mProgram);

        vertexArray = new VertexArray(rectangleVertex);

        POSITION_COMPONENT_COUNT = 2;
    }

    @Override
    public void bindData() {
        super.bindData();

        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        setIdentityM(mvpMatrix, 0);

    }

    @Override
    public void draw(float[] mvpMatrix) {
        super.draw(mvpMatrix);

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }

    @Override
    public void draw() {
        super.draw();


        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }


}
