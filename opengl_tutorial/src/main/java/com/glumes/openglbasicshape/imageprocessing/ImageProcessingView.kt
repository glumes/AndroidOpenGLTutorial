package com.glumes.openglbasicshape.imageprocessing

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.glumes.openglbasicshape.base.LogUtil
import com.glumes.openglbasicshape.imageprocessing.processor.ImageHaHaProcess

/**
 * Created by glumes on 06/06/2018
 */


class ImageProcessingView : GLSurfaceView {


    private val mRenderer: ImageProcessRenderer by lazy {
        ImageProcessRenderer(context)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        LogUtil.d("init")
        setEGLContextClientVersion(2)
        setRenderer(mRenderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    fun changeFilter() {
        mRenderer.changeImageProcess(ImageHaHaProcess())
    }
}
