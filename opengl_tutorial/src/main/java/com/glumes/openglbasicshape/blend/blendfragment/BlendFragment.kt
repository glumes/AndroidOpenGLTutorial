package com.glumes.openglbasicshape.blend.blendfragment

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.LoadedObjectVertexNormalAverage
import com.glumes.importobject.LoadedObjectVertexNormalFace
import com.glumes.importobject.TextureRect
import com.glumes.importobject.utils.ObjectLoadUtil
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * @Author  glumes
 */
class BlendFragment : Fragment() {

    lateinit var surfaceView: BlendSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = BlendSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}

class BlendSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(BlendRenderer())
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    inner class BlendRenderer : GLSurfaceView.Renderer {

        var textureId: Int = 0

        var pm: LoadedObjectVertexNormalFace? = null
        var cft: LoadedObjectVertexNormalFace? = null
        var qt: LoadedObjectVertexNormalAverage? = null
        var yh: LoadedObjectVertexNormalAverage? = null
        var ch: LoadedObjectVertexNormalAverage? = null

        var rect: TextureRect? = null

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

            MatrixState.pushMatrix()

            MatrixState.pushMatrix()

            MatrixState.rotate(25f, 1f, 0f, 0f)


            pm!!.drawSelf()

            MatrixState.pushMatrix()
            MatrixState.scale(1.5f, 1.5f, 1.5f)

            MatrixState.pushMatrix()
            MatrixState.translate(-10f, 0f, 0f)
            cft!!.drawSelf()
            MatrixState.popMatrix()


            MatrixState.pushMatrix()
            MatrixState.translate(10f, 0f, 0f)
            qt!!.drawSelf()
            MatrixState.popMatrix()

            MatrixState.pushMatrix()
            MatrixState.translate(0f, 0f, -10f)
            yh!!.drawSelf()
            MatrixState.popMatrix()

            MatrixState.pushMatrix()
            MatrixState.translate(0f, 0f, 10f)
            ch!!.drawSelf()
            MatrixState.popMatrix()

            MatrixState.popMatrix()

            MatrixState.popMatrix()

            GLES20.glEnable(GLES20.GL_BLEND)
            GLES20.glBlendFunc(GLES20.GL_SRC_COLOR, GLES20.GL_ONE_MINUS_SRC_COLOR)

            MatrixState.pushMatrix()
            MatrixState.translate(0f, 0f, 25f)


            rect!!.drawSelf(textureId)
            MatrixState.popMatrix()

            GLES20.glDisable(GLES20.GL_BLEND)

            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            var ratio = width.toFloat() / height.toFloat()
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
            MatrixState.setCamera(0f, 0f, 50f, 0f, 0f, 0f, 0f, 1f, 0f)
            MatrixState.setLightLocation(100f, 100f, 100f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f)
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            GLES20.glEnable(GLES20.GL_CULL_FACE)
            MatrixState.setInitStack()
            textureId = TextureHelper.loadTexture(mContext, R.drawable.lgq)

            ch = ObjectLoadUtil.loadFromFileVertexOnlyAverage("ch2.obj", mContext.resources)
            pm = ObjectLoadUtil.loadFromFileVertexOnlyFace("pm.obj", mContext.resources)
            cft = ObjectLoadUtil.loadFromFileVertexOnlyFace("cft.obj", mContext.resources)
            qt = ObjectLoadUtil.loadFromFileVertexOnlyAverage("qt.obj", mContext.resources)
            yh = ObjectLoadUtil.loadFromFileVertexOnlyAverage("yh.obj", mContext.resources)
            rect = TextureRect(mContext.resources, 10f, 10f)

        }
    }
}