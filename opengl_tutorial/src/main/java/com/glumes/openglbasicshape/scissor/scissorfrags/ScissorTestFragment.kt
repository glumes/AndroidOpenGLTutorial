package com.glumes.openglbasicshape.scissor.scissorfrags

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.openglbasicshape.base.LogUtil
import com.glumes.openglbasicshape.draw.BaseShapeView
import com.glumes.openglbasicshape.draw.texture.CubeTexture
import com.glumes.openglbasicshape.draw.texture.RectangleTexture
import com.glumes.openglbasicshape.obj.LoadedObjectVertexOnly
import com.glumes.openglbasicshape.renderers.BaseRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 14/06/2018
 */
class ScissorTestFragment : Fragment() {

    lateinit var surfaceView: BaseShapeView
    lateinit var renderer: BaseRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        renderer = ScissorRenderer(context!!)
        surfaceView = BaseShapeView(context, renderer)
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}


class ScissorRenderer(mContext: Context) : BaseRenderer(mContext) {

    lateinit var mRectangle: RectangleTexture
    //    lateinit var mCube: CubeTexture
    var mImportShape: LoadedObjectVertexOnly = LoadedObjectVertexOnly(mContext)

    lateinit var mCube: CubeTexture


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)

        mRectangle = RectangleTexture(mContext)
        mRectangle.onSurfaceCreated(gl, config)

        mCube = CubeTexture(mContext)
        mCube.onSurfaceCreated(gl, config)
    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        // 多个形状同时改变 viewport 以最后的 viewport 大小为准
        mRectangle.onSurfaceChanged(gl, width, height)
        mCube.onSurfaceChanged(gl, width, height)

    }


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        mRectangle.onDrawFrame(gl)


//        GLES20.glEnable(GLES20.GL_SCISSOR_TEST)
//        GLES20.glScissor(0, 1080 - 300, 430, 600)
//        GLES20.glClearColor(1f, 0f, 0f, 1f)
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
//
////        mCube.getMatrixStateOnly().setProjectFrustum(ratio, ratio, bottom, top, near, far)
//
//        GLES20.glDisable(GLES20.GL_SCISSOR_TEST)
        mCube.onDrawFrame(gl)
    }

    override fun onSurfaceDestroyed() {
        super.onSurfaceDestroyed()
        mRectangle.onSurfaceDestroyed()
    }
}

