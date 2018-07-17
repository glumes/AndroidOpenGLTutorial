package com.glumes.openglbasicshape.bufferusage


import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.VBOTextureRect
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class VBOFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return VBOUsage(context!!).also {
            it.requestFocus()
            it.isFocusableInTouchMode = true
        }
    }
}


class VBOUsage(var mContext: Context) : GLSurfaceView(mContext) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(VertexBufferRenderer())
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    inner class VertexBufferRenderer : GLSurfaceView.Renderer {

        var mShape: VBOTextureRect? = null

        var textureId: Int = 0

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

            mShape?.drawSelf(textureId)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)

            Matrix.setIdentityM(MatrixState.getMMatrix(), 0)
            Matrix.setIdentityM(MatrixState.getVMatrix(), 0)
            Matrix.setIdentityM(MatrixState.getProMatrix(), 0)

        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f)
//            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
//            GLES20.glEnable(GLES20.GL_CULL_FACE)
            MatrixState.setInitStack()
            mShape = VBOTextureRect(mContext.resources, 2.0f, 2.0f)

            textureId = TextureHelper.loadTexture(mContext, R.drawable.drawpen)
        }

    }

}