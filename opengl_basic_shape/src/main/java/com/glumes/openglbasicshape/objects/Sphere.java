package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/8/9.
 */

public class Sphere extends BaseShape {

    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";


    private int uMatrixLocation;
    private int aPositionLocation;
    private int uColorLocation;


    float[] sphereVertex;

    private float step = 2.0f;
    private float step2 = 4.0f;
    private float radius = 1.0f;
    private int length;

    public Sphere(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.sphere_vertex_shader, R.raw.sphere_fragment_shader);

        glUseProgram(mProgram);

        POSITION_COMPONENT_COUNT = 3;

        sphereVertex = initSphereVertex();

        vertexArray = new VertexArray(sphereVertex);

        length = sphereVertex.length / 3;
    }


    @Override
    public void bindData() {

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
//        uColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0);

        setIdentityM(modelMatrix, 0);

    }


    @Override
    public void draw() {

        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);
//        glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, length);

    }

    @Override
    public void draw(float[] mvpMatrix) {
        super.draw(mvpMatrix);

        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);
//        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);

        glDrawArrays(GL_LINE_STRIP, 0, length);
    }

    private float[] initSphereVertex() {

        float r1;
        float r2;
        float y1;
        float y2;
        float cos;
        float sin;
        ArrayList<Float> data = new ArrayList<>();

        for (float i = -90.0f; i <= 90.0f; i += step) {

            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);

            y1 = (float) Math.sin(i * Math.PI / 180.0);
            y2 = (float) Math.sin((i + step) * Math.PI / 180.0);

            for (float j = 0.0f; j <= 360.0f; j += step2) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = (float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(y2);
                data.add(r2 * sin);

                data.add(r1 * cos);
                data.add(y1);
                data.add(r1 * sin);
            }
        }

        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }


}
