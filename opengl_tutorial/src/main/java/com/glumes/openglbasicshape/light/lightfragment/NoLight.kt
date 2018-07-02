package com.glumes.openglbasicshape.light.lightfragment

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.openglbasicshape.blend.blendfragment.BlendSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author  glumes
 */
class NoLight : Fragment() {

    lateinit var surfaceView: BlendSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = BlendSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}

class NoLightSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {

    init {
        setEGLContextClientVersion(2)
    }

    inner class NoLightRenderer : GLSurfaceView.Renderer {
        override fun onDrawFrame(gl: GL10?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}