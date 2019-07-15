package com.glumes.openglbasicshape.bezier.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Class which provides helper functions for OpenGL objects
 */
public abstract class GLHelper {

    private static final String TAG = "GLHelper";

    /**
     * Initializes a buffer with the size of content
     */
    public static FloatBuffer initFloatBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size * 4);
        bb.order(ByteOrder.nativeOrder());

        return bb.asFloatBuffer();
    }

    /**
     * Initializes a buffer with the size of content, and places content inside
     */
    public static FloatBuffer initFloatBuffer(float[] content) {
        FloatBuffer buffer = initFloatBuffer(content.length);
        buffer.put(content);
        buffer.position(0);

        return buffer;
    }

    /**
     * Initializes a buffer with the size of content, and places content inside
     */
    public static ShortBuffer initShortBuffer(short[] content) {
        ByteBuffer bb = ByteBuffer.allocateDirect(content.length * 2);
        bb.order(ByteOrder.nativeOrder());

        ShortBuffer buffer = bb.asShortBuffer();
        buffer.put(content);
        buffer.position(0);

        return buffer;
    }
    
    public static void loadShaders(int programHandle, String vertexShaderCode, String fragmentShaderCode) {
        // prepare shaders and OpenGL program
        int vertexShaderId = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShaderId = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        GLES30.glAttachShader(programHandle, vertexShaderId);   // add the vertex shader to program
        GLES30.glAttachShader(programHandle, fragmentShaderId); // add the fragment shader to program
        GLES30.glLinkProgram(programHandle);                  // create OpenGL program executables

        // Get the link status.
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

        // If the link failed, delete the program.
        if (linkStatus[0] == 0) {
            Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
            GLES20.glDeleteProgram(programHandle);
            programHandle = 0;
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }
    }

    public static int loadShader(int shaderType, String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderSource);

            GLES20.glCompileShader(shaderHandle);

            final int[] compilationStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compilationStatus, 0);

            if (compilationStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            Log.e(TAG, "Error compiling shader " + GLES20.glGetShaderInfoLog(shaderHandle));
            throw new RuntimeException("Error creating shader. do you call on GLSurfaceView render thread ?");
        }

        return shaderHandle;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public static float[] toOpenGlColor(int color) {
        return new float[]{
                Color.red(color) / 255f,
                Color.green(color) / 255f,
                Color.blue(color) / 255f,
                Color.alpha(color) / 255f
        };
    }

    public static String readGLSL(final Context context, final int resId) {
        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(resId))
        );

        String line;
        final StringBuilder body = new StringBuilder();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                body.append(line).append('\n');
            }
        } catch (IOException e) {
            return null;
        }

        return body.toString();
    }

    public static int loadTexture(Context context, int resourceId, int color) {
        // Read in the resource
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        bitmap = createBitmap(bitmap, color);
        return loadTexture(bitmap);
    }

    public static int loadTexture(Bitmap bitmap, int color) {
        Bitmap temBitmap = createBitmap(bitmap, color);
        return loadTexture(temBitmap);
    }

    public static int loadTexture(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            Log.w(TAG, "Could not generate a new OpenGL texture object.");

            return 0;
        }

        if (bitmap == null) {
            Log.w(TAG, "bitmap " + " could not be decoded.");
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // Set filtering: a default must be set, or the texture will be
        // black.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Load the bitmap into the bound texture.
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.

        glGenerateMipmap(GL_TEXTURE_2D);

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
//        bitmap.recycle();

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    public static int loadTexture(Context context, int resourceId) {
        return loadTexture(context, resourceId, Color.BLACK);
    }

    private static Bitmap createBitmap(Bitmap src, int color) {
        int w = src.getWidth();
        int h = src.getHeight();
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //这样用DSI_IN的图是有透明度的 [Sa * Da, Sa * Dc] 
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        canvas.drawColor(color);
        canvas.drawBitmap(src, 0, 0, paint);
        return newb;
    }
}
