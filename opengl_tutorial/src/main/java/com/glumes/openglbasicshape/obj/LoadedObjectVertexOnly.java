package com.glumes.openglbasicshape.obj;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.base.LogUtil;
import com.glumes.openglbasicshape.draw.BaseShape;
import com.glumes.openglbasicshape.utils.LoadUtil;
import com.glumes.openglbasicshape.utils.MatrixState;
import com.glumes.openglbasicshape.utils.MatrixStateOnly;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Objects;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Author glumes
 */
public class LoadedObjectVertexOnly extends BaseShape {

    public static final String VERTEX_FILE_NAME = "vertex_color.glsl";
    public static final String FRAGMENT_FILE_NAME = "frag_color.glsl";
    public static final String OBJECT_FILE_NAME = "ch.obj";
    int vCount = 0;
    FloatBuffer mVertexBuffer;
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用

    private MatrixStateOnly mMatrixStateOnly;

    public LoadedObjectVertexOnly(Context context) {
        super(context);

        mMatrixStateOnly = new MatrixStateOnly();

        mProgram = ShaderHelper.buildProgramFromAssetFile(context, VERTEX_FILE_NAME, FRAGMENT_FILE_NAME);

        float[] vertices = Objects.requireNonNull(LoadUtil.loadFromFile(OBJECT_FILE_NAME, context));
        initVertexData(vertices);

    }

    private void initVertexData(float[] vertices) {
        vCount = vertices.length / 3;
        mVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        MatrixState.setInitStack();

        GLES20.glUseProgram(mProgram);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

//
//        width = 430;
//        height = 600;

        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

//
//        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 100);
//        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 0, 0f, 0f, -1f, 0f, 1.0f, 0.0f);

        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
        MatrixState.setCamera(0, 0, 0, 0f, 0f, -1f, 0f, 1.0f, 0.0f);
//

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);



        MatrixState.pushMatrix();
        MatrixState.translate(0, -2f, -25f);


        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);

        MatrixState.popMatrix();
    }
}
