package com.glumes.openglbasicshape.objects;

import android.content.Context;

import com.glumes.comlib.LogUtil;
import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.data.VertexArray;
import com.glumes.openglbasicshape.utils.Constant;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static android.opengl.GLES20.GL_INT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
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
//    private static final String U_COLOR = "u_Color";


    private int uMatrixLocation;
    private int aPositionLocation;
//    private int uColorLocation;


    float[] sphereVertex;

    private float step = 2.0f;
    private float step2 = 4.0f;
    private float radius = 1.0f;
    private int length;

    private IntBuffer intBuffer;

    private int[] position;

    public Sphere(Context context) {
        super(context);

        mProgram = ShaderHelper.buildProgram(context, R.raw.sphere_vertex_shader, R.raw.sphere_fragment_shader);

        glUseProgram(mProgram);

        POSITION_COMPONENT_COUNT = 3;

        initSphereVertex2();

//        sphereVertex = initSphereVertex();

        vertexArray = new VertexArray(sphereVertex);

        length = sphereVertex.length / 3;

        LogUtil.d("buffer length is " + position.length);

        intBuffer = ByteBuffer.allocateDirect(position.length * Constant.BYTES_PRE_FLOAT).asIntBuffer().put(position);

        intBuffer.position(0);


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
//        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, length);

    }

    @Override
    public void draw(float[] mvpMatrix) {
        super.draw(mvpMatrix);

        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);
//        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);

//        glDrawArrays(GL_TRIANGLE_STRIP, 0, length);

        // 通过索引来绘制圆
        glDrawElements(GL_TRIANGLE_STRIP, position.length, GL_UNSIGNED_INT, intBuffer);

    }


    /**
     * 采用 glDrawElement 方式绘制
     *
     * @return
     */
    private float[] initSphereVertex2() {

        ArrayList<Float> data = new ArrayList<>();

        float r;
        float y;
        float sin;
        float cos;

        /**
         * 只遍历一边顶点就好了
         */
        for (float i = -90.0f; i < 90.0f; i += step) {

            r = (float) Math.cos(i * Math.PI / 180.0);
            y = (float) Math.sin(i * Math.PI / 180.0);

            for (float j = 0.0f; j < 360.0f; j += step2) {

                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = (float) Math.sin(j * Math.PI / 180.0);
                data.add(r * sin);
                data.add(y);
                data.add(r * cos);
            }
        }

        sphereVertex = new float[data.size()];

        for (int i = 0; i < data.size(); i++) {
            sphereVertex[0] = data.get(i);
        }

        ArrayList<Integer> indices = new ArrayList<>();

        int row = 90;
        int column = 90;
        int pos;
        for (int i = 0; i <= row; i++) {
            for (int j = 0; j <= column; j++) {

                // 当前点
                pos = (column) * i + j;

                indices.add(pos);
                indices.add(pos + 1);
                indices.add(pos + column + 1);

                indices.add(pos);
                indices.add(pos + column + 1);
                indices.add(pos + column);

            }
        }

        position = new int[indices.size()];

        for (int i = 0; i < indices.size(); i++) {

            position[i] = indices.get(i);

        }


        return null;
    }


    /**
     * 利用 glDrawArrays 绘制球体时的顶点要求
     * 采用 glDrawArrays 的方式绘制
     * 计算方式:
     * x = radius * cos * sin
     * y = radius * sin
     * z = radius * con * cos
     *
     * @return
     */
    private float[] initSphereVertex() {

        float r1;
        float r2;
        float y1;
        float y2;
        float cos;
        float sin;
        ArrayList<Float> data = new ArrayList<>();

        int count = 0;
        int count2 = 0;
        for (float i = -90.0f; i <= 90.0f; i += step) {

            count++;
            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);

            y1 = (float) Math.sin(i * Math.PI / 180.0);
            y2 = (float) Math.sin((i + step) * Math.PI / 180.0);

            for (float j = 0.0f; j <= 360.0f; j += step2) {

                count2++;
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = (float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * sin);
                data.add(y2);
                data.add(r2 * cos);

                data.add(r1 * sin);
                data.add(y1);
                data.add(r1 * cos);
            }
        }

        LogUtil.d("count is " + count);
        LogUtil.d("count2 is " + count2);

        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }

        return f;
    }


}
