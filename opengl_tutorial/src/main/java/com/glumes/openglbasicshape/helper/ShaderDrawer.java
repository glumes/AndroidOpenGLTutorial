package com.glumes.openglbasicshape.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.IntBuffer;

import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glReadPixels;

/**
 * Created by glumes on 08/09/2018
 */
public class ShaderDrawer {

    private int mInputWidth;
    private int mInputHeight;

    private int mDisplayWidth;
    private int mDisplayHeight;

    private TextureDrawer mTextureDrawerFBO;
    private TextureDrawer mTextureDrawerScreen;

    private int mFBOTextureId;
    private int mFBO;
    private Context mContext;

    public ShaderDrawer(Context context) {
        mContext = context;
        mTextureDrawerFBO = new TextureDrawer(mContext, Constant.VERTEX_SHADER, Constant.FRAGMENT_SHADER);
        mTextureDrawerScreen = new TextureDrawer(mContext, Constant.VERTEX_SHADER_SCANLINES, Constant.FRAGMENT_SHADER_SCANLINES);
    }

    public void init() {
        mTextureDrawerFBO.setInputSize(mInputWidth, mInputHeight);
        mTextureDrawerFBO.setDisplaySize(mDisplayWidth, mDisplayHeight);
        mTextureDrawerFBO.init();

        mTextureDrawerScreen.setInputSize(mDisplayWidth, mDisplayHeight);
        mTextureDrawerScreen.setDisplaySize(mDisplayWidth, mDisplayHeight);
        mTextureDrawerScreen.init();

    }

    public void renderer(int inputTexture) {

        int texture = drawOnFBO(inputTexture);

        drawOnScreen(texture);

    }

    private int drawOnFBO(int inputTexture) {

        mFBO = TextureHelper.loadFBO();

        glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFBO);

        LogUtil.d("FBO is " + mFBO);

        mFBOTextureId = TextureHelper.loadTexture(mDisplayWidth, mDisplayHeight);

        glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mFBOTextureId, 0);

        mTextureDrawerFBO.renderer(inputTexture);

        IntBuffer ib = IntBuffer.allocate(mDisplayWidth * mDisplayHeight);

        glReadPixels(0, 0, mDisplayWidth, mDisplayHeight, GL_RGBA, GL_UNSIGNED_BYTE, ib);

        Bitmap bitmap = Bitmap.createBitmap(mDisplayWidth, mDisplayHeight, Bitmap.Config.ARGB_8888);

        bitmap.copyPixelsFromBuffer(ib);

        LogUtil.d("watch bitmap");

        return mFBOTextureId;
    }

    private void drawOnScreen(int fboTextureId) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        mTextureDrawerScreen.renderer(fboTextureId);
    }


    public void setInputSize(int iw, int ih) {
        mInputWidth = iw;
        mInputHeight = ih;
    }

    public void setDisplaySize(int dw, int dh) {
        mDisplayWidth = dw;
        mDisplayHeight = dh;
    }

}
