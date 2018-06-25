package com.glumes.openglbasicshape.light.lightfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glumes.openglbasicshape.blend.blendfragment.BlendSurfaceView

/**
 * @Author  glumes
 */
class MixLight : Fragment() {

    lateinit var surfaceView: BlendSurfaceView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surfaceView = BlendSurfaceView(context!!)
        surfaceView.requestFocus()
        surfaceView.isFocusableInTouchMode = true
        return surfaceView
    }
}