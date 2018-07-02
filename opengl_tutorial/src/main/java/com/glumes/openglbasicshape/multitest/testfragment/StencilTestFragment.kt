package com.glumes.openglbasicshape.multitest.testfragment

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.importobject.BallForControl
import com.glumes.importobject.BallTextureByVertex
import com.glumes.importobject.TextureRect
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.utils.MatrixState
import com.glumes.openglbasicshape.utils.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 25/06/2018
 */

class StencilTestFragment : Fragment() {

    lateinit var surfaceView: StencilSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = StencilSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}


class StencilSurfaceView(var mContext: Context) : GLSurfaceView(mContext) {


    val UNIT_SIZE = 0.8f//球单位尺寸
    val BALL_SCALE = 1.0f//球单位尺寸
    val ANGLE_SPAN = 11.25f//将球进行单位切分的角度

    init {
        setEGLContextClientVersion(2)
        setRenderer(StencilRenderer())
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }


    inner class StencilRenderer : GLSurfaceView.Renderer {

        var texureRect: TextureRect? = null
        var ball: BallTextureByVertex? = null
        var bfg: BallForControl? = null
        var textureFloor: Int? = 0
        var textureFloorBTM: Int? = 0
        var textureBallId: Int? = 0
        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
            MatrixState.pushMatrix()
            MatrixState.translate(0f, -2f, 0f)

            GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT)

            GLES20.glEnable(GLES20.GL_STENCIL_TEST)

            GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 1)
            GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE)

            texureRect!!.drawSelf(textureFloor!!)

            GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 1)
            GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE)
            bfg!!.drawSelfMirror(textureBallId!!)
            GLES20.glDisable(GLES20.GL_STENCIL_TEST)

            GLES20.glEnable(GLES20.GL_BLEND)
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
            texureRect!!.drawSelf(textureFloorBTM!!)
            GLES20.glDisable(GLES20.GL_BLEND)

            bfg!!.drawSelf(textureBallId!!)
            MatrixState.popMatrix()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            var ratio = width.toFloat() / height.toFloat()
            MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 3f, 100f)
            MatrixState.setCamera(0f, 8f, 8f, 0f, 0f, 0f, 0f, 1f, 0f)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0f, 0f, 0f, 1f)
            texureRect = TextureRect(mContext.resources, 4f, 2.568f)
            ball = BallTextureByVertex(mContext.resources, BALL_SCALE)
            bfg = BallForControl(ball, 3f)

            GLES20.glEnable(GLES20.GL_DEPTH_TEST)

            textureFloor = TextureHelper.loadTexture(mContext, R.drawable.mdb)
            textureFloorBTM = TextureHelper.loadTexture(mContext, R.drawable.mdbtm)
            textureBallId = TextureHelper.loadTexture(mContext, R.drawable.basketball)

            GLES20.glEnable(GLES20.GL_CULL_FACE)

            MatrixState.setInitStack()
        }

    }


}
