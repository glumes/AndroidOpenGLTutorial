package com.glumes.openglbasicshape.helper;

import android.content.Context;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_DITHER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform2fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by glumes on 08/09/2018
 */
public class TextureDrawer {

    private Context mContext;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureBuffer;
    private ByteBuffer mIndicesBuffer;
    private int mProgram;

    private int mInputWidth;
    private int mInputHeight;

    private int mDisplayWidth;
    private int mDisplayHeight;

    private int u_vp_matrix_handler;
    private int u_texture;
    private int a_position_handler;
    private int a_texcoord_handler;
    private int a_color;
    private int lut_tex_coord;
    private int input_size_handler;
    private int output_size_handler;
    private int texture_size_handler;
    private int scanline_bright_handler;

    private boolean scanlines = true;

    private float[] projectionMatrix = new float[16];
    private int mTextureId;

    private String mVertexShader;
    private String mFragmentShader;

    private byte[] indices = {
            0, 1, 2,
            0, 2, 3
    };

    private float[] vertex = {
            -0.5f, -0.5f, 0.0f,
            +0.5f, -0.5f, 0.0f,
            +0.5f, +0.5f, 0.0f,
            -0.5f, +0.5f, 0.0f
    };

    private float[] texture = {
            0f, 0f,
            1.0f, 0f,
            1.0f, 1.0f,
            0f, 1.0f
    };


    public TextureDrawer(Context context, String vertexShader, String fragmentShader) {
        mContext = context;
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;

        initVertexData();
        initTextureData();
        initIndicesData();
    }

    private void initVertexData() {
        mVertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        mVertexBuffer.position(0);
    }

    private void initIndicesData() {
        mTextureBuffer = ByteBuffer.allocateDirect(texture.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texture);
        mTextureBuffer.position(0);
    }

    private void initTextureData() {
        mIndicesBuffer = ByteBuffer.allocateDirect(indices.length).put(indices);
        mIndicesBuffer.position(0);
    }

    public void renderer(int textureId) {

        setHandlerInShader();

        bindTexture(textureId);

        enableAttribArray();

        draw();

        disableAttribArray();

        unBindTexture();
    }

    public void init() {
        createProgram();

        glUseProgram(mProgram);

        getHandlerInShader();

//        setHandlerInShader();
    }


    private void createProgram() {
        mProgram = ShaderHelper.buildProgramFromAssetFile(mContext, mVertexShader, mFragmentShader);
    }


    private void getHandlerInShader() {
        u_vp_matrix_handler = glGetUniformLocation(mProgram, "MVPMatrix");
        a_texcoord_handler = glGetAttribLocation(mProgram, "TexCoord");
        a_position_handler = glGetAttribLocation(mProgram, "VertexCoord");

        if (scanlines) {
            input_size_handler = glGetUniformLocation(mProgram, "InputSize");
            output_size_handler = glGetUniformLocation(mProgram, "OutputSize");
            texture_size_handler = glGetUniformLocation(mProgram, "TextureSize");
            scanline_bright_handler = glGetUniformLocation(mProgram, "SCANLINE_BASE_BRIGHTNESS");
        }
    }


    private void setHandlerInShader() {
        int texture_width = mInputWidth;
        int texture_height = mDisplayHeight / (mDisplayHeight / mInputHeight);

        float[] input_size = new float[2];
        float[] output_size = new float[2];
        float[] texture_size = new float[2];

        float scanline_bright = 0.85f;

        input_size[0] = mInputWidth;
        input_size[1] = mInputHeight;

        output_size[0] = mDisplayWidth;
        output_size[1] = mDisplayHeight;

        texture_size[0] = texture_width;
        texture_size[1] = texture_height;

        glUniform2fv(input_size_handler, 1, input_size, 0);
        glUniform2fv(output_size_handler, 1, output_size, 0);
        glUniform2fv(texture_size_handler, 1, texture_size, 0);
        glUniform1f(scanline_bright_handler, scanline_bright);

        Matrix.orthoM(projectionMatrix, 0, -0.5f, 0.5f, 0.5f, -0.5f, -1f, 1f);

        glUniformMatrix4fv(u_vp_matrix_handler, 1, false, projectionMatrix, 0);
    }

    private void bindTexture(int textureId) {
        mTextureId = textureId;
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, mTextureId);
    }

    private void enableAttribArray() {
        glVertexAttribPointer(a_position_handler, 3, GL_FLOAT, false, 0, mVertexBuffer);
        glEnableVertexAttribArray(a_position_handler);

        glVertexAttribPointer(a_texcoord_handler, 2, GL_FLOAT, false, 0, mTextureBuffer);
        glEnableVertexAttribArray(a_texcoord_handler);

    }


    private void draw() {
        glDisable(GL_DITHER);
        glDisable(GL_DEPTH_TEST);
        glClearColor(0, 0, 0, 0); // Black background
        glViewport(0, 0, mDisplayWidth, mDisplayHeight);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_BYTE, mIndicesBuffer);

        ShaderHelper.checkGlError("draw");
    }

    private void disableAttribArray() {
        glDisableVertexAttribArray(a_position_handler);
        glDisableVertexAttribArray(a_texcoord_handler);
    }

    private void unBindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0);
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
