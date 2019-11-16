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
        mGLSurfaceView.setRender(mTransitionRender)

//        mTransitionRender.setProgress(0.5f)
//        mGLSurfaceView.requestRender()
    }


    private var mLastX: Float = 0.toFloat()
    private var mLastY: Float = 0.toFloat()

    override fun onTouchEvent(event: MotionEvent): Boolean {


        val x = event.getX()
        val y = event.getY()

        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {

                mTransitionRender.setProgress(x)
                mGLSurfaceView.requestRender()
                LogUtil.d("distance is $x")
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> {
            }
        }
        return true
    }
}
