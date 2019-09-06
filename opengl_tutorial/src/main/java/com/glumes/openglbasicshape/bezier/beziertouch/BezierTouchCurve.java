package com.glumes.openglbasicshape.bezier.beziertouch;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.glumes.importobject.TextureRect;
import com.glumes.openglbasicshape.R;
import com.glumes.openglbasicshape.base.LogUtil;
import com.glumes.openglbasicshape.bezier.Buffers;
import com.glumes.openglbasicshape.bezier.Const;
import com.glumes.openglbasicshape.bezier.drawer.NormalSizeHelper;
import com.glumes.openglbasicshape.bezier.shape.ScreenTexture;
import com.glumes.openglbasicshape.utils.DebugUtil;
import com.glumes.openglbasicshape.utils.ShaderHelper;
import com.glumes.openglbasicshape.utils.TextureHelper;

import java.nio.FloatBuffer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glCheckFramebufferStatus;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glReadPixels;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform2f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glViewport;

public class BezierTouchCurve {
    private final Context mContext;

    private float[] mStartEndPoints;
    private float[] mControlPoints;
    private float[] mDataPoints;

    private int mProgram;
    private int mStartEndHandle;
    private int mControlHandle;


    private int mStartPointHandle;
    private int mEndPointHandle;
    private int mControlPoint1Handle;
    private int mControlPoint2Handle;

    private int mDataHandle;
    private int mAmpsHandle;
    private int mMvpHandle;

    private float mAmps = 1.0f;

    private FloatBuffer mBuffer;

    private final int mBufferId;

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mTemporaryMatrix = new float[16];

    public TimedPoint startPoint;
    public TimedPoint control1;
    public TimedPoint control2;
    public TimedPoint endPoint;

    final int[] fboId = new int[1];
    private final int[] textureId = new int[1];

    private TextureRect mTextureDrawer;

    private int mTextureId;

    private float[] mMMatrix;

    private int mWidth;
    private int mHeight;

    private ScreenTexture mScreenTexture;

    private BezierTextureDrawer mTextureRect;

    private BezierDrawView mDrawView;

    private ArrayList<Bezier> mBezierList = new ArrayList<>();

    public BezierTouchCurve(Context context, BezierDrawView bezierDrawView) {
        mContext = context;

        mDrawView = bezierDrawView;

        mProgram = ShaderHelper.buildProgram(mContext, R.raw.bezier_touch_vertex, R.raw.bezier_touch_fragment);

        glUseProgram(mProgram);

        mStartEndHandle = glGetUniformLocation(mProgram, "uStartEndData");
        mControlHandle = glGetUniformLocation(mProgram, "uControlData");

        mStartPointHandle = glGetUniformLocation(mProgram, "uStartPoint");
        mEndPointHandle = glGetUniformLocation(mProgram, "uEndPoint");
        mControlPoint1Handle = glGetUniformLocation(mProgram, "uControlPoint1");
        mControlPoint2Handle = glGetUniformLocation(mProgram, "uControlPoint2");

        mAmpsHandle = glGetUniformLocation(mProgram, "u_Amp");

        mDataHandle = glGetAttribLocation(mProgram, "aData");
//
        mMvpHandle = glGetUniformLocation(mProgram, "u_MVPMatrix");


        mStartEndPoints = new float[]{
                -1, 0,
                1, 0,
        };

        mControlPoints = new float[]{
                0, 0.5f,
                1, 0,
        };

//        startPoint = new TimedPoint().set(-1, 0);
//        endPoint = new TimedPoint().set(1, 0);
//
//        control1 = new TimedPoint().set(0, 0.5f);
//        control2 = new TimedPoint().set(1, 0);

        startPoint = new TimedPoint().set(706.346f, 893.91235f);
        endPoint = new TimedPoint().set(706.346f, 895.959f);

        control1 = new TimedPoint().set(706.3465f, 894.93567f);
        control2 = new TimedPoint().set(706.346f, 894.93567f);

        mDataPoints = genTData();

        mBuffer = Buffers.makeInterleavedBuffer(mDataPoints, Const.NUM_POINTS);

        final int buffers[] = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mBuffer.capacity() * Const.BYTES_PER_FLOAT,
                mBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        mBufferId = buffers[0];

        mBuffer = null;

        Matrix.setIdentityM(mModelMatrix, 0);

        mTextureDrawer = new TextureRect(mContext.getResources(), 2, 2);

        mTextureId = TextureHelper.loadTexture(mContext, R.drawable.texture);

        mMMatrix = new float[16];

        Matrix.setIdentityM(mMMatrix, 0);
//
        mScreenTexture = new ScreenTexture();
        mScreenTexture.createShape();

        mTextureRect = new BezierTextureDrawer(mContext.getResources(), 2, 2);
    }


