package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by glumes on 2017/7/29.
 */

public class Point extends BaseShape {

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private int aColorLocation;
    private int aPositionLocation;


    float[] pointVertex = {
            0f, 0f
    };

    public Point(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.point_vertex_shader
                , R.raw.point_fragment_shader);

        glUseProgram(mProgram);

        vertexArray = new VertexArray(pointVertex);

        POSITION_COMPONENT_COUNT = 2;

    }

    @Override
    public void bindData() {
        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT,
                0);
    }

    @Override
    public void draw() {

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 0, 1);
    }

    @Override
    public void draw(float[] mvpMatrix) {
        super.draw(mvpMatrix);
    }
}
