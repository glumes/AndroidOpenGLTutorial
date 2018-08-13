package com.glumes.openglbasicshape.egl;


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
import com.glumes.openglbasicshape.utils.MatrixState;
import com.glumes.openglbasicshape.utils.TextureHelper;


public class EglDemo {


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

    // EGL 的主版本号
    private int[] mMajorVersion = new int[1];
    // EGL 的次版本号
    private int[] mMinorVersion = new int[1];

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

    public void initEgl() {
        // 创建与本地窗口系统的连接
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            // failed
        }

        boolean result = EGL14.eglInitialize(mEGLDisplay, mMajorVersion, 0, mMinorVersion, 0);
        if (!result) {
            // failed
        }

        // 所有符合配置的 EGLConfig 个数
        int[] numConfigs = new int[1];
        // 所有符合配置的 EGLConfig
        EGLConfig[] configs = new EGLConfig[1];

        // configs 参数为 null,会在 numConfigs 中输出所有满足 EGL_CONFIG 的 config 个数
        EGL14.eglChooseConfig(mEGLDisplay, EGL_CONFIG, 0, null, 0, 0, numConfigs, 0);
        // 得到满足条件的个数
        int num = numConfigs[0];
        if (num != 0) {
            // 会获取所有满足 EGL_CONFIG 的 config
            EGL14.eglChooseConfig(mEGLDisplay, EGL_CONFIG, 0, configs, 0, configs.length, numConfigs, 0);
            // 去第一个
            mEGLConfig = configs[0];
        }

        // 创建上下文
        mEGLContext = EGL14.eglCreateContext(mEGLDisplay, mEGLConfig, EGL14.EGL_NO_CONTEXT, EGL_ATTRIBUTE, 0);

        if (mEGLContext == EGL14.EGL_NO_CONTEXT) {
            // failed
        }

    }


    int textureId = 0;

    public void render(Surface surface, int width, int height, Context context) {
        mEGLSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, surface, EGL_SURFACE, 0);

        EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);

        mTextureRect = new TextureRect(context.getResources(), 2, 2);

        textureId = TextureHelper.loadTexture(context, R.drawable.lgq);

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

        // 执行绘制

        // 交换显存
        EGL14.eglSwapBuffers(mEGLDisplay, mEGLSurface);
        EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface);
    }



    // 获得 Config 的某个属性值
    private void printEglAttribute(EGLDisplay display, EGLConfig config, int attribute) {
        int[] value = new int[1];

        EGL14.eglGetConfigAttrib(display, config, attribute, value, 0);

    }
}
