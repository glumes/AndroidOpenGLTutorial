package com.glumes.openglbasicshape.utils;

import android.opengl.GLES20;

/**
 * @Author glumes
 */
public class FBOHelper {

    public static int loadFBO() {
        int[] framebuffers = new int[1];
        GLES20.glGenFramebuffers(1, framebuffers, 0);
        return framebuffers[0];
    }


}
