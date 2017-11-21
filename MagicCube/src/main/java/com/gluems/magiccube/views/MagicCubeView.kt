package com.gluems.magiccube.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.gluems.magiccube.render.MagicCubeRender

/**
 * @Author glumes
 */
class MagicCubeView(mContext: Context) : GLSurfaceView(mContext) {


    var render: MagicCubeRender

    init {
        setEGLContextClientVersion(2)
        render = MagicCubeRender()
        setRenderer(render)
        renderMode = RENDERMODE_CONTINUOUSLY
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

    }
}