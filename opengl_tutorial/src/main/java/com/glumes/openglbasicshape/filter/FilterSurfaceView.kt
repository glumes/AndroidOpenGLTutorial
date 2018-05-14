package com.glumes.openglbasicshape.filter

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.view.MotionEvent

/**
 * Created by glumes on 27/03/2018
 */
class FilterSurfaceView(context: Context) : GLSurfaceView(context), SurfaceTexture.OnFrameAvailableListener {


    private val mRender = FilterRender(context, this)

    private val mDistance = 300

    init {
        setEGLContextClientVersion(2)
        setRenderer(mRender)
        renderMode = RENDERMODE_WHEN_DIRTY
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        val action = event.action
        var mLastX = 0f
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x
            }
            MotionEvent.ACTION_UP -> {
                if (Math.abs(event.x - mLastX) > mDistance) {
                    changeFilter()
                }
            }
        }
        return true
    }


    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        requestRender()
    }


    private fun changeFilter() {
        queueEvent {
            mRender.changeFilter()
        }
    }
}