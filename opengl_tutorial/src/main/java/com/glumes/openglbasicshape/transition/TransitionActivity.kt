package com.glumes.openglbasicshape.transition

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.glumes.openglbasicshape.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class TransitionActivity : AppCompatActivity() {

    private lateinit var mGLSurfaceView: GLSurfaceView
    private lateinit var mTransitionRender: TransitionRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        mGLSurfaceView = findViewById(R.id.transition_surfaceview)

        mTransitionRender = TransitionRender(this)

        mGLSurfaceView.setEGLContextClientVersion(2)
        mGLSurfaceView.setRenderer(mTransitionRender)
        mGLSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        Observable.interval(33, TimeUnit.MILLISECONDS).subscribeOn(AndroidSchedulers.mainThread()).subscribe {
            mGLSurfaceView.requestRender()
        }
    }
}
