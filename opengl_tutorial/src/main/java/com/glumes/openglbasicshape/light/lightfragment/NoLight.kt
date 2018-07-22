package com.glumes.openglbasicshape.light.lightfragment

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.Ball
import com.glumes.importobject.utils.BallTypeHelper
import com.glumes.openglbasicshape.utils.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author  glumes
 */
class NoLight : Fragment() {

    lateinit var surfaceView: NoLightSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = NoLightSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}

class NoLightSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {


    var mRenderer: NoLightRenderer
    lateinit var mShape: Ball
    var mPreviousY = 0f
    var mPreviousX = 0f

    var TOUCH_SCALE_FACTOR = 180.0f / 320

    init {

        mRenderer = NoLightRenderer()
        setEGLContextClientVersion(3)
        setRenderer(mRenderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviousY
                val dx = x - mPreviousX
                mShape.yAngle += dx * TOUCH_SCALE_FACTOR
                mShape.xAngle += dy * TOUCH_SCALE_FACTOR
            }
        }
        mPreviousX = y
        mPreviousX = x
        return true
    }

    inner class NoLightRenderer : GLSurfaceView.Renderer {

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
            MatrixState.pushMatrix()

            MatrixState.pushMatrix()
            mShape.drawSelf()
            MatrixState.popMatrix()

            MatrixState.popMatrix()

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
            val ratio = width.toFloat() / height.toFloat()
            MatrixState.setInitStack()
            MatrixState.setCamera(0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0f)
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 20f, 100f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(0f, 0f, 0f, 0f)
            mShape = Ball(mContext.resources, BallTypeHelper.NormalBall)

        }

    }
}