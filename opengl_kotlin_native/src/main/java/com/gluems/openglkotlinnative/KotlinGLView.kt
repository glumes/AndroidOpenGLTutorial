package com.gluems.openglkotlinnative

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.View
import com.orhanobut.logger.Logger
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

const val ANGLE_SPAN: Float = 0.375f


/**
 * @Author  glumes
 */
class KotlinGLView(context: Context) : GLSurfaceView(context) {

    var mRender: SceneRender

    var mContext: Context

    lateinit var rThread: RotateThread

    init {
        setEGLContextClientVersion(3)
        mRender = SceneRender()
        setRenderer(mRender)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY


        mContext = context
    }


    inner class SceneRender : GLSurfaceView.Renderer {

        lateinit var triangle: Triangle


        override fun onDrawFrame(p0: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            triangle.drawSelf()
        }

        override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
            GLES30.glViewport(0, 0, p1, p2)


            val ratio: Float = (p1.toFloat() / p2.toFloat())


            Logger.e("ratio is " + ratio)
            Matrix.frustumM(Triangle.mProjMatrix, 0, ratio * -1f, ratio, -1f, 1f, 1f, 10f)
            Matrix.setLookAtM(Triangle.mVMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f,
                    0f, 1.0f, 0f)
        }

        override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
            GLES30.glClearColor(0f, 0f, 0f, 1.0f)
            triangle = Triangle(this@KotlinGLView)
            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            rThread = RotateThread()
            rThread.start()
        }

    }

    inner class RotateThread : Thread() {

        val flag: Boolean = true

        override fun run() {
            while (flag) {

                if (mRender.triangle.xAngle != null) {
                    mRender.triangle.xAngle = mRender.triangle.xAngle!! + ANGLE_SPAN
                    Logger.d("change xAngle" + mRender.triangle.xAngle)
                }
                Thread.sleep(20)
            }
        }

    }

}
