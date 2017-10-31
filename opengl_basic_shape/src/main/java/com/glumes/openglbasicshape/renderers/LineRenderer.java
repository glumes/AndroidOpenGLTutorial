package com.glumes.openglbasicshape.renderers;

import android.content.Context;
import android.opengl.Matrix;

import com.glumes.openglbasicshape.objects.Line;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.setIdentityM;

/**
 * Created by glumes on 2017/7/22.
 */

public class LineRenderer extends BaseRenderer {


    private Line mLine;

    public LineRenderer(Context mContext) {
        super(mContext);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        mLine = new Line(mContext);

        mLine.bindData();
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
        mLine.draw();
    }


}
