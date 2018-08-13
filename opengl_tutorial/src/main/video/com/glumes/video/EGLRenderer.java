package com.glumes.video;

import android.opengl.EGL14;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;


public class EGLRenderer {

    private static final int CFG_RED_SIZE = 8;
    private static final int CFG_GREEN_SIZE = 8;
    private static final int CFG_BLUE_SIZE = 8;
    private static final int CFG_ALPHA_SIZE = 8;
    private static final int CFG_DEPTH_SIZE = 16;
    private static final int CFG_STENCIL_SIZE = 0;

    private static final int[] EGL_CONFIGS = {
            EGL10.EGL_RED_SIZE, CFG_RED_SIZE,
            EGL10.EGL_GREEN_SIZE, CFG_GREEN_SIZE,
            EGL10.EGL_BLUE_SIZE, CFG_BLUE_SIZE,
            EGL10.EGL_ALPHA_SIZE, CFG_ALPHA_SIZE,
            EGL10.EGL_DEPTH_SIZE, CFG_DEPTH_SIZE,
            EGL10.EGL_STENCIL_SIZE, CFG_STENCIL_SIZE,
            EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL10.EGL_NONE,
    };

    private static final int[] EGL_ATTRIBUTE = {
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL10.EGL_NONE,
    };

    private EGL10 mEGL = null;

    private EGLContext mEGLContext = EGL10.EGL_NO_CONTEXT;

    private EGLDisplay mEGLDisplay = EGL10.EGL_NO_DISPLAY;

    private EGLSurface mEGLSurface = EGL10.EGL_NO_SURFACE;

    public void create(int w, int h) {

        // 使用 eglCreatePbufferSurface 就需要指定宽和高
        int[] surfaceAttr = {
                EGL10.EGL_WIDTH, w,
                EGL10.EGL_HEIGHT, h,
                EGL10.EGL_NONE,
        };

        mEGL = (EGL10) EGLContext.getEGL();
        mEGLDisplay = mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        mEGL.eglInitialize(mEGLDisplay, null);

        EGLConfig config = getEGLConfig(mEGL, mEGLDisplay);
        mEGLContext = mEGL.eglCreateContext(mEGLDisplay, config, EGL10.EGL_NO_CONTEXT, EGL_ATTRIBUTE);
        mEGLSurface = mEGL.eglCreatePbufferSurface(mEGLDisplay, config, surfaceAttr);
//        mEGL.eglCreateWindowSurface()
        mEGL.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
    }

    public void destroy() {
        mEGL.eglMakeCurrent(mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        mEGL.eglDestroyContext(mEGLDisplay, mEGLContext);
        mEGL.eglDestroySurface(mEGLDisplay, mEGLSurface);
        mEGL.eglTerminate(mEGLDisplay);
        mEGL = null;
        mEGLContext = EGL10.EGL_NO_CONTEXT;
        mEGLDisplay = EGL10.EGL_NO_DISPLAY;
        mEGLSurface = EGL10.EGL_NO_SURFACE;
    }

    private static EGLConfig getEGLConfig(EGL10 egl, EGLDisplay display) {
        // eglChooseConfig 用于获取满足所有 attribute 的 Config
        int[] configCount = {0};
        if (!egl.eglChooseConfig(display, EGL_CONFIGS, null, 0, configCount)) {
            return null;
        }

        EGLConfig[] configs = new EGLConfig[configCount[0]];
        if (!egl.eglChooseConfig(display, EGL_CONFIGS, configs, configCount[0], configCount)) {
            return null;
        }

        return chooseEGLConfig(egl, display, configs, configCount[0]);
    }

    private static EGLConfig chooseEGLConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs, int count) {
        if (configs == null) {
            return null;
        }

        int[] value = new int[1];
        for (int i = 0; i < count; ++i) {
            EGLConfig config = configs[i];
            if (config == null) {
                continue;
            }

            int d = getConfigAttribute(egl, display, config, value, EGL10.EGL_DEPTH_SIZE, 0);
            int s = getConfigAttribute(egl, display, config, value, EGL10.EGL_STENCIL_SIZE, 0);
            if (d >= CFG_DEPTH_SIZE && s >= CFG_STENCIL_SIZE) {
                int r = getConfigAttribute(egl, display, config, value, EGL10.EGL_RED_SIZE, 0);
                int g = getConfigAttribute(egl, display, config, value, EGL10.EGL_GREEN_SIZE, 0);
                int b = getConfigAttribute(egl, display, config, value, EGL10.EGL_BLUE_SIZE, 0);
                int a = getConfigAttribute(egl, display, config, value, EGL10.EGL_ALPHA_SIZE, 0);
                if (r == CFG_RED_SIZE && g == CFG_GREEN_SIZE && b == CFG_BLUE_SIZE && a == CFG_ALPHA_SIZE) {
                    return config;
                }
            }
        }

        return null;
    }

    private static int getConfigAttribute(EGL10 egl, EGLDisplay display,
                                          EGLConfig config, int[] value,
                                          int attribute, int defaultValue) {
        if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
            return value[0];
        }
        return defaultValue;
    }
}
