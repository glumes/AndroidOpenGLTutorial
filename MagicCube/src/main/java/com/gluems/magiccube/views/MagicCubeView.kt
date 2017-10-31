package com.gluems.magiccube.views

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

/**
 * @Author glumes
 */
class MagicCubeView(val mContext: Context) : GLSurfaceView(mContext) {




    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

    }
}