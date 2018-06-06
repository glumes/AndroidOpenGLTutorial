package com.glumes.openglbasicshape.obj;

import android.content.Context;

import com.glumes.openglbasicshape.draw.BaseShape;
import com.glumes.openglbasicshape.utils.ShaderHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Author glumes
 */
public class LoadedObjectVertexOnly extends BaseShape {


    public LoadedObjectVertexOnly(Context context, float[] vertices) {
        super(context);
//        mProgram = ShaderHelper.buildProgram(context,)

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl, float[] mvpMatrix) {
        super.onDrawFrame(gl, mvpMatrix);
    }
}
