package com.glumes.openglbasicshape.transition_banner

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.LogUtil
import com.glumes.openglbasicshape.transition.TransitionRender

class TransitionBannerActivity : AppCompatActivity() {

    private lateinit var mGLSurfaceView: BannerGLSurfaceView
    private lateinit var mTransitionRender: TransitionBannerRender

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_banner)

        mGLSurfaceView = findViewById(R.id.transition_banner)

        mTransitionRender = TransitionBannerRender(this)

        mGLSurfaceView.setEGLContextClientVersion(2)
        mGLSurfaceView.setRenderer(mTransitionRender)
        mGLSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        mTransitionRender.setProgress(0.5f)
        mGLSurfaceView.requestRender()

    }
}
