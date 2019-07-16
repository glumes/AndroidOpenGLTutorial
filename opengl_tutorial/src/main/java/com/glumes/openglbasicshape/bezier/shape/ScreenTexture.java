package com.glumes.openglbasicshape.bezier.shape;



import com.glumes.openglbasicshape.bezier.drawer.Constant;
import com.glumes.openglbasicshape.bezier.drawer.NormalSizeHelper;
import com.glumes.openglbasicshape.utils.GLHelper;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;


public class ScreenTexture extends ExpandGLShape {

    protected int mUTextureUnitLocation;
    protected int mTextureCoordinatesLocation;

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * Constant.FLOAT_SIZE;

    @Override
    protected void drawGL(float[] MVPMatrix) {
        float width = NormalSizeHelper.getAspectRatio() * 1;
        FloatBuffer screenBuffer = GLHelper.initFloatBuffer(new float[]{
                        // x y s t a
                        -width, 1f, 0f, 1f,
                        -width, -1f, 0f, 0f,
                        width, 1f, 1f, 1f,
                        width, -1f, 1f, 0f,
                }
        );

        screenBuffer.position(0);
        glVertexAttribPointer(mPositionHandle, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, screenBuffer);
        screenBuffer.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(mTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, screenBuffer);

        glUniformMatrix4fv(mMvpHandle, 1, false, MVPMatrix, 0);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    protected String getVertexShaderCode() {
        return "uniform mat4 uMVPMatrix;" +
                "attribute vec4 aPosition;" +
                "attribute vec2 a_TextureCoordinates;" +
                "varying vec2 v_TextureCoordinates;" +
                "void main() {" +
                "  v_TextureCoordinates = a_TextureCoordinates;" +
                "  gl_Position = uMVPMatrix * aPosition;" +
                "}";
    }

    @Override
    protected String getFragmentShaderCode() {
        return "precision mediump float;" +
                "uniform sampler2D u_TextureUnit;" +
                "varying vec2 v_TextureCoordinates;" +
                "void main() {" +
                "  gl_FragColor =  texture2D(u_TextureUnit, v_TextureCoordinates);" +
                "}";
    }

    @Override
    protected void findHandlersInGLSL() {
        super.findHandlersInGLSL();
        mUTextureUnitLocation = glGetUniformLocation(mProgramHandler, "u_TextureUnit");
        mTextureCoordinatesLocation = glGetAttribLocation(mProgramHandler, "a_TextureCoordinates");
    }

    @Override
    protected void enableAttrArrays() {
        super.enableAttrArrays();
        glEnableVertexAttribArray(mTextureCoordinatesLocation);
    }

    @Override
    protected void disableAttrArrays() {
        super.disableAttrArrays();
        glDisableVertexAttribArray(mTextureCoordinatesLocation);
    }

}
