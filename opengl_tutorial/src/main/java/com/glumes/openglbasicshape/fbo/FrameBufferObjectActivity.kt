package com.glumes.openglbasicshape.fbo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.glumes.openglbasicshape.R

class FrameBufferObjectActivity : AppCompatActivity() {

    private lateinit var fboview: SurfaceView

    private lateinit var mFBORenderer: FBORenderer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_buffer_object)

        mFBORenderer = FBORenderer()
        fboview = findViewById(R.id.fboview)
        fboview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                mFBORenderer.render(fboview.holder.surface, width, height, this@FrameBufferObjectActivity)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mFBORenderer.release()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                mFBORenderer.init()
            }

        })
    }
}
