package com.glumes.openglbasicshape.transition_banner;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.transition.TransitionDrawer;
import com.glumes.openglbasicshape.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Author glumes
 */
public class TransitionBannerRender implements GLSurfaceView.Renderer {

    private TransitionBannerDrawer mTransitionDrawer;
    private Context mContext;

    private int mTextureId;
    private int mTextureId2;
    private float[] mMMatrix;

    private float mProgress = 0f;

    private int mWidth;
    private int mHeight;

    public TransitionBannerRender(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mTransitionDrawer = new TransitionBannerDrawer(mContext.getResources());
        mMMatrix = new float[16];
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        mWidth = width;
        mHeight = height;

        mTextureId = TextureHelper.loadTexture(mContext, R.drawable.texture);
        mTextureId2 = TextureHelper.loadTexture(mContext, R.drawable.drawpen);
        Matrix.setIdentityM(mMMatrix, 0);

        mTransitionDrawer.setDirection(1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        mTransitionDrawer.setProgress(mProgress);
        mTransitionDrawer.drawSelf(mTextureId, mTextureId2, mMMatrix);
//        if (mProgress >= 1.0f) {
//            mProgress = 0.0f;
//        } else {
//            mProgress += 0.01;
//        }

        Log.d("zhy","transition progress is " + mProgress);
    }

    public void setProgress(float progress){

        progress = Math.abs(progress);
        mProgress = progress / mWidth;

    }
}
