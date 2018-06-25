package com.glumes.openglbasicshape.multitest.testfragment

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.LoadedObjectVertexNormal
import com.glumes.importobject.utils.ObjectLoadUtil
import com.glumes.openglbasicshape.obj.LoadedObjectVertexOnly
import com.glumes.openglbasicshape.utils.LoadUtil
import com.glumes.openglbasicshape.utils.MatrixState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author  glumes
 */
class ClipFaceFragment : Fragment() {

    lateinit var surfaceView: ClipFaceSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = ClipFaceSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}

class ClipFaceSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(ClipFaceRenderer())
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    inner class ClipFaceRenderer : GLSurfaceView.Renderer {

        var teapot: LoadedObjectVertexNormal? = null

        var countE = 0f
        var spanE = 0.01f

        override fun onDrawFrame(gl: GL10?) {
            if (countE >= 2) {
                spanE = -0.01f
            } else if (countE <= 0) {
                spanE = 0.01f
            }
            countE += spanE
            var e = floatArrayOf(1f, countE - 1, -countE + 1, 0f)
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
            MatrixState.pushMatrix()
            MatrixState.translate(0f, -2f, -25f)
            teapot!!.drawSelf(e)
            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            var ratio = width.toFloat() / height.toFloat()
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
            MatrixState.setCamera(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1f, 0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f)
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            GLES20.glEnable(GLES20.GL_CULL_FACE)
            MatrixState.setInitStack()
            MatrixState.setLightLocation(40f, 10f, 20f)
            teapot = ObjectLoadUtil.loadFromFileVertexOnly("ch.obj", mContext.resources)
        }

    }
}
