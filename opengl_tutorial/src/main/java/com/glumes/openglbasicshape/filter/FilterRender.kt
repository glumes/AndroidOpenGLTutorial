package com.glumes.openglbasicshape.filter

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.glumes.camera.Camera2
import com.glumes.gpuimage.GPUImageFilter
import com.glumes.gpuimage.utils.OpenGlUtils
import com.glumes.importobject.CubeDraw
import com.glumes.openglbasicshape.base.LogUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by glumes on 27/03/2018
 */
class FilterRender(val mContext: Context, val mFrameAvailableListener: SurfaceTexture.OnFrameAvailableListener) : GLSurfaceView.Renderer {


    private var mFilter: GPUImageFilter? = null
    private var mBackgroundRed = 0f
    private var mBackgroundGreen = 0f
    private var mBackgroundBlue = 0f

    private var mTextureId = -1
    private var mSurfaceTexture: SurfaceTexture? = null

    private var mCamera2: Camera2? = null

    private var isCameraInit = false
    private var mSurfaceWidth = 0
    private var mSurfaceHeight = 0

    private val TAG = "FilterRender"

    private lateinit var mCubeDraw: CubeDraw

    init {
        mFilter = GPUImageFilter()
        mCamera2 = Camera2(mContext)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(mBackgroundRed, mBackgroundGreen, mBackgroundBlue, 1f)
//        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        mFilter!!.init()
//
        mTextureId = OpenGlUtils.getExternalOESTextureID()
        mSurfaceTexture = SurfaceTexture(mTextureId)
        mSurfaceTexture!!.setOnFrameAvailableListener(mFrameAvailableListener)

        mCubeDraw = CubeDraw(mContext)
        mCubeDraw.init()

    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
//        GLES20.glUseProgram(mFilter!!.program)
        mSurfaceWidth = width
        mSurfaceHeight = height

        mCubeDraw.onSizeChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {

        mSurfaceTexture?.updateTexImage()


        if (!isCameraInit) {
            initCameraSurfaceTexture()
            isCameraInit = true
            return
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        LogUtil.d("draw frame")
        GLES20.glUseProgram(mFilter!!.program)
        mFilter!!.onDraw(mTextureId)

        GLES20.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE_MINUS_SRC_ALPHA)

        mCubeDraw.drawSelf()

        GLES20.glDisable(GLES30.GL_BLEND)


    }

    private fun initCameraSurfaceTexture() {
        mCamera2?.setPreviewSize(mSurfaceWidth, mSurfaceHeight)
        mCamera2?.setPreviewSurfaceTexture(mSurfaceTexture!!)
        mCamera2?.openCamera()
    }


    fun changeFilter() {
        if (mFilter != null) {
            mFilter!!.destroy()
            mFilter = FilterFactory.getFilterCycle()

            mFilter!!.init()
        }
    }


}