package com.glumes.gpuimage;

import android.graphics.PointF;
import android.opengl.GLES20;

/**
 * Created by glumes on 14/05/2018
 */
public class GPUImageSketchFilter extends GPUImageFilter {

    public static final String RGB_FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "  varying highp vec2 textureCoordinate;\n" +
            "  \n" +
            "  uniform samplerExternalOES inputImageTexture;\n" +
            "  uniform vec2 center;\n" +
            "  void main()\n" +
            "  {\n" +
            "  highp vec2 normCoord = 2.0 * textureCoordinate - 1.0;\n" +
            "  highp vec2 normCenter = 2.0 * center - 1.0;\n" +
            "  normCoord -= normCenter;\n" +
            "  mediump vec2 s = sign(normCoord);\n" +
            "  normCoord = abs(normCoord);\n" +
            "  normCoord = 0.5 * normCoord + 0.5 * smoothstep(0.25, 0.5, normCoord) * normCoord;\n" +
            "  normCoord = s * normCoord;\n" +
            "  normCoord += normCenter;\n" +
            "  mediump vec2 textureCoordinateToUse = normCoord / 2.0 + 0.5;\n" +
            "\n" +
            "  gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n" +
            "  }\n";

    private int mRedLocation;
    private float mRed;
    private int mGreenLocation;
    private float mGreen;
    private int mBlueLocation;
    private float mBlue;
    private boolean mIsInitialized = false;

    private int mCenterLocation;

    public GPUImageSketchFilter() {
        this(1.0f, 1.0f, 1.0f);
    }

    public GPUImageSketchFilter(final float red, final float green, final float blue) {
        super(NO_FILTER_VERTEX_SHADER, RGB_FRAGMENT_SHADER);
//        mRed = red;
//        mGreen = green;
//        mBlue = blue;
    }

    @Override
    public void onInit() {
        super.onInit();
//        mRedLocation = GLES20.glGetUniformLocation(getProgram(), "red");
//        mGreenLocation = GLES20.glGetUniformLocation(getProgram(), "green");
//        mBlueLocation = GLES20.glGetUniformLocation(getProgram(), "blue");

        mCenterLocation = GLES20.glGetUniformLocation(getProgram(), "center");

        mIsInitialized = true;

//        setRed(mRed);
//        setGreen(mGreen);
//        setBlue(mBlue);

        float[] vec2 = new float[2];
        vec2[0] = 0.5f;
        vec2[1] = 0.5f;
        setPoint(mCenterLocation, new PointF(0.5f, 0.5f));
    }

    public void setRed(final float red) {
        mRed = red;
        if (mIsInitialized) {
            setFloat(mRedLocation, mRed);
        }
    }

    public void setGreen(final float green) {
        mGreen = green;
        if (mIsInitialized) {
            setFloat(mGreenLocation, mGreen);
        }
    }

    public void setBlue(final float blue) {
        mBlue = blue;
        if (mIsInitialized) {
            setFloat(mBlueLocation, mBlue);
        }
    }
}
