package com.glumes.openglbasicshape.filter

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.SurfaceHolder
import com.glumes.camera.Camera2

/**
 * Created by glumes on 27/03/2018
 */
class FilterSurfaceView : GLSurfaceView, SurfaceTexture.OnFrameAvailableListener {

    private val mRender = FilterRender(context!!, this)


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    init {
        setEGLContextClientVersion(2)
        setRenderer(mRender)
//        renderMode = RENDERMODE_WHEN_DIRTY
        renderMode = RENDERMODE_CONTINUOUSLY
    }


    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
//        requestRender()
    }




}