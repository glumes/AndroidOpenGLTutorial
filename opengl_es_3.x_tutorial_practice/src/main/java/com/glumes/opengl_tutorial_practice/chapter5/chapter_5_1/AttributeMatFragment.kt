package com.glumes.opengl_tutorial_practice.chapter5.chapter_5_1


import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.glumes.opengl_tutorial_practice.R
import com.glumes.opengl_tutorial_practice.shape.AttributeMatShape
import com.glumes.opengl_tutorial_practice.utils.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AttributeMatFragment : Fragment() {

    lateinit var mView: AttributeMatSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = AttributeMatSurfaceView(context!!)
        mView.requestFocus()
        mView.isFocusableInTouchMode = true
        return mView
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }

}

class AttributeMatSurfaceView(context: Context) : GLSurfaceView(context) {

    init {
        setEGLContextClientVersion(3)
        setRenderer(AttributeMatRenderer(context))
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }


    inner class AttributeMatRenderer(val mContext: Context) : GLSurfaceView.Renderer {

        lateinit var mShape: AttributeMatShape

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

            mShape.drawSelf()

            Log.d("mat", "onDrawFrame")
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)
            val ratio = width.toFloat() / height
            MatrixState.setProjectOrtho(-ratio, ratio, -1f, 1f, 1f, 10f)
            MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
            mShape = AttributeMatShape(mContext.resources)

            MatrixState.setInitStack()
        }

    }
}
