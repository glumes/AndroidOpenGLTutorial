package com.glumes.openglbasicshape.fbo;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.view.Surface;

import com.glumes.importobject.TextureRect;
import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.base.LogUtil;
import com.glumes.openglbasicshape.utils.FBOHelper;
import com.glumes.openglbasicshape.utils.MatrixState;
import com.glumes.openglbasicshape.utils.TextureHelper;


/**
 * @Author glumes
 */
public class FBORenderer {


    private static final int CFG_RED_SIZE = 8;
    private static final int CFG_GREEN_SIZE = 8;
    private static final int CFG_BLUE_SIZE = 8;
    private static final int CFG_ALPHA_SIZE = 8;
    private static final int CFG_DEPTH_SIZE = 16;
    private static final int CFG_STENCIL_SIZE = 0;

    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLConfig mEGLConfig;

    private int[] version = new int[2];

    private TextureRect mTextureRect;

    private static final int[] EGL_CONFIG = {
            EGL14.EGL_RED_SIZE, CFG_RED_SIZE,
            EGL14.EGL_GREEN_SIZE, CFG_GREEN_SIZE,
            EGL14.EGL_BLUE_SIZE, CFG_BLUE_SIZE,
            EGL14.EGL_ALPHA_SIZE, CFG_ALPHA_SIZE,
            EGL14.EGL_DEPTH_SIZE, CFG_DEPTH_SIZE,
            EGL14.EGL_STENCIL_SIZE, CFG_STENCIL_SIZE,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_NONE,
    };

    private static final int[] EGL_ATTRIBUTE = {
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE,
    };

    private static final int[] EGL_SURFACE = {
            EGL14.EGL_NONE,
    };

    public void init() {
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            LogUtil.d("get egl display failed");
        }

        boolean result = EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1);
        if (!result) {
            LogUtil.d("init egl failed");
        }

        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];

        EGL14.eglChooseConfig(mEGLDisplay, EGL_CONFIG, 0, null, 0, 0, numConfigs, 0);
        int num = numConfigs[0];
        if (num != 0) {
            EGL14.eglChooseConfig(mEGLDisplay, EGL_CONFIG, 0, configs, 0, configs.length, numConfigs, 0);
            mEGLConfig = configs[0];
        }

        mEGLContext = EGL14.eglCreateContext(mEGLDisplay, mEGLConfig, EGL14.EGL_NO_CONTEXT, EGL_ATTRIBUTE, 0);
        if (mEGLContext == EGL14.EGL_NO_CONTEXT) {
            LogUtil.d("create context failed");
        }

    }

    public void render(Surface surface, int width, int height, Context context) {
        mEGLSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, surface, EGL_SURFACE, 0);
        EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);

        // 绘制操作

        int fboId = FBOHelper.loadFBO();
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);

        int fboTextureId = TextureHelper.loadTexture(width, height);
        int renderbufferId = TextureHelper.loadRenderBuffer();

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, fboTextureId, 0);

        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            LogUtil.d("Framebuffer error");
        }

        mTextureRect = new TextureRect(context.getResources(), 2, 2);

        int textureId = TextureHelper.loadTexture(context, R.drawable.lgq);

        GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        MatrixState.setInitStack();

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glViewport(0, 0, width, height);
        float ratio = width * 1.0f / height * 1.0f;
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f);

        MatrixState.setCamera(0f, 0f, 4f, 0f, 0f, 0f, 0f, 1f, 0f);

        mTextureRect.drawSelf(textureId);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glViewport(0, 0, width, height);

        mTextureRect.drawSelf(fboTextureId);

        EGL14.eglSwapBuffers(mEGLDisplay, mEGLSurface);
    }

    public void release() {
        EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
        EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEGLDisplay);

        mEGLContext = EGL14.EGL_NO_CONTEXT;
        mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        mEGLConfig = null;
    }
}
