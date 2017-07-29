package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/7/22.
 */

public class LineRenderer extends BaseRenderer {

    private FloatBuffer vertexData;

    float[] lineVertex = {
            -0.5f, 0.5f,
            0.5f, -0.5f
    };

    public static final int POSITION_COMPONENT_COUNT = 2;


    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    private float[] modelMatrix = new float[16];

    public LineRenderer(Context mContext) {
        super(mContext);

        vertexData = ByteBuffer.allocateDirect(lineVertex.length * BYTES_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(lineVertex);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


        aColorLocation = glGetUniformLocation(program, U_COLOR);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, 0.5f, 0, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        // 使用矩阵平移，将坐标 x 轴平移 0.5 个单位
        glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);
        glDrawArrays(GL_LINES, 0, 2);

    }

    @Override
    public void readShaderSource() {
        vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.line_vertex_shader);
        fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.line_fragment_shader);
    }
}
