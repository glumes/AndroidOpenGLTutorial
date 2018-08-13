package com.glumes.openglbasicshape.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.ETC1Util;
import android.opengl.GLES20;

import com.glumes.openglbasicshape.R;

import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by glumes on 2017/8/1.
 */

public class TextureHelper {


    private static int[] imageFileIDs = { // Image file IDs
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
            R.drawable.f, R.drawable.g};

    private static int[] animalIds = {
            R.drawable.dog,
            R.drawable.elephant, R.drawable.frog,
            R.drawable.monkey, R.drawable.rabbit, R.drawable.tortoise, R.drawable.tigger
    };

    public static int CUBE = 0x01;
    public static int ANIMAL = 0x02;

    /**
     * 返回加载图像后的 OpenGl 纹理的 ID
     *
     * @param context
     * @param resourceId
     * @return
     */
    public static int loadTexture(Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (bitmap == null) {
            Timber.d("resource Id could not be decoded");
            return 0;
        }

        return loadTextureByBitmap(bitmap);
    }

    public static int loadECTTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            Timber.d("Could not generate a new OpenGL texture object.");
            return 0;
        }


        // 设置缩小的情况下过滤方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        // 设置放大的情况下过滤方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        InputStream is = context.getResources().openRawResource(resourceId);
        try {
            ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D, 0, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, is);
        } catch (Exception e) {
            Timber.e(e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return textureObjectIds[0];
    }

    public static int loadTextureByBitmap(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            Timber.d("Could not generate a new OpenGL texture object.");
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // 设置缩小的情况下过滤方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        // 设置放大的情况下过滤方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // 加载纹理到 OpenGL，读入 Bitmap 定义的位图数据，并把它复制到当前绑定的纹理对象
        // 当前绑定的纹理对象就会被附加上纹理图像。
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        // 为当前绑定的纹理自动生成所有需要的多级渐远纹理
        // 生成 MIP 贴图
        glGenerateMipmap(GL_TEXTURE_2D);

        // 解除与纹理的绑定，避免用其他的纹理方法意外地改变这个纹理
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }


    /**
     * 立方体纹理 生成多个纹理贴图
     *
     * @param context
     * @return
     */
    public static int[] loadCubeTexture(Context context, int type) {

        final int[] cubeTextureIds = new int[7];
        Bitmap bitmap;
        glGenTextures(6, cubeTextureIds, 0);

        int[] images = imageFileIDs;
        if (type == ANIMAL) {
            images = animalIds;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        for (int i = 0; i < 7; i++) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), images[i], options);

            glBindTexture(GL_TEXTURE_2D, cubeTextureIds[i]);
            // 设置缩小的情况下过滤方式
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            // 设置放大的情况下过滤方式
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();

            glGenerateMipmap(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        return cubeTextureIds;
    }


    public static int loadTexture(int width, int height) {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height,
                0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, null);
        return textureId;
    }


    public static int loadRenderBuffer() {
        int[] renderbuffer = new int[1];
        GLES20.glGenRenderbuffers(1, renderbuffer, 0);
        return renderbuffer[0];
    }
}
