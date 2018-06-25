package com.glumes.openglbasicshape.blend.blendfragment

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.TextureRect
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 25/06/2018
 */
class ETCFragment : Fragment() {

    lateinit var surfaceView: ETCSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = ETCSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}

class ETCSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(ETCRenderer())
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    inner class ETCRenderer : GLSurfaceView.Renderer {

        var rect: TextureRect? = null

        var textureId: Int = 0
        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
            rect!!.drawSelf(textureId)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            var ratio = width.toFloat() / height.toFloat()
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 1f, 10f)
            MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
            rect = TextureRect(mContext.resources, 2f, 2f)
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            MatrixState.setInitStack()
            textureId = TextureHelper.loadECTTexture(mContext, R.raw.house)

//            textureId = TextureHelper.loadTexture(mContext, R.drawable.lgq)

            GLES20.glDisable(GLES20.GL_CULL_FACE)
        }

    }
}