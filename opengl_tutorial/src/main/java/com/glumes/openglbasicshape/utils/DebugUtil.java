package com.glumes.openglbasicshape.utils;

import android.graphics.Bitmap;

import com.glumes.openglbasicshape.base.LogUtil;

import java.nio.IntBuffer;

import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glReadPixels;

public class DebugUtil {

    public static void readPixelDebug(int width, int height) {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        IntBuffer pixels = IntBuffer.allocate(width * height);

        pixels.clear();

        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);


        int[] pixelMirroredArray = new int[width * height];

        pixels.position(0);

        int[] pixelArray = pixels.array();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelMirroredArray[(height -i -1) * width +j] = pixelArray[i * width + j];
            }
        }

        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray));

        LogUtil.d("debug watch bitmap");

    }
}
