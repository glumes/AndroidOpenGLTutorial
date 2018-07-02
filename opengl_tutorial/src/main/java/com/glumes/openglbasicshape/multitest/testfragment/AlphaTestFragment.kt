package com.glumes.openglbasicshape.multitest.testfragment

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.AlphaTextureRect
import com.glumes.importobject.Desert
import com.glumes.importobject.TreeGroup
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 25/06/2018
 */
class AlphaTestFragment : Fragment() {

    lateinit var surfaceView: AlphaTestSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = AlphaTestSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}

class AlphaTestSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(AlphaTestRenderer())
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    inner class AlphaTestRenderer : GLSurfaceView.Renderer {


        private var tg: TreeGroup? = null
        private var desert: Desert? = null
        private var rect: AlphaTextureRect? = null

        private var treeId: Int = 0
        private var desertId: Int = 0
        private var maskTextureId: Int = 0

        private var cx = 0f
        private var cz = 15f

        var ratio: Float? = 0f
        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
            MatrixState.setProjectFrustum(-ratio!!, ratio!!, -1f, 1f, 1f, 100f)
            MatrixState.setCamera(cx, 0f, cz, 0f, 0f, 0f, 0f, 1.0f, 0f)

            MatrixState.pushMatrix()
            MatrixState.translate(0f, -2f, 0f)
            desert!!.drawSelf(desertId)
            MatrixState.popMatrix()

            GLES20.glEnable(GLES20.GL_BLEND)

            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA)
            MatrixState.pushMatrix()
            MatrixState.translate(0f, -2f, 0f)
            tg!!.drawSelf(treeId)
            MatrixState.popMatrix()

            GLES20.glDisable(GLES20.GL_BLEND)

            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)

            MatrixState.pushMatrix()
            MatrixState.setProjectOrtho(-1f, 1f, -1f, 1f, 1f, 100f)
            MatrixState.setCamera(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            rect!!.drawSelf(maskTextureId)
            MatrixState.popMatrix()

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            ratio = width.toFloat() / height.toFloat()
            MatrixState.setProjectFrustum(-ratio!!, ratio!!, -1f, 1f, 1f, 100f)
            MatrixState.setCamera(0f, 0f, 15f, 0f, 0f, 0f, 0f, 1.0f, 0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            MatrixState.setInitStack()

            tg = TreeGroup(mContext.resources)
            rect = AlphaTextureRect(mContext.resources, 2f, 2f)

            tg!!.alist.sort()

            desert = Desert(mContext.resources, floatArrayOf(
                    0f, 0f,
                    0f, 6f,
                    6f, 6f,
                    6f, 6f,
                    6f, 0f,
                    0f, 0f
            ), 30, 20)

            treeId = TextureHelper.loadTexture(mContext, R.drawable.tree)
            desertId = TextureHelper.loadTexture(mContext, R.drawable.desert)
            maskTextureId = TextureHelper.loadTexture(mContext, R.drawable.mask)

        }

    }

}