    public void onSurfaceChanged(int width, int height) {

        mWidth = width;
        mHeight = height;
        glViewport(0, 0, width, mHeight);

        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
        NormalSizeHelper.setAspectRatio(aspectRatio);
        NormalSizeHelper.setSurfaceViewInfo(width, height);

        if (width > height) {
            NormalSizeHelper.setIsVertical(false);
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 3, 7);
        } else {
            NormalSizeHelper.setIsVertical(true);
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 3, 7);
        }

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

//        Matrix.setIdentityM(mMVPMatrix,0);

        initFBO(width, height);
    }


    private void initFBO(int width, int height) {
        glGenFramebuffers(1, fboId, 0);
        glGenTextures(1, textureId, 0);

        glBindTexture(GL_TEXTURE_2D, textureId[0]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glBindFramebuffer(GL_FRAMEBUFFER, fboId[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId[0], 0);

        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
    }


    public void drawTextureAndScreen() {

        LogUtil.d("actual draw");
        glBindFramebuffer(GL_FRAMEBUFFER, fboId[0]);

        drawFBOTexture();

        drawScreenTexture(mMVPMatrix);
    }

    private void drawFBOTexture() {
//        glBindFramebuffer(GL_FRAMEBUFFER,fboId[0]);
//        glBindFramebuffer(GL_FRAMEBUFFER,0);
        draw();

//        DebugUtil.readPixelDebug(mWidth, mHeight);
    }

    private void drawScreenTexture(float[] mvpMatrix) {

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        GLES20.glClearColor(0.0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        float[] resultMatrix = new float[16];
//
        Matrix.multiplyMM(resultMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMvpHandle, 1, false, mMVPMatrix, 0);


//        mTextureRect.drawSelf(mTextureId,resultMatrix);
//
        mTextureRect.drawSelf(textureId[0], mModelMatrix);

//
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, textureId[0]);
//        glBindTexture(GL_TEXTURE_2D, mTextureId);
//

//        mScreenTexture.draw(resultMatrix);
    }

    public void draw() {
//        GLES20.glClearColor(0.0f, 0f, 0f, 1f);
//        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        glUseProgram(mProgram);

        glUniform4f(mStartEndHandle,
                mStartEndPoints[0],
                mStartEndPoints[1],
                mStartEndPoints[2],
                mStartEndPoints[3]);

        glUniform4f(mControlHandle,
                mControlPoints[0],
                mControlPoints[1],
                mControlPoints[2],
                mControlPoints[3]);


        glUniform2f(mStartPointHandle,
                NormalSizeHelper.convertNormalX(startPoint.x),
                NormalSizeHelper.convertNormalY(startPoint.y));

        glUniform2f(mEndPointHandle,
                NormalSizeHelper.convertNormalX(endPoint.x),
                NormalSizeHelper.convertNormalY(endPoint.y));

        glUniform2f(mControlPoint1Handle,
                NormalSizeHelper.convertNormalX(control1.x),
                NormalSizeHelper.convertNormalY(control1.y));

        glUniform2f(mControlPoint2Handle,
                NormalSizeHelper.convertNormalX(control2.x),
                NormalSizeHelper.convertNormalY(control2.y));

        glUniform1f(mAmpsHandle, mAmps);

        final int stride = Const.BYTES_PER_FLOAT * Const.T_DATA_SIZE;

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferId);
        GLES20.glEnableVertexAttribArray(mDataHandle);
        GLES20.glVertexAttribPointer(mDataHandle,
                Const.T_DATA_SIZE,
                GLES20.GL_FLOAT,
                false,
                stride,
                0);

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        float[] resultMatrix = new float[16];

        Matrix.multiplyMM(resultMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMvpHandle, 1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, Const.NUM_POINTS * Const.POINTS_PER_TRIANGLE);

    }


    public BezierTouchCurve set(TimedPoint startPoint, TimedPoint control1,
                                TimedPoint control2, TimedPoint endPoint) {
        this.startPoint = startPoint;
        this.control1 = control1;
        this.control2 = control2;
        this.endPoint = endPoint;
//        LogUtil.d(
//                "start point x is " + startPoint.x + " start point y is " + startPoint.y +
//                        "end point x is " + endPoint.x + " end point y is " + endPoint.y +
//                        "control point 1 x is " + control1.x + " control point 1 y is " + control1.y +
//                        "control point 2 x is " + control2.x + " control point 2 y is " + control2.y
//        );

        LogUtil.d("request render");
        mDrawView.requestRender();

        return this;
    }

    int num = 0;

    public void addPoint(Bezier bezier) {
        LogUtil.d("add point start x " + bezier.startPoint.x + " end point is " + bezier.startPoint.y);
        mBezierList.add(bezier);

        LogUtil.d("start point x is " + mBezierList.get(num).startPoint.x + " end point is y" + mBezierList.get(num).endPoint.y);

        num++;
    }


    public void touchUp() {
        LogUtil.d("bezier num is " + mBezierList.size());
//        for (Bezier bezier : mBezierList) {
//            this.startPoint = bezier.startPoint;
//            this.control1 = bezier.control1;
//            this.control2 = bezier.control2;
//            this.endPoint = bezier.endPoint;
//            LogUtil.d("request render");
//            mDrawView.requestRender();
//        }

        for (int i = 0; i < mBezierList.size(); i++) {
            this.startPoint = mBezierList.get(i).startPoint;
            this.control1 = mBezierList.get(i).control1;
            this.control2 = mBezierList.get(i).control2;
            this.endPoint = mBezierList.get(i).endPoint;

            LogUtil.d("start point x is " + mBezierList.get(i).startPoint.x + " end point is y" + mBezierList.get(i).endPoint.y);

            LogUtil.d(
                    "start point x is " + startPoint.x + " start point y is " + startPoint.y +
                            "end point x is " + endPoint.x + " end point y is " + endPoint.y +
                            "control point 1 x is " + control1.x + " control point 1 y is " + control1.y +
                            "control point 2 x is " + control2.x + " control point 2 y is " + control2.y
            );
            mDrawView.requestRender();
        }
    }

    public void setBezier(List<Bezier> bezierList){
        for (int i = 0; i < bezierList.size(); i++) {
            this.startPoint = bezierList.get(i).startPoint;
            this.control1 = bezierList.get(i).control1;
            this.control2 = bezierList.get(i).control2;
            this.endPoint = bezierList.get(i).endPoint;

            LogUtil.d("start point x is " + bezierList.get(i).startPoint.x + " end point is y" + bezierList.get(i).endPoint.y);

            mDrawView.requestRender();
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private float[] genTData() {
        //  1---2
        //  | /
        //  3
        float[] tData = new float[Const.POINTS_PER_TRIANGLE * Const.T_DATA_SIZE * Const.NUM_POINTS];

        float step = 1f / (float) tData.length * 2f;

        for (int i = 0; i < tData.length; i += Const.POINTS_PER_TRIANGLE) {
            float t = (float) i / (float) tData.length;
            float t1 = (float) (i + 1) / (float) tData.length;
            float t2 = (float) (i + 2) / (float) tData.length;

            tData[i] = t;
            tData[i + 1] = t1;
            tData[i + 2] = t2;

        }

        return tData;
    }

    public void setAmp(float amp) {
        mAmps = amp;
    }


}
