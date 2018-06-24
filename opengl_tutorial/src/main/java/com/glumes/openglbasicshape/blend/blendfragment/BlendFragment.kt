package com.glumes.openglbasicshape.blend.blendfragment

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.openglbasicshape.draw.BaseShapeView
import com.glumes.openglbasicshape.renderers.BaseRenderer


/**
 * @Author  glumes
 */
class BlendFragment : Fragment() {

    lateinit var surfaceView: BaseShapeView
    lateinit var renderer: BaseRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        renderer = BlendRenderer(context!!)
        surfaceView = BaseShapeView(context, renderer)
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}


class BlendRenderer(mContext: Context) : BaseRenderer(mContext) {

}