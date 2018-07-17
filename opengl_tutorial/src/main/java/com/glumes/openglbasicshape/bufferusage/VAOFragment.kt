package com.glumes.openglbasicshape.bufferusage


import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.VAOTextureRect
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper3
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class VAOFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return VAOUsage(context!!).also {
            it.requestFocus()
            it.isFocusableInTouchMode = true
        }
    }
}


class VAOUsage(var mContext: Context) : GLSurfaceView(mContext) {

    init {
        setEGLContextClientVersion(3)
        setRenderer(VAORenderer())
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    inner class VAORenderer : GLSurfaceView.Renderer {

        var mShape: VAOTextureRect? = null

        var textureId: Int = 0

        override fun onDrawFrame(gl: GL10?) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

            mShape?.drawSelf(textureId)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES30.glViewport(0, 0, width, height)

            Matrix.setIdentityM(MatrixState.getMMatrix(), 0)
            Matrix.setIdentityM(MatrixState.getVMatrix(), 0)
            Matrix.setIdentityM(MatrixState.getProMatrix(), 0)

        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES30.glClearColor(0.3f, 0.3f, 0.3f, 1.0f)
//            GLES30.glEnable(GLES30.GL_DEPTH_TEST)
//            GLES30.glEnable(GLES30.GL_CULL_FACE)
            MatrixState.setInitStack()
            mShape = VAOTextureRect(mContext.resources, 2.0f, 2.0f)

            textureId = TextureHelper3.loadTexture(mContext, R.drawable.drawpen)
        }

    }

}