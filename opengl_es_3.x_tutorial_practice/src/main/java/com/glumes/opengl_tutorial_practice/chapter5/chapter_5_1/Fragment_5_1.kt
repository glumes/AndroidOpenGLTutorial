package com.glumes.opengl_tutorial_practice.chapter5.chapter_5_1

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.glumes.opengl_tutorial_practice.shape.SixPointedStar
import com.glumes.opengl_tutorial_practice.utils.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 11/05/2018
 */


class Fragment_5_1 : Fragment() {


    lateinit var surfaceView: RendererSurface

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = RendererSurface(context)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }

    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        surfaceView.onPause()
    }
}


class RendererSurface(context: Context) : GLSurfaceView(context) {

    private val TOUCH_SCALE_FACTOR = 180.0f / 320

    private var mPreviewX: Float = 0f
    private var mPreviewY: Float = 0f

    var mRenderer: SceneRenderer

    init {
        setEGLContextClientVersion(3)
        mRenderer = SceneRenderer(context)
        setRenderer(mRenderer)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreviewY
                val dx = x - mPreviewX
                for (it in mRenderer.shape) {
                    it!!.yAngle += dx * TOUCH_SCALE_FACTOR
                    it!!.xAngle += dy * TOUCH_SCALE_FACTOR
                }
            }
        }
        mPreviewX = x
        mPreviewY = y
        return true
    }
}

class SceneRenderer(val mContext: Context) : GLSurfaceView.Renderer {


    var shape = arrayOfNulls<SixPointedStar>(6)//六角星数组


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        for (it in 0..5) {
            shape[it] = SixPointedStar(mContext.resources, 0.2f, 0.5f, -0.3f * it)
        }
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        MatrixState.setProjectOrtho(-ratio, ratio, -1f, 1f, 1f, 10f)
        MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        for (it in 0..5) {
            shape[it]!!.drawSelf()
        }
    }

}